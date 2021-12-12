package aron.utcn.licenta.facade.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.converter.BaseConverter;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ReservationFacade;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationFacadeImpl implements ReservationFacade {

	private final BaseConverter<Reservation, ReservationDto> reservationConverter;
	
	private final ReservationManagementService reservationService;
	
	@Override
	public int reserveParkingPlace(ReservationDto reservation) {
		return reservationService.reserveParkingPlace(reservationConverter.convertToEntity(reservation));
	}

	@Override
	public void cancelReservation(Integer reservationId) {
		reservationService.cancelReservation(reservationId);
	}

	@Override
	public Boolean isReservationPending(Integer reservationId) {
		return reservationService.isReservationPending(reservationId);
	}

	@Override
	public List<ReservationDto> getReservationSchedule(Integer parkingSpotId, LocalDate reservationDate) {
		return reservationService.getReservationSchedule(parkingSpotId, reservationDate).stream().map(reservationConverter::convertToDto).collect(Collectors.toList());
	}
	
	@Override
	public List<ReservationDto> findAllReservations(int userId) {
		List<Reservation> reservations = reservationService.findReservationsByUser(userId);
		return reservations.stream().map(reservationConverter::convertToDto).collect(Collectors.toList());
	}

	@Override
	public List<ReservationDto> findUnconfirmedReservations(int userId) {
		List<Reservation> reservedPlaces = reservationService.findReservationsByUser(userId);
		reservedPlaces = reservedPlaces.stream().filter(Reservation::isReserved).collect(Collectors.toList());
		return reservedPlaces.stream().map(reservationConverter::convertToDto).collect(Collectors.toList());
	}

}
