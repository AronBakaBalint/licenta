package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.model.SimpleDate;

public interface ReservationManagementService {

	public Integer reserveParkingPlace(Reservation reservation);
	
	public void extendReservation(int reservationId);
	
	public void cancelReservation(int reservationId);
	
	public Reservation findById(int reservationId);
	
	public List<Reservation> getAllActiveReservations(Integer parkingSpotId, SimpleDate reservationDate);
	
	public List<Reservation> findReservationsByUser(int userId);
	
	public String getReservationStatus(Integer reservationId);
	
}
