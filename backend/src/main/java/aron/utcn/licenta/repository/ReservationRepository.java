package aron.utcn.licenta.repository;

import java.util.List;
import java.util.Optional;

import aron.utcn.licenta.model.Reservation;

public interface ReservationRepository {

	public Integer saveReservation(Reservation reservation);
	
	public Optional<Reservation> findById(int reservationId);
	
	public Optional<List<Reservation>> findByLicensePlate(String licensePlate);
	
	public List<Reservation> getAllReservations();
	
	public List<Reservation> findReservationsByUser(int userId);

}
