package aron.utcn.licenta.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.PersonHibernateRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonHibernateRepositoryImpl implements PersonHibernateRepository {

	private final EntityManager entityManager;

	@Override
	public void save(Person person) {
		entityManager.persist(person);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Person person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Person> findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getAll() {
		// TODO Auto-generated method stub
		return null;
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
}
