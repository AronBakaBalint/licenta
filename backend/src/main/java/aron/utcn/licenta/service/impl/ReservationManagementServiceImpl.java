package aron.utcn.licenta.service.impl;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.DtoToReservationConverter;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.PersonHibernateRepository;
import aron.utcn.licenta.repository.ReservationManagementRepository;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationManagementServiceImpl implements ReservationManagementService {

	private final ReservationManagementRepository reservationManagementRepository;
	
	private final ParkingPlaceRepository parkingPlaceRespository;
	
	private final DtoToReservationConverter dtoToReservationConverter;
	
	private final PersonHibernateRepository personRepository;
	
	private final Environment environment;
	
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

	@Override
	@Transactional
	public void extendReservation(int parkingPlaceId) {
		Double extensionCost = Double.parseDouble(environment.getProperty("parking.extension_cost"));
		Person person = personRepository.findById(parkingPlaceRespository.findById(parkingPlaceId).getUserId());
		person.pay(extensionCost);
	}

}
