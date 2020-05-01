package aron.utcn.licenta.repository.impl;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

	private final EntityManager entityManager;

	@Override
	public Person findById(int id) {
		return entityManager.find(Person.class, id);
	}


	@Override
	public Optional<Person> findByUsername(String username) {
		try{
			return Optional.ofNullable((Person)entityManager.createQuery(
				"SELECT person FROM Person person WHERE person.username LIKE :username")
				.setParameter("username", username)
				.getResultList().get(0));
		} catch(IndexOutOfBoundsException e) {
			throw new UsernameNotFoundException("User not found!");
		}
		
	}


	@Override
	public void save(Person person) {
		entityManager.persist(person);
	}
}
