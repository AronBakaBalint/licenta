package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.model.Person;

public interface PersonManagementService {
	
	public PersonDto findById(int id);
	
	public void save(Person person);
	
	public Person findByUsername(String username);
	
	public List<Person> getAll();
	
	public void addMoney(Integer userId, Double amount);
}
