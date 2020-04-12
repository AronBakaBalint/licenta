package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;

@Component
public class DtoToReservationConverter {

	public Reservation convertDtoToReservation(ReservationDto reservationDto) {
		Reservation reservation = new Reservation();
		reservation.setLicensePlateNumber(reservationDto.getLicensePlate());
		reservation.setParkingPlaceId(reservationDto.getParkingPlaceId());
		reservation.setStatus("reserved");
		return reservation;
	}
	
}
