package aron.utcn.licenta.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.MessageDto;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final Environment environment;
	
	private final ReservationManagementService reservationManagementService;
	
	@PostMapping("/reservation")
	public MessageDto reserveParkingPlace(@RequestBody ReservationDto reservation) {
		reservationManagementService.reserveParkingPlace(reservation);
		return new MessageDto("ok");
	}
	
	@DeleteMapping("/reservation/{id}")
	public MessageDto cancelReservation(@PathVariable("id") Integer parkingPlaceId) {
		reservationManagementService.cancelReservation(parkingPlaceId);
		return new MessageDto("ok");
	}
	
	@GetMapping("reservation/extension")
	public MessageDto getExtensionCost() {
		return new MessageDto(environment.getProperty("parking.extension_cost"));
	}
	
}
