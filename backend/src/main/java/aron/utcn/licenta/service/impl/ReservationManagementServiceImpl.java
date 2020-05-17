package aron.utcn.licenta.service.impl;

import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.DtoToReservationConverter;
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.PersonRepository;
import aron.utcn.licenta.repository.ReservationRepository;
import aron.utcn.licenta.service.ReservationManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationManagementServiceImpl implements ReservationManagementService {

	private final ReservationRepository reservationRepository;

	private final ParkingPlaceRepository parkingPlaceRespository;

	private final DtoToReservationConverter dtoToReservationConverter;

	private final PersonRepository personRepository;

	private final Environment environment;

	@Override
	@Transactional
	public Integer reserveParkingPlace(ReservationDto reservationDto) {
		Reservation reservation = dtoToReservationConverter.convertDtoToReservation(reservationDto);
		Optional<Reservation> optreservation = reservationRepository
				.findByLicensePlate(reservationDto.getLicensePlate());
		if (optreservation.isPresent() && (optreservation.get().getStatus().equals("reserved") || optreservation.get().getStatus().equals("occupied"))) {
				return -1;
		} else {
			reservation.setStatus("reserved");
			int reservationId = reservationRepository.saveReservation(reservation);
			reserve(reservationDto.getParkingPlaceId(), reservationDto.getLicensePlate(), reservationDto.getUserId());
			return reservationId;
		}
	}

	@Transactional
	private void reserve(int parkingPlaceId, String licensePlate, int userId) {
		Person user = personRepository.findById(userId);
		ParkingPlace parkingPlace = parkingPlaceRespository.findById(parkingPlaceId);
		System.out.println(parkingPlace.getId());
		parkingPlace.setReserved(licensePlate, user);
	}

	@Override
	@Transactional
	public void cancelReservation(int reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId).get();
		reservation.cancel();
		ParkingPlace parkingPlace = parkingPlaceRespository.findById(reservation.getParkingPlace().getId());
		parkingPlace.setFree();
	}

	@Override
	@Transactional
	public void extendReservation(int reservationId) {
		Double extensionCost = Double.parseDouble(environment.getProperty("parking.extension_cost"));
		Reservation reservation = reservationRepository.findById(reservationId).get();
		Person person = personRepository
				.findById(parkingPlaceRespository.findById(reservation.getParkingPlace().getId()).getUser().getId());
		person.pay(extensionCost);
	}

	@Override
	public Reservation findById(int reservationId) {
		return reservationRepository.findById(reservationId).get();
	}

}
