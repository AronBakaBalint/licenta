package aron.utcn.licenta.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.ParkingPlaceToDtoConverter;
import aron.utcn.licenta.converter.UnconfirmedReservationToDtoConverter;
import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.service.ParkingPlaceService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingPlaceServiceImpl implements ParkingPlaceService {

	private final ParkingPlaceRepository parkingPlaceRepository;
	
	private final ParkingPlaceToDtoConverter parkingPlaceToDtoConverter;
	
	private final UnconfirmedReservationToDtoConverter unconfirmedReservationToDtoConverter;
	
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

	@Override
	public ParkingPlaceDto findById(int id) {
		return parkingPlaceToDtoConverter.convertParkingPlaceToDto(parkingPlaceRepository.findById(id));
	}

	@Override
	public List<UnconfirmedReservationDto> findUnconfirmedReservations(int userId) {
		List<ParkingPlace> reservedPlaces = parkingPlaceRepository.findReservationsByUser(userId);
		reservedPlaces = reservedPlaces.stream().filter(rp->rp.getStatus().equals("reserved")).collect(Collectors.toList());
		return reservedPlaces.stream().map(unconfirmedReservationToDtoConverter::convertUnconfirmedReservationToDto).collect(Collectors.toList());
	}

	@Override
	public List<UnconfirmedReservationDto> findAllReservations(int userId) {
		List<ParkingPlace> reservedPlaces = parkingPlaceRepository.findReservationsByUser(userId);
		return reservedPlaces.stream().map(unconfirmedReservationToDtoConverter::convertUnconfirmedReservationToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void setArrived(String licensePlate) {
		parkingPlaceRepository.setArrived(licensePlate);
	}

}
