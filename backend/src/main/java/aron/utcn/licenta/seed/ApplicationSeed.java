package aron.utcn.licenta.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationSeed implements CommandLineRunner {

	private final PersonManagementService personManagementService;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person();
		person.setUsername("asd");
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
	}
	
	
}
