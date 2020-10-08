package aron.utcn.licenta.facade;

import java.util.List;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.SimpleDate;

public interface ReservationFacade {

	public int reserveParkingPlace(ReservationDto reservation);

	public void extendReservation(Integer reservationId);
	
	public List<ReservationDto> getAllActiveReservations(Integer id, SimpleDate reservationDate);

	public void cancelReservation(Integer reservationId);
	
	public String getReservationStatus(Integer reservationId);
}
