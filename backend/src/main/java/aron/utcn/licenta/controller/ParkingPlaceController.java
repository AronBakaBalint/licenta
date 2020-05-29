package aron.utcn.licenta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ParkingSpotFacade;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParkingPlaceController {

	private final ParkingSpotFacade parkingSpotFacade;
	
	@GetMapping("/parking")
	public List<ParkingPlaceDto> getAllParkingPlaces(){
		return parkingSpotFacade.getAllParkingPlaces();
	}
	
	@GetMapping("/parking/unoccupied/{id}")
	public List<ReservationDto> getUnoccupiedPlaces(@PathVariable Integer id) {
		return parkingSpotFacade.findUnconfirmedReservations(id);
	}
	
	@GetMapping("/parking/reserved/{id}")
	public List<ReservationDto> getAllReservedPlaces(@PathVariable Integer id) {
		return parkingSpotFacade.findAllReservations(id);
	}
}