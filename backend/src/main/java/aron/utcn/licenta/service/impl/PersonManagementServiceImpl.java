package aron.utcn.licenta.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.PersonToDtoConverter;
import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.exception.UserNotFoundException;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.PersonRepository;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {

	private final PersonRepository personHibernateRepository;
	
	private final PersonToDtoConverter personToDtoConverter;
	
	@Override
	@Transactional
	public void save(Person person) {
		personHibernateRepository.save(person);
	}

	@Override
	@Transactional
	public PersonDto findById(int id) {
		return personToDtoConverter.convertPersonToDto(personHibernateRepository.findById(id));
	}
	
	@Override
	@Transactional
	public Person findByUsername(String username) {
		return personHibernateRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

}
