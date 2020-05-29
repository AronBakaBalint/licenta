package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.model.ParkingPlace;

@Component
public class ParkingPlaceConverter implements BaseConverter<ParkingPlace, ParkingPlaceDto>{

	@Override
	public ParkingPlace convertToEntity(ParkingPlaceDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParkingPlaceDto convertToDto(ParkingPlace entity) {
		ParkingPlaceDto parkingPlaceDto = new ParkingPlaceDto();
		parkingPlaceDto.setId(entity.getId());
		parkingPlaceDto.setOccupierCarPlate(entity.getOccupierCarPlate());
		parkingPlaceDto.setStatus(entity.getStatus());
		return parkingPlaceDto;
	}
}
