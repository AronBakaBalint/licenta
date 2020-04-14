package aron.utcn.licenta.controller;

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

	private final ReservationManagementService reservationManagementService;
	
	@PostMapping("/reservation")
	public MessageDto reserveParkingPlace(@RequestBody ReservationDto reservation) {
		reservationManagementService.reserveParkingPlace(reservation);
		return new MessageDto("ok");
	}
	
}
