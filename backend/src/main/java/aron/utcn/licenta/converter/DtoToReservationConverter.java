package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DtoToReservationConverter {
	
	private final ParkingPlaceRepository parkingPlaceService;
	
	private final PersonRepository personRepository;

	public Reservation convertDtoToReservation(ReservationDto reservationDto) {
		Reservation reservation = new Reservation();
		reservation.setUser(personRepository.findById(reservationDto.getUserId()));
		reservation.setLicensePlate(reservationDto.getLicensePlate());
		reservation.setParkingPlace(parkingPlaceService.findById(reservationDto.getParkingPlaceId()));
		return reservation;
	}
	
}
