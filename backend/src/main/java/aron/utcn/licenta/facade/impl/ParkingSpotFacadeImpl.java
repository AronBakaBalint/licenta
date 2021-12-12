package aron.utcn.licenta.facade.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.converter.BaseConverter;
import aron.utcn.licenta.dto.ParkingSpotDto;
import aron.utcn.licenta.facade.ParkingSpotFacade;
import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingSpotFacadeImpl implements ParkingSpotFacade {

	private final ParkingSpotManagementService parkingSpotService;

	private final BaseConverter<ParkingSpot, ParkingSpotDto> parkingSpotconverter;

	@Override
	public List<ParkingSpotDto> getAllParkingSpots() {
		return parkingSpotService.getAll().stream().map(parkingSpotconverter::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ParkingSpotDto findById(int id) {
		return parkingSpotconverter.convertToDto(parkingSpotService.findById(id));
	}

}
