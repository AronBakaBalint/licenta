package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.model.ParkingPlace;

public interface ParkingPlaceService {

	public List<ParkingPlaceDto> getAllParkingPlaces();
	
	public void save(ParkingPlace parkingPlace);
	
	public ParkingPlaceDto findById(int id);
	
	public List<UnconfirmedReservationDto> findAllReservations(int userId);
	
	public List<UnconfirmedReservationDto> findUnconfirmedReservations(int userId);
}
