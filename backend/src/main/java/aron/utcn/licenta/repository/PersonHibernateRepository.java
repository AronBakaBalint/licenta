package aron.utcn.licenta.repository;

import java.util.List;
import java.util.Optional;

import aron.utcn.licenta.model.Person;

public interface PersonHibernateRepository {

	public void save(Person person);
	
	public void delete(int id);
	
	public void update(Person person);
	
	public Optional<Person> findById(int id);
	
	public Optional<Person> findByUsername(String username);
	
	public List<Person> getAll();
}
