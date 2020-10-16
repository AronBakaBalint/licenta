package aron.utcn.licenta.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

	private final EntityManager entityManager;

	@Override
	public Integer saveReservation(Reservation reservation) {
		entityManager.persist(reservation);
		return reservation.getId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<Reservation> findById(int reservationId) {
		List<Reservation> reservations = entityManager
				.createQuery("SELECT r FROM Reservation r left join fetch r.parkingSpot p WHERE r.id = :id")
				.setParameter("id", reservationId).getResultList();
		return reservations.isEmpty() ? Optional.empty() : Optional.of(reservations.get(0));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<List<Reservation>> findByLicensePlate(String licensePlate) {
		List<Reservation> reservationList = entityManager
				.createQuery("SELECT reservation FROM Reservation reservation WHERE licensePlate LIKE :licensePlate")
				.setParameter("licensePlate", licensePlate).getResultList();
		return reservationList.isEmpty() ? Optional.empty() : Optional.of(reservationList);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Reservation> findReservationsByUser(int userId) {
		return entityManager
				.createQuery("SELECT reservation FROM Reservation reservation WHERE reservation.user.id LIKE :userId")
				.setParameter("userId", userId).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Reservation> getAllReservations() {
		return entityManager.createQuery("SELECT r FROM Reservation r").getResultList();
	}

}
