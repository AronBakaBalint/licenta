package aron.utcn.licenta.service.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.PersonRepository;
import aron.utcn.licenta.repository.ReservationRepository;
import aron.utcn.licenta.service.ArduinoService;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingSpotManagementServiceImpl implements ParkingSpotManagementService {

	private final Environment environment;

	private final ArduinoService arduinoService;

	private final ParkingPlaceRepository parkingPlaceRepository;

	private final ReservationRepository reservationRepository;

	private final PersonRepository personRepository;

	@Override
	public List<ParkingPlace> getAllParkingPlaces() {
		return parkingPlaceRepository.getAllParkingPlaces();
	}

	@Override
	@Transactional
	public void save(ParkingPlace parkingPlace) {
		parkingPlaceRepository.save(parkingPlace);
	}

	@Override
	public ParkingPlace findById(int id) {
		return parkingPlaceRepository.findById(id);
	}

	@Override
	public List<Reservation> findUnconfirmedReservations(int userId) {
		return reservationRepository.findReservationsByUser(userId);
	}

	@Override
	public List<Reservation> findAllReservations(int userId) {
		return reservationRepository.findReservationsByUser(userId);
	}

	@Override
	@Transactional
	public void handleScannedCode(String qrCode) {
		int reservationId = 0;
		try {
			reservationId = Integer.parseInt(qrCode);
		} catch (NumberFormatException nfe) {
			displayOnLCD("no reservation found");
			return;
		}
		Optional<Reservation> optreservation = reservationRepository.findById(reservationId);
		if (optreservation.isPresent()) {
			Reservation reservation = optreservation.get();
			if (reservation.isExpired()) {
				displayOnLCD("reservation expired");
			} else {
				ParkingPlace parkingPlace = parkingPlaceRepository.findById(reservation.getParkingPlace().getId());
				if (parkingPlace.isOccupied()) {
					Person person = personRepository.findById(parkingPlace.getUser().getId());
					double price = calculatePrice(parkingPlace);
					DecimalFormat df = new DecimalFormat("#.##");
					df.setRoundingMode(RoundingMode.CEILING);
					price = Double.valueOf(df.format(price));
					if (person.hasEnoughMoney(price)) {
						person.pay(price);
						reservation.setFinished();
						parkingPlace.setFree();
						df.setRoundingMode(RoundingMode.FLOOR);
						displayOnLCD(price + " lei;" + df.format(person.getBalance()));
					} else {
						displayOnLCD("no money");
					}
				} else {
					openBarrier();
					Person user = reservation.getUser();
					String reservationCost = environment.getProperty("parking.reservation_cost");
					user.addMoney(Double.parseDouble(reservationCost));
					reservation.setOccupied();
					parkingPlace.setOccupied();
					parkingPlace.setArrivalTime(new Date());
				}
			}
		} else {
			displayOnLCD("no reservation found");
		}
	}

	private float calculatePrice(ParkingPlace parkingPlace) {
		Date departureTime = new Date();
		Date arrivalTime = parkingPlace.getArrivalTime();
		long diffInMillies = Math.abs(departureTime.getTime() - arrivalTime.getTime());
		long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		float pricePerHour = Float.parseFloat(environment.getProperty("parking.price_per_hour"));
		return diff * pricePerHour / 3600;
	}

	private void displayOnLCD(String message) {
		arduinoService.displayOnLCD(message);
	}

	private void openBarrier() {
		arduinoService.activateBarrier();
	}

}
