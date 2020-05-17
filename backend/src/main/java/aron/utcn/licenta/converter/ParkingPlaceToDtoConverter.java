package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.model.ParkingPlace;

@Component
public class ParkingPlaceToDtoConverter {

	public ParkingPlaceDto convertParkingPlaceToDto(ParkingPlace parkingPlace) {
		ParkingPlaceDto parkingPlaceDto = new ParkingPlaceDto();
		parkingPlaceDto.setId(parkingPlace.getId());
		try {
			parkingPlaceDto.setUserId(parkingPlace.getUser().getId());
		} catch(NullPointerException npe) {	}
		
		parkingPlaceDto.setOccupierCarPlate(parkingPlace.getOccupierCarPlate());
		parkingPlaceDto.setStatus(parkingPlace.getStatus());
		return parkingPlaceDto;
	}
}
