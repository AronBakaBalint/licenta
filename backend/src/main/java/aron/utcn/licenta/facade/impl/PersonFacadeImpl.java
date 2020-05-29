package aron.utcn.licenta.facade.impl;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.converter.BaseConverter;
import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.facade.PersonFacade;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonFacadeImpl implements PersonFacade {
	
	private final PersonManagementService personService;

	private final BaseConverter<Person, PersonDto> personConverter;
	
	public PersonDto findById(int id) {
		return personConverter.convertToDto(personService.findById(id));
	}

	@Override
	public void save(Person person) {
		personService.save(person);
	}

	@Override
	public void addMoney(Integer userId, Double amount) {
		personService.addMoney(userId, amount);
	}

}
