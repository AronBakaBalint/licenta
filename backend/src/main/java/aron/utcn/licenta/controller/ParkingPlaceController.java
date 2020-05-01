package aron.utcn.licenta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.service.ParkingPlaceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParkingPlaceController {

	private final ParkingPlaceService parkingPlaceService;
	
	@GetMapping("/parking")
	public List<ParkingPlaceDto> getAllParkingPlaces(){
		return parkingPlaceService.getAllParkingPlaces();
	}
	
	@GetMapping("/parking/unoccupied/{id}")
	public List<UnconfirmedReservationDto> getUnoccupiedPlaces(@PathVariable Integer id) {
		return parkingPlaceService.findUnconfirmedReservations(id);
	}
	
	@GetMapping("/parking/reserved/{id}")
	public List<UnconfirmedReservationDto> getAllReservedPlaces(@PathVariable Integer id) {
		return parkingPlaceService.findAllReservations(id);
	}
}