package aron.utcn.licenta.service;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;

public interface ReservationManagementService {

	public Integer reserveParkingPlace(ReservationDto reservation);
	
	public void extendReservation(int parkingPlaceId);
	
	public void cancelReservation(int parkingPlaceId);
	
	public Reservation findById(int reservationId);
	
}
