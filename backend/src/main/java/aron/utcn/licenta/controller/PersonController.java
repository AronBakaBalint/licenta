package aron.utcn.licenta.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.PersonDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PersonController {

	@GetMapping("/details/{id}")
	public PersonDto getDetails(@RequestParam int id) {
		return null;
	}
}
