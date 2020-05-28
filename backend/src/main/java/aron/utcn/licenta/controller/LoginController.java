package aron.utcn.licenta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.JwtTokenDto;
import aron.utcn.licenta.dto.LoginDto;
import aron.utcn.licenta.exception.UserNotFoundException;
import aron.utcn.licenta.jwt.JwtResponse;
import aron.utcn.licenta.jwt.JwtTokenUtil;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;

@RestController
public class LoginController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PersonManagementService personManagementService;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/login")
	public JwtTokenDto createAuthenticationToken(@RequestBody LoginDto authenticationRequest) {

		Person person;
		try{
			person = personManagementService.findByUsername(authenticationRequest.getUsername());
		} catch(UserNotFoundException unfe) {
			return new JwtTokenDto("false");
		}
		
		if(!passwordEncoder.matches(authenticationRequest.getPassword(), person.getPassword())) {
			return new JwtTokenDto("false");
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return new JwtTokenDto(person.getId()+";"+ResponseEntity.ok(new JwtResponse(token)).getBody().getToken());
	}

}
