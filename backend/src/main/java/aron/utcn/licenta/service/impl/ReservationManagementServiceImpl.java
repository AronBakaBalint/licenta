package aron.utcn.licenta.service.impl;

import static java.util.function.Predicate.not;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.model.ParkingSpot;
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

	@Value("${parking.price_per_hour}")
	private Double pricePerHour;

	@Value("${parking.extension_cost}")
	private Double extensionCost;

	private final ReservationRepository reservationRepository;

	private final ParkingPlaceRepository parkingPlaceRespository;

	private final PersonRepository personRepository;

	@Override
	@Transactional
	public Integer reserveParkingPlace(Reservation reservation) {
		Person user = reservation.getUser();
		user.pay(pricePerHour * reservation.getDuration());
		reservation.setReserved();
		int reservationId = reservationRepository.saveReservation(reservation);
		reserve(reservationId, reservation.getParkingSpotId(), reservation.getLicensePlate(), reservation.getUser().getId());
		return reservationId;

	}

	@Transactional
	private void reserve(int reservationId, int parkingPlaceId, String licensePlate, int userId) {
		Person user = personRepository.findById(userId);
		ParkingSpot parkingPlace = parkingPlaceRespository.findById(parkingPlaceId);
		parkingPlace.setReserved(licensePlate, user);
		parkingPlace.setReservation(reservationRepository.findById(reservationId).get());
	}

	@Override
	@Transactional
	public void cancelReservation(int reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId).get();
		reservation.cancel();
		ParkingSpot parkingPlace = parkingPlaceRespository.findById(reservation.getParkingSpotId());
		parkingPlace.setFree();
	}

	@Override
	@Transactional
	public void extendReservation(int reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId).get();
		Person person = personRepository
				.findById(parkingPlaceRespository.findById(reservation.getParkingSpotId()).getUser().getId());
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
	public Boolean isReservationPending(Integer reservationId) {
		return reservationRepository.findById(reservationId).get().isReserved();
	}

	@Override
	public List<Reservation> getReservationSchedule(Integer parkingSpotId, LocalDate reservationDate) {
		return reservationRepository.getAllReservations().stream()
				.filter(reservation -> reservation.getParkingSpotId() == parkingSpotId)
				.filter(reservation -> reservation.hasDate(reservationDate)).filter(not(Reservation::isCancelled))
				.collect(Collectors.toList());
	}

}
