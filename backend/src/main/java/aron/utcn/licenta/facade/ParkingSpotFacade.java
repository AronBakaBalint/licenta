package aron.utcn.licenta.facade;

import java.util.List;

import aron.utcn.licenta.dto.ParkingSpotDto;

public interface ParkingSpotFacade {

	public List<ParkingSpotDto> getAllParkingSpots();
	
	public ParkingSpotDto findById(int id);
}
