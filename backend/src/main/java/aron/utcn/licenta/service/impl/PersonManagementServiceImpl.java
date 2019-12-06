package aron.utcn.licenta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.exception.UserNotFoundException;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.PersonHibernateRepository;
import aron.utcn.licenta.service.PersonManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {

	private final PersonHibernateRepository personHibernateRepository;
	
	@Override
	@Transactional
	public void save(Person person) {
		personHibernateRepository.save(person);
	}

	@Override
	@Transactional
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public void update(Person person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public Optional<Person> findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public Person findByUsername(String username) {
		return personHibernateRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

	@Override
	@Transactional
	public List<Person> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
