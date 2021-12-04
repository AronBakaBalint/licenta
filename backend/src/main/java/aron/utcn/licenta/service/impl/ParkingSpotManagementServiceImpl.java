package aron.utcn.licenta.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.ReservationRepository;
import aron.utcn.licenta.service.ArduinoService;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingSpotManagementServiceImpl implements ParkingSpotManagementService {

	private final ArduinoService arduinoService;

	private final ParkingPlaceRepository parkingPlaceRepository;

	private final ReservationRepository reservationRepository;

	@Override
	public List<ParkingSpot> getAllParkingPlaces() {
		return parkingPlaceRepository.getAllParkingPlaces();
	}

	@Override
	@Transactional
	public void save(ParkingSpot parkingPlace) {
		parkingPlaceRepository.save(parkingPlace);
	}

	@Override
	public ParkingSpot findById(int id) {
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
				ParkingSpot parkingPlace = parkingPlaceRepository.findById(reservation.getParkingSpotId());
				if (parkingPlace.isOccupied()) {
					reservation.setFinished();
					parkingPlace.setFree();
					displayOnLCD("Goodbye");
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

	private void displayOnLCD(String message) {
		arduinoService.displayOnLCD(message);
	}

	private void openBarrier() {
		arduinoService.activateBarrier();
	}

}
