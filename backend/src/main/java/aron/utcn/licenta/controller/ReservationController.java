package aron.utcn.licenta.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationManagementService reservationManagementService;
	
	@PostMapping("/reservation")
	public void reserveParkingLot(@RequestBody ReservationDto reservation) throws ParseException {
		reservationManagementService.reserveParkingPlace(reservation);
	}
	
}
