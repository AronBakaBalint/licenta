package aron.utcn.licenta.seed;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.facade.ReservationFacade;
import aron.utcn.licenta.model.ParkingPlace;
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
		person.setBalance(50.0);
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
		
		for(int i=1;i<=24; i++) {
			ParkingPlace p = new ParkingPlace();
			parkingPlaceService.save(p);
		}
		
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("CJ25BBA");
		reservation.setUserId(1);
		reservation.setParkingPlaceId(1);
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		reservation.setStartTime(new SimpleDate(day, month+1, year));
		reservation.setDuration(List.of(13, 14, 15));
		reservationFacade.reserveParkingPlace(reservation);
		
		reservation = new ReservationDto();
		reservation.setLicensePlate("CJ97BBA");
		reservation.setUserId(1);
		reservation.setParkingPlaceId(1);
		cal.setTime(date);
		reservation.setStartTime(new SimpleDate(day+1, month+1, year));
		reservation.setDuration(List.of(10, 11));
		reservationFacade.reserveParkingPlace(reservation);
	}
	
	private void startTimer() {
		Timer timer = new Timer ();
		TimerTask hourlyTask = new TimerTask () {
		    @Override
		    public void run () {
		    	parkingPlaceService.clearUnoccupiedPlaces();
		    }
		};

		// schedule the task to run starting now and then every hour...
		timer.schedule (hourlyTask, 0l, 1000*60/**60*/);
	}
	
}
