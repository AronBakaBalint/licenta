package aron.utcn.licenta.service;

import aron.utcn.licenta.dto.ReservationDto;

public interface ReservationManagementService {

	public void reserveParkingPlace(ReservationDto reservation);
	
	public void extendReservation(int parkingPlaceId);
	
	public void cancelReservation(int parkingPlaceId);
	
}
