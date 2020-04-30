package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.model.Person;

@Component
public class PersonToDtoConverter {

	public PersonDto convertPersonToDto(Person person) {
		PersonDto personDto = new PersonDto();
		personDto.setUsername(person.getUsername());
		personDto.setEmail(person.getEmail());
		personDto.setCurrentSold(person.getBalance());
		return personDto;
	}
}
