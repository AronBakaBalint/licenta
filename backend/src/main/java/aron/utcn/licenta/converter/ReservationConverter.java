package aron.utcn.licenta.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		reservation.setParkingSpot(parkingSpotService.findById(dto.getParkingSpotId()));
		
		String date = dto.getStartTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		Date reservationDate;
		try {
			reservationDate = new SimpleDateFormat("dd/MM/yyyy HH").parse(localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear() + " " + dto.getDuration().get(0));
			reservation.setReservationDate(reservationDate);
		} catch (ParseException e) {
			System.err.println("Date Conversion Error!");
		}  
		reservation.setDuration(dto.getDuration().size());
		return reservation;
	}

	@Override
	public ReservationDto convertToDto(Reservation entity) {
		ReservationDto reservationDto = new ReservationDto();
		reservationDto.setStatus(entity.getStatus());
		reservationDto.setId(entity.getId());
		reservationDto.setLicensePlate(entity.getLicensePlate());
		reservationDto.setParkingSpotId(entity.getParkingSpotId());
		reservationDto.setDuration(generateHours(entity.getReservationDate().getHours(), entity.getDuration()));
		LocalDate localDate = entity.getReservationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		reservationDto.setStartTime(localDate.toString());
		return reservationDto;
	}
	
	private List<Integer> generateHours(Integer startTime, Integer duration) {
		List<Integer> hoursList = new ArrayList<>();
		for(int i = 0; i < duration; i++) {
			hoursList.add(startTime + i);
		}
		return hoursList;
	}
	
}
