package aron.utcn.licenta.seed;

import java.time.LocalDate;
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
		Person person = new Person();
		person.setUsername("asd31");
		person.setName("John Doe");
		person.setEmail("asd31@gmail.com");
		person.setBalance(14.0);
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);

		for (int i = 1; i <= 24; i++) {
			ParkingSpot p = new ParkingSpot();
			parkingPlaceService.save(p);
		}

		createReservation("CJ25BBA", 1, 1, List.of(13, 14, 15));
		createReservation("CJ97BBA", 1, 1, List.of(10, 11));

	}

	private void createReservation(String licensePlate, Integer userId, Integer parkingSpotId, List<Integer> duration) {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate(licensePlate);
		reservation.setUserId(userId);
		reservation.setParkingSpotId(parkingSpotId);
		reservation.setStartTime(LocalDate.now().toString());
		reservation.setDuration(duration);
		reservationFacade.reserveParkingPlace(reservation);
	}

}
