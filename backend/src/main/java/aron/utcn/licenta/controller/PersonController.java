package aron.utcn.licenta.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.dto.MoneyTransferDto;
import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PersonController {

	private final PersonManagementService personManagementService;
	
	@GetMapping("/users/{id}")
	public PersonDto getDetails(@PathVariable Integer id) {
		return personManagementService.findById(id);
	}
	
	@PostMapping("/users/addMoney")
    public void transferMoney(@RequestBody MoneyTransferDto moneyTransferDto) {
		personManagementService.addMoney(moneyTransferDto.getUserId(), moneyTransferDto.getAmount());
	}
}
