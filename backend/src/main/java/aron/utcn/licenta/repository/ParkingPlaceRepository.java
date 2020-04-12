package aron.utcn.licenta.repository;

import java.util.List;

import aron.utcn.licenta.model.ParkingPlace;

public interface ParkingPlaceRepository {

	public List<ParkingPlace> getAllParkingPlaces();
	
	public void makeReservation(int parkingPlaceId, String licensePlate);
	
	public void save(ParkingPlace parkingPlace);
}
