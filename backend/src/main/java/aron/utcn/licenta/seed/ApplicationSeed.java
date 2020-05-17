package aron.utcn.licenta.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.ParkingPlaceService;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationSeed implements CommandLineRunner {

	private final PersonManagementService personManagementService;
	
	private final ParkingPlaceService parkingPlaceService;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person();
		person.setUsername("asd31");
		person.setName("John Doe");
		person.setEmail("asd31@gmail.com");
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
		
		for(int i=1;i<=24; i++) {
			ParkingPlace p = new ParkingPlace();
			parkingPlaceService.save(p);
		}
		
	}
	
	
}
