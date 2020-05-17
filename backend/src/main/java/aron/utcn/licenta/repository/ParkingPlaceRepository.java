package aron.utcn.licenta.repository;

import java.util.List;

import aron.utcn.licenta.model.ParkingPlace;

public interface ParkingPlaceRepository {

	public List<ParkingPlace> getAllParkingPlaces();
	
	public void save(ParkingPlace parkingPlace);
	
	public ParkingPlace findById(int id);
	
	public void free(int parkingPlaceId);
}
