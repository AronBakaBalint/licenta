package aron.utcn.licenta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ParkingPlaceDto;
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
}