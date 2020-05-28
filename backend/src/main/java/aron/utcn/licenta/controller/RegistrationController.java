package aron.utcn.licenta.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.MessageDto;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

	private final PersonManagementService personManagementService;
	
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public MessageDto registerUser(@RequestBody Person person) {
		String password = person.getPassword();
		password = passwordEncoder.encode(password);
		person.setPassword(password);
		try {
			personManagementService.save(person);
		} catch (Exception e) {
			System.out.println("Username already exists");
			return new MessageDto("error");
		}
		return new MessageDto("ok");
	}
}
