package aron.utcn.licenta.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.facade.PersonFacade;
import aron.utcn.licenta.model.Person;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

	private final PersonFacade personFacade;
	
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public Integer registerUser(@RequestBody Person person) {
		String password = person.getPassword();
		password = passwordEncoder.encode(password);
		person.setPassword(password);
		try {
			personFacade.save(person);
		} catch (Exception e) {
			System.out.println("Username already exists");
			return 1;
		}
		return 0;
	}
}
