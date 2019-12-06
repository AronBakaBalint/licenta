package aron.utcn.licenta.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.Reservation;

@Component
public class DtoToReservationConverter {

	public Reservation convertDtoToReservation(ReservationDto reservationDto) throws ParseException {
		Reservation reservation = new Reservation();
		reservation.setLicensePlateNumber(reservationDto.getLicensePlateNumber());
		reservation.setParkingLotId(reservationDto.getParkingLotId());
		reservation.setArrived(false);
		
		Date reservationStart = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(reservationDto.getReservationStartTime());  
		reservation.setReservationStartTime(reservationStart);
		
		Date reservationEnd = addHoursToJavaUtilDate(reservationStart, Integer.parseInt(reservationDto.getReservationDuration()));
		reservation.setReservationEndTime(reservationEnd);
		
		return reservation;
	}
	
	private static Date addHoursToJavaUtilDate(Date date, int hours) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hours);
	    return calendar.getTime();
	}
	
}
