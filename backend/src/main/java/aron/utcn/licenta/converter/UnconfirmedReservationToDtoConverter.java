package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.model.Reservation;

@Component
public class UnconfirmedReservationToDtoConverter {

	public UnconfirmedReservationDto convertUnconfirmedReservationToDto(Reservation reservation) {
		UnconfirmedReservationDto reservationDto = new UnconfirmedReservationDto();
		reservationDto.setStatus(reservation.getStatus());
		reservationDto.setReservationId(reservation.getId());
		reservationDto.setLicensePlate(reservation.getLicensePlate());
		reservationDto.setParkingPlaceId(reservation.getParkingPlace().getId());
		return reservationDto;
	}
}
