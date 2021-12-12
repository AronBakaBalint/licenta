package aron.utcn.licenta.facade;

import java.time.LocalDate;
import java.util.List;

import aron.utcn.licenta.dto.ReservationDto;

public interface ReservationFacade {

	public int reserveParkingPlace(ReservationDto reservation);
	
	public List<ReservationDto> getReservationSchedule(Integer id, LocalDate reservationDate);

	public void cancelReservation(Integer reservationId);
	
	public Boolean isReservationPending(Integer reservationId);
	
	public List<ReservationDto> findAllReservations(int userId);
	
	public List<ReservationDto> findUnconfirmedReservations(int userId);
}
