package aron.utcn.licenta.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.PersonConverter;
import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.exception.UserNotFoundException;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.PersonRepository;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {

	private final PersonRepository personRepository;
	
	@Override
	@Transactional
	public void save(Person person) {
		personRepository.save(person);
	}

	@Override
	@Transactional
	public Person findById(int id) {
		return personRepository.findById(id);
		//return personToDtoConverter.convertPersonToDto(personRepository.findById(id));
	}
	
	@Override
	@Transactional
	public Person findByUsername(String username) {
		return personRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

	@Override
	@Transactional
	public void addMoney(Integer userId, Double amount) {
		Person person = personRepository.findById(userId);
		person.addMoney(amount);
	}

	@Override
	public List<Person> getAll() {
		return personRepository.getAll();
	}

}
