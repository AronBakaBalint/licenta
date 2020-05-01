package aron.utcn.licenta.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.MessageDto;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final Environment environment;
	
	private final ReservationManagementService reservationManagementService;
	
	@GetMapping("/reservation/{id}")
	public MessageDto getReservationStatus(@PathVariable Integer id) {
		Reservation reservation = reservationManagementService.findById(id);
		return new MessageDto(reservation.getStatus());
	}
	
	@PostMapping("/reservation")
	public MessageDto makeReservation(@RequestBody ReservationDto reservation) {
		int reservationId = reservationManagementService.reserveParkingPlace(reservation);
		return new MessageDto(reservationId+"");
	}
	
	@PutMapping("/reservation/{id}")
	public void extendReservation(@PathVariable("id") Integer reservationId) {
		reservationManagementService.extendReservation(reservationId);
	}
	
	@DeleteMapping("/reservation/{id}")
	public void cancelReservation(@PathVariable("id") Integer reservationId) {
		reservationManagementService.cancelReservation(reservationId);
	}
	
	@GetMapping("reservation/extension")
	public MessageDto getExtensionCost() {
		return new MessageDto(environment.getProperty("parking.extension_cost"));
	}
	
}
