package aron.utcn.licenta.controller;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ReservationFacade;
import aron.utcn.licenta.model.SimpleDate;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final Environment environment;
	
	private final ReservationFacade reservationFacade;
	
	@GetMapping("/reservation/{id}")
	public Boolean isReservationPending(@PathVariable Integer id) {
		return reservationFacade.isReservationPending(id);
	}
	
	@PostMapping("/reservation")
	public Integer makeReservation(@RequestBody ReservationDto reservation) {
		return reservationFacade.reserveParkingPlace(reservation);
	}
	
	@PutMapping("/reservation/{id}")
	public void extendReservation(@PathVariable("id") Integer reservationId) {
		reservationFacade.extendReservation(reservationId);
	}
	
	@DeleteMapping("/reservation/{id}")
	public void cancelReservation(@PathVariable("id") Integer reservationId) {
		reservationFacade.cancelReservation(reservationId);
	}
	
	@PostMapping("/reservation/date/{id}")
	public List<ReservationDto> getAllActiveReservations(@PathVariable Integer id, @RequestBody SimpleDate reservationDate) {
		List<ReservationDto> response = reservationFacade.getAllActiveReservations(id, reservationDate);
		return response;
	}
	
	@GetMapping("/reservation/extension")
	public Double getExtensionCost() {
		return Double.parseDouble(environment.getProperty("parking.extension_cost"));
	}
	
	@GetMapping("/reservation")
	public Double getReservationCost() {
		return Double.parseDouble(environment.getProperty("parking.reservation_cost"));
	}
	
}
