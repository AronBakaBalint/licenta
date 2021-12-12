package aron.utcn.licenta.service;

import java.time.LocalDate;
import java.util.List;

import aron.utcn.licenta.model.Reservation;

public interface ReservationManagementService {

	public Integer reserveParkingPlace(Reservation reservation);
	
	public void cancelReservation(int reservationId);
	
	public Reservation findById(int reservationId);
	
	public List<Reservation> getReservationSchedule(Integer parkingSpotId, LocalDate reservationDate);
	
	public List<Reservation> findReservationsByUser(int userId);
	
	public Boolean isReservationPending(Integer reservationId);
	
	public List<Reservation> findAllReservationsOfUser(int userId);
	
	public List<Reservation> findUnconfirmedReservationsOfUser(int userId);
	
}
