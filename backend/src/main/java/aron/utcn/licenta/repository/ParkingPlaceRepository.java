package aron.utcn.licenta.repository;

import java.util.List;

import aron.utcn.licenta.model.ParkingSpot;

public interface ParkingPlaceRepository {

	public List<ParkingSpot> getAllParkingPlaces();
	
	public void save(ParkingSpot parkingPlace);
	
	public ParkingSpot findById(int id);
	
	public void free(int parkingPlaceId);
}
