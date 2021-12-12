package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.model.Reservation;

public interface ParkingSpotManagementService {

	public List<ParkingSpot> getAll();
	
	public void save(ParkingSpot parkingPlace);
	
	public ParkingSpot findById(int id);
	
	public void handleScannedCode(String qrCode);
}
