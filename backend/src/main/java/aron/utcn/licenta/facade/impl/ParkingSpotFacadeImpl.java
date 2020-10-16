package aron.utcn.licenta.facade.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.converter.BaseConverter;
import aron.utcn.licenta.dto.ParkingSpotDto;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ParkingSpotFacade;
import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingSpotFacadeImpl implements ParkingSpotFacade {

	private final ParkingSpotManagementService parkingSpotService;

	private final ReservationManagementService reservationService;

	private final BaseConverter<ParkingSpot, ParkingSpotDto> parkingSpotconverter;

	private final BaseConverter<Reservation, ReservationDto> reservationConverter;

	@Override
	public List<ParkingSpotDto> getAllParkingPlaces() {
		return parkingSpotService.getAllParkingPlaces().stream().map(parkingSpotconverter::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ParkingSpotDto findById(int id) {
		return parkingSpotconverter.convertToDto(parkingSpotService.findById(id));
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
