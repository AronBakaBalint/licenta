package aron.utcn.licenta.repository;

import java.util.List;
import java.util.Optional;

import aron.utcn.licenta.model.Person;

public interface PersonRepository {
	
	public Person findById(int id);
	
	public void save(Person person);
	
	public List<Person> getAll();
	
	public Optional<Person> findByUsername(String username);
}
