package aron.utcn.licenta.service;

import java.util.List;
import java.util.Optional;

import aron.utcn.licenta.model.Person;

public interface PersonManagementService {

	public void save(Person person);
	
	public void delete(int id);
	
	public void update(Person person);
	
	public Optional<Person> findById(int id);
	
	public Person findByUsername(String username);
	
	public List<Person> getAll();
}
