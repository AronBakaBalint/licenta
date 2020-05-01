package aron.utcn.licenta.repository;

import java.util.Optional;

import aron.utcn.licenta.model.Person;

public interface PersonRepository {
	
	public Person findById(int id);
	
	public void save(Person person);
	
	public Optional<Person> findByUsername(String username);
}
