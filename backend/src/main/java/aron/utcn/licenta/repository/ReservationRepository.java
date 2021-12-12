package aron.utcn.licenta.repository;

import java.util.List;
import java.util.Optional;

import aron.utcn.licenta.model.Reservation;

public interface ReservationRepository {

	public Integer save(Reservation reservation);
	
	public Optional<Reservation> findById(int reservationId);
	
	public Optional<List<Reservation>> findByLicensePlate(String licensePlate);
	
	public List<Reservation> getAll();
	
	public List<Reservation> findByUser(int userId);

}
