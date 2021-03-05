package aron.utcn.licenta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.LoginDto;
import aron.utcn.licenta.exception.UserNotFoundException;
import aron.utcn.licenta.jwt.JwtResponse;
import aron.utcn.licenta.jwt.JwtTokenUtil;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

	private final JwtTokenUtil jwtTokenUtil;

	private final UserDetailsService userDetailsService;
	
	private final PersonManagementService personManagementService;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/login")
	public String createAuthenticationToken(@RequestBody LoginDto authenticationRequest) {

		Person person;
		try{
			person = personManagementService.findByUsername(authenticationRequest.getUsername());
		} catch(UserNotFoundException unfe) {
			return "false";
		}
		
		if(!passwordEncoder.matches(authenticationRequest.getPassword(), person.getPassword())) {
			return "false";
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new JwtResponse(token)).getBody().getToken();
	}

}
