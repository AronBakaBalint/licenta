package aron.utcn.licenta.seed;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ReservationFacade;
import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.model.SimpleDate;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationSeed implements CommandLineRunner {

	private final PersonManagementService personManagementService;
	
	private final ParkingSpotManagementService parkingPlaceService;
	
	private final ReservationFacade reservationFacade;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void run(String... args) throws Exception {
		//startTimer();
		Person person = new Person();
		person.setUsername("asd31");
		person.setName("John Doe");
		person.setEmail("asd31@gmail.com");
		person.setBalance(14.0);
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
		
		for(int i=1;i<=24; i++) {
			ParkingSpot p = new ParkingSpot();
			parkingPlaceService.save(p);
		}
		
		createReservation("CJ25BBA", 1, 1, List.of(13, 14, 15), 0);
		createReservation("CJ97BBA", 1, 1, List.of(10, 11), 1);
		
	}
	
	private void createReservation(String licensePlate, Integer userId, Integer parkingSpotId, List<Integer> duration, Integer dayOffset) {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate(licensePlate);
		reservation.setUserId(userId);
		reservation.setParkingSpotId(parkingSpotId);
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		reservation.setStartTime(new SimpleDate(day+dayOffset, month+1, year));
		reservation.setDuration(duration);
		reservationFacade.reserveParkingPlace(reservation);
	}
	
}
