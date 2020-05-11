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
		return reservation.getReservationId();
	}

	@Override
	public Optional<Reservation> findById(int reservationId) {
		Reservation reservation =  entityManager.find(Reservation.class, reservationId);
		return reservation == null ? Optional.empty() : Optional.ofNullable(reservation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Reservation> findByLicensePlate(String licensePlate) {
		List<Reservation> reservationList =  (List<Reservation>)entityManager.createQuery(
				"SELECT reservation FROM Reservation reservation WHERE licensePlate LIKE :licensePlate")
				.setParameter("licensePlate", licensePlate)
				.getResultList();
		Reservation reservation = null;
		try {
			reservation =  reservationList.get(reservationList.size()-1);
		} catch (IndexOutOfBoundsException ioube) {
			return Optional.empty();
		}
		return reservation.getStatus().equals("finished") ? Optional.empty() : Optional.ofNullable(reservation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> findReservationsByUser(int userId) {
		return (List<Reservation>)entityManager.createQuery(
				"SELECT reservation FROM Reservation reservation WHERE reservation.userId LIKE :userId")
				.setParameter("userId", userId)
				.getResultList();
	}

}
