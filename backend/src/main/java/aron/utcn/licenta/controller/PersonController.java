package aron.utcn.licenta.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.facade.PersonFacade;
import aron.utcn.licenta.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PersonController {

	private final PersonFacade personFacade;
	
	private final JwtTokenUtil jwtTokenUtil;
	
	@GetMapping("/users")
	public PersonDto getDetails(@RequestHeader("Authorization") String token) {
		Integer userId = Integer.parseInt(jwtTokenUtil.getIdFromToken(token.replace("Bearer ", "")));
		return personFacade.findById(userId);
	}
	
	@PostMapping("/users/addMoney")
    public void transferMoney(@RequestHeader("Authorization") String token, @RequestBody Double amount) {
		Integer userId = Integer.parseInt(jwtTokenUtil.getIdFromToken(token.replace("Bearer ", "")));
		personFacade.addMoney(userId, amount);
	}
}
