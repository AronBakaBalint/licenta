package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.model.ParkingPlace;

@Component
public class ParkingPlaceToDtoConverter {

	public ParkingPlaceDto convertParkingPlaceToDto(ParkingPlace parkingPlace) {
		ParkingPlaceDto parkingPlaceDto = new ParkingPlaceDto();
		parkingPlaceDto.setId(parkingPlace.getId());
		parkingPlaceDto.setUserId(parkingPlace.getUserId());
		parkingPlaceDto.setOccupierCarPlate(parkingPlace.getOccupierCarPlate());
		parkingPlaceDto.setStatus(parkingPlace.getStatus());
		return parkingPlaceDto;
	}
}
