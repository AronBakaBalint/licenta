package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.model.Reservation;

public interface ParkingSpotManagementService {

	public List<ParkingSpot> getAllParkingPlaces();
	
	public void save(ParkingSpot parkingPlace);
	
	public ParkingSpot findById(int id);
	
	public List<Reservation> findAllReservations(int userId);
	
	public List<Reservation> findUnconfirmedReservations(int userId);
	
	public void handleScannedCode(String qrCode);
	
	public void clearUnoccupiedPlaces();
}
