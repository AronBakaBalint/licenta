package aron.utcn.licenta.service;

import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.model.Person;

public interface PersonManagementService {
	
	public PersonDto findById(int id);
	
	public void save(Person person);
	
	public Person findByUsername(String username);
}
