package aron.utcn.licenta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ParkingSpotDto;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ParkingSpotFacade;
import aron.utcn.licenta.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParkingSpotController {

	private final ParkingSpotFacade parkingSpotFacade;
	
	private final JwtTokenUtil jwtTokenUtil;
	
	@GetMapping("/parking")
	public List<ParkingSpotDto> getAllParkingPlaces(){
		return parkingSpotFacade.getAllParkingPlaces();
	}
	
	@GetMapping("/parking/unoccupied/{id}")
	public List<ReservationDto> getUnoccupiedPlaces(@PathVariable Integer id) {
		return parkingSpotFacade.findUnconfirmedReservations(id);
	}
	
	@GetMapping("/parking/reserved")
	public List<ReservationDto> getReservationHistory(@RequestHeader("Authorization") String token) {
		Integer userId = Integer.parseInt(jwtTokenUtil.getIdFromToken(token.replace("Bearer ", "")));
		return parkingSpotFacade.findAllReservations(userId);
	}
	
}