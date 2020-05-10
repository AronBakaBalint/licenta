package aron.utcn.licenta.repository;

import java.util.List;
import java.util.Optional;

import aron.utcn.licenta.model.Reservation;

public interface ReservationRepository {

	public Integer saveReservation(Reservation reservation);
	
	public Reservation findById(int reservationId);
	
	public Optional<Reservation> findByLicensePlate(String licensePlate);
	
	public List<Reservation> findReservationsByUser(int userId);

}
