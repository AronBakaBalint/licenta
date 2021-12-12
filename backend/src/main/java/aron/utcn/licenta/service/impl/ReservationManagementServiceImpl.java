package aron.utcn.licenta.service.impl;

import static java.util.function.Predicate.not;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
		int reservationId = reservationRepository.save(reservation);
		reserve(reservationId, reservation.getParkingSpotId(), reservation.getLicensePlate(), reservation.getUserId());
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
	public Reservation findById(int reservationId) {
		return reservationRepository.findById(reservationId).get();
	}

	@Override
	public List<Reservation> findReservationsByUser(int userId) {
		return reservationRepository.findByUser(userId);
	}

	@Override
	public Boolean isReservationPending(Integer reservationId) {
		Optional<Reservation> reservation = reservationRepository.findById(reservationId);
		if(reservation.isPresent()) {
			return reservation.get().isReserved();
		}
		return false;
	}

	@Override
	public List<Reservation> getReservationSchedule(Integer parkingSpotId, LocalDate reservationDate) {
		return reservationRepository.getAll().stream()
				.filter(reservation -> reservation.getParkingSpotId() == parkingSpotId)
				.filter(reservation -> reservation.hasDate(reservationDate))
				.filter(not(Reservation::isCancelled))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Reservation> findUnconfirmedReservationsOfUser(int userId) {
		return reservationRepository.findByUser(userId);
	}

	@Override
	public List<Reservation> findAllReservationsOfUser(int userId) {
		return reservationRepository.findByUser(userId);
	}

}
