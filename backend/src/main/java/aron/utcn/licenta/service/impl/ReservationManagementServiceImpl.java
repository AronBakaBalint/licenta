package aron.utcn.licenta.service.impl;

import java.text.ParseException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.DtoToReservationConverter;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.repository.ReservationManagementRepository;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationManagementServiceImpl implements ReservationManagementService {

	private final ReservationManagementRepository reservationManagementRepository;
	
	private final DtoToReservationConverter dtoToReservationConverter;
	
	@Override
	@Transactional
	public void reserveParkingPlace(ReservationDto reservationDto) throws ParseException {
		reservationManagementRepository.reserveParkingLot(dtoToReservationConverter.convertDtoToReservation(reservationDto));		
	}

}
