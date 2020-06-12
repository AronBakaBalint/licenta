package aron.utcn.licenta.converter;

import java.util.Date;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationConverter implements BaseConverter<Reservation, ReservationDto> {
	
	private final ParkingSpotManagementService parkingSpotService;
	
	private final PersonManagementService personService;

	@Override
	public Reservation convertToEntity(ReservationDto dto) {
		Reservation reservation = new Reservation();
		reservation.setUser(personService.findById(dto.getUserId()));
		reservation.setLicensePlate(dto.getLicensePlate());
		reservation.setReservationDate(new Date());
		reservation.setParkingPlace(parkingSpotService.findById(dto.getParkingPlaceId()));
		return reservation;
	}

	@Override
	public ReservationDto convertToDto(Reservation entity) {
		ReservationDto reservationDto = new ReservationDto();
		reservationDto.setStatus(entity.getStatus());
		reservationDto.setId(entity.getId());
		reservationDto.setLicensePlate(entity.getLicensePlate());
		reservationDto.setParkingPlaceId(entity.getParkingPlace().getId());
		return reservationDto;
	}
	
}
