package aron.utcn.licenta.facade;

import aron.utcn.licenta.dto.PersonDto;
import aron.utcn.licenta.model.Person;

public interface PersonFacade {

	public PersonDto findById(int id);
	
	public void save(Person person);
	
	public void addMoney(Integer userId, Double amount);
}
