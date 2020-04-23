package aron.utcn.licenta.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.DtoToReservationConverter;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.ReservationManagementRepository;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationManagementServiceImpl implements ReservationManagementService {

	private final ReservationManagementRepository reservationManagementRepository;
	
	private final ParkingPlaceRepository parkingPlaceRespository;
	
	private final DtoToReservationConverter dtoToReservationConverter;
	
	@Override
	@Transactional
	public void reserveParkingPlace(ReservationDto reservationDto) {
		reservationManagementRepository.saveReservation(dtoToReservationConverter.convertDtoToReservation(reservationDto));	
		reserve(reservationDto.getParkingPlaceId(), reservationDto.getLicensePlate(), reservationDto.getUserId());
	}
	
	@Transactional
	private void reserve(int parkingPlaceId, String licensePlate, int userId) {
		parkingPlaceRespository.makeReservation(parkingPlaceId, licensePlate, userId);
	}

	@Override
	@Transactional
	public void cancelReservation(int parkingPlaceId) {
		parkingPlaceRespository.free(parkingPlaceId);
	}

}
