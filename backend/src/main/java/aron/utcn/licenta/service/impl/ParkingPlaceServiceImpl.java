package aron.utcn.licenta.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.DtoToReservationConverter;
import aron.utcn.licenta.converter.ParkingPlaceToDtoConverter;
import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.service.ParkingPlaceService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingPlaceServiceImpl implements ParkingPlaceService {

	private final ParkingPlaceRepository parkingPlaceRepository;
	
	private final ParkingPlaceToDtoConverter parkingPlaceToDtoConverter;
	
	@Override
	public List<ParkingPlaceDto> getAllParkingPlaces() {
		List<ParkingPlace> parkingPLaces = parkingPlaceRepository.getAllParkingPlaces();
		return parkingPLaces.stream().map(p -> parkingPlaceToDtoConverter.convertParkingPlaceToDto(p)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void save(ParkingPlace parkingPlace) {
		parkingPlaceRepository.save(parkingPlace);
	}

}
