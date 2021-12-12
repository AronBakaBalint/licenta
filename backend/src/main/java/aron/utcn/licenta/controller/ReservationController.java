package aron.utcn.licenta.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ReservationFacade;
import aron.utcn.licenta.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	@Value("${parking.price_per_hour}")
	private Double pricePerHour;
	
	@Value("${parking.extension_cost}")
	private Double extensionCost;
	
	private final ReservationFacade reservationFacade;
	
	private final JwtTokenUtil jwtTokenUtil;
	
	@GetMapping("/reservation/{id}")
	public Boolean isReservationPending(@PathVariable Integer id) {
		return reservationFacade.isReservationPending(id);
	}
	
	@PostMapping("/reservation")
	public Integer makeReservation(@RequestHeader("Authorization") String token, @RequestBody ReservationDto reservation) {
		Integer userId = Integer.parseInt(jwtTokenUtil.getIdFromToken(token.replace("Bearer ", ""))); 
		reservation.setUserId(userId);
		return reservationFacade.reserveParkingPlace(reservation);
	}
	
	@DeleteMapping("/reservation/{id}")
	public void cancelReservation(@PathVariable("id") Integer reservationId) {
		reservationFacade.cancelReservation(reservationId);
	}
	
	@GetMapping("/reservation/schedule")
	public List<ReservationDto> getReservationSchedule(@RequestParam("id") Integer parkingSpotId, @RequestParam("date") String reservationDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate reservationLocalDate = LocalDate.parse(reservationDate, formatter);
		List<ReservationDto> response = reservationFacade.getReservationSchedule(parkingSpotId, reservationLocalDate);
		return response;
	}
	
	@GetMapping("/reservation/extension")
	public Double getExtensionCost() {
		return extensionCost;
	}
	
	@GetMapping("/reservation")
	public Double getPricePerHour() {
		return pricePerHour;
	}
	
}
