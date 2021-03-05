package aron.utcn.licenta.converter;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.model.Person;

@Component
public class PersonConverter implements BaseConverter<Person, PersonDto> {

	@Override
	public Person convertToEntity(PersonDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto convertToDto(Person entity) {
		PersonDto personDto = new PersonDto();
		personDto.setUsername(entity.getUsername());
		personDto.setEmail(entity.getEmail());
		personDto.setBalance(entity.getBalance());
		return personDto;
	}
}
