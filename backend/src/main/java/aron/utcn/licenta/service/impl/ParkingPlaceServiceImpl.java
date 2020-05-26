package aron.utcn.licenta.service.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.ParkingPlaceToDtoConverter;
import aron.utcn.licenta.converter.UnconfirmedReservationToDtoConverter;
import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.PersonRepository;
import aron.utcn.licenta.repository.ReservationRepository;
import aron.utcn.licenta.service.ArduinoService;
import aron.utcn.licenta.service.ParkingPlaceService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingPlaceServiceImpl implements ParkingPlaceService {

	private final Environment environment;

	private final ArduinoService arduinoService;

	private final ParkingPlaceRepository parkingPlaceRepository;

	private final ParkingPlaceToDtoConverter parkingPlaceToDtoConverter;

	private final ReservationRepository reservationRepository;

	private final PersonRepository personRepository;

	private final UnconfirmedReservationToDtoConverter unconfirmedReservationToDtoConverter;

	@Override
	public List<ParkingPlaceDto> getAllParkingPlaces() {
		List<ParkingPlace> parkingPLaces = parkingPlaceRepository.getAllParkingPlaces();
		return parkingPLaces.stream().map(p -> parkingPlaceToDtoConverter.convertParkingPlaceToDto(p))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void save(ParkingPlace parkingPlace) {
		parkingPlaceRepository.save(parkingPlace);
	}

	@Override
	public ParkingPlaceDto findById(int id) {
		return parkingPlaceToDtoConverter.convertParkingPlaceToDto(parkingPlaceRepository.findById(id));
	}

	@Override
	public List<UnconfirmedReservationDto> findUnconfirmedReservations(int userId) {
		List<Reservation> reservedPlaces = reservationRepository.findReservationsByUser(userId);
		reservedPlaces = reservedPlaces.stream().filter(rp -> rp.getStatus().equals("reserved"))
				.collect(Collectors.toList());
		return reservedPlaces.stream().map(unconfirmedReservationToDtoConverter::convertUnconfirmedReservationToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<UnconfirmedReservationDto> findAllReservations(int userId) {
		List<Reservation> reservedPlaces = reservationRepository.findReservationsByUser(userId);
		return reservedPlaces.stream().map(unconfirmedReservationToDtoConverter::convertUnconfirmedReservationToDto)
				.collect(Collectors.toList());
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
