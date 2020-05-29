package aron.utcn.licenta.facade;

import aron.utcn.licenta.dto.ReservationDto;

public interface ReservationFacade {

	public int reserveParkingPlace(ReservationDto reservation);

	public void extendReservation(Integer reservationId);

	public void cancelReservation(Integer reservationId);
	
	public String getReservationStatus(Integer reservationId);
}
