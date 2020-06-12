package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Reservation;

public interface ParkingSpotManagementService {

	public List<ParkingPlace> getAllParkingPlaces();
	
	public void save(ParkingPlace parkingPlace);
	
	public ParkingPlace findById(int id);
	
	public List<Reservation> findAllReservations(int userId);
	
	public List<Reservation> findUnconfirmedReservations(int userId);
	
	public void handleScannedCode(String qrCode);
	
	public void clearUnoccupiedPlaces();
}
