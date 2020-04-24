package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.model.ParkingPlace;

@Component
public class UnconfirmedReservationToDtoConverter {

	public UnconfirmedReservationDto convertUnconfirmedReservationToDto(ParkingPlace parkingPlace) {
		UnconfirmedReservationDto reservationDto = new UnconfirmedReservationDto();
		reservationDto.setLicensePlate(parkingPlace.getOccupierCarPlate());
		reservationDto.setParkingPlaceId(parkingPlace.getId());
		return reservationDto;
	}
}
