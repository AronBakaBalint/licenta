package aron.utcn.licenta.service;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;

public interface ReservationManagementService {

	public Integer reserveParkingPlace(ReservationDto reservation);
	
	public void extendReservation(int reservationId);
	
	public void cancelReservation(int reservationId);
	
	public Reservation findById(int reservationId);
	
}
