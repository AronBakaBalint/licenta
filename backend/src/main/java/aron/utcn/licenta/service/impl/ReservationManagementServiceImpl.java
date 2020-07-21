package aron.utcn.licenta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.PersonRepository;
import aron.utcn.licenta.repository.ReservationRepository;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationManagementServiceImpl implements ReservationManagementService {

	private final ReservationRepository reservationRepository;

	private final ParkingPlaceRepository parkingPlaceRespository;

	private final PersonRepository personRepository;

	private final Environment environment;

	@Override
	@Transactional
	public Integer reserveParkingPlace(Reservation reservation) {
		Optional<Reservation> optreservation = reservationRepository.findByLicensePlate(reservation.getLicensePlate());
		if (optreservation.isPresent() && (optreservation.get().getStatus().equals("reserved")
				|| optreservation.get().getStatus().equals("occupied"))) {
			return -1;
		} else {
			Person user = reservation.getUser();
			String reservationCost = environment.getProperty("parking.reservation_cost");
			user.pay(Double.parseDouble(reservationCost));
			reservation.setStatus("reserved");
			int reservationId = reservationRepository.saveReservation(reservation);
			reserve(reservationId, reservation.getParkingPlace().getId(), reservation.getLicensePlate(),
					reservation.getUser().getId());
			return reservationId;
		}
	}

	@Transactional
	private void reserve(int reservationId, int parkingPlaceId, String licensePlate, int userId) {
		Person user = personRepository.findById(userId);
		ParkingPlace parkingPlace = parkingPlaceRespository.findById(parkingPlaceId);
		parkingPlace.setReserved(licensePlate, user);
		parkingPlace.setReservation(reservationRepository.findById(reservationId).get());
	}

	@Override
	@Transactional
	public void cancelReservation(int reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId).get();
		reservation.cancel();
		ParkingPlace parkingPlace = parkingPlaceRespository.findById(reservation.getParkingPlace().getId());
		parkingPlace.setFree();
	}

	@Override
	@Transactional
	public void extendReservation(int reservationId) {
		Double extensionCost = Double.parseDouble(environment.getProperty("parking.extension_cost"));
		Reservation reservation = reservationRepository.findById(reservationId).get();
		Person person = personRepository
				.findById(parkingPlaceRespository.findById(reservation.getParkingPlace().getId()).getUser().getId());
		person.pay(extensionCost);
	}

	@Override
	public Reservation findById(int reservationId) {
		return reservationRepository.findById(reservationId).get();
	}

	@Override
	public List<Reservation> findReservationsByUser(int userId) {
		return reservationRepository.findReservationsByUser(userId);
	}

	@Override
	public String getReservationStatus(Integer reservationId) {
		return reservationRepository.findById(reservationId).get().getStatus();
	}

}
