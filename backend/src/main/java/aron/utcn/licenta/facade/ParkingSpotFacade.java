package aron.utcn.licenta.facade;

import java.util.List;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.ReservationDto;

public interface ParkingSpotFacade {

	public List<ParkingPlaceDto> getAllParkingPlaces();
	
	public ParkingPlaceDto findById(int id);
	
	public List<ReservationDto> findAllReservations(int userId);
	
	public List<ReservationDto> findUnconfirmedReservations(int userId);
}
