package aron.travel.Travel;

import javax.persistence.PersistenceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import aron.utcn.licenta.ParkingApplication;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan("aron.utcn.licenta")
@ContextConfiguration(classes = { ParkingApplication.class })
public class ReservationTests {
	
	@Autowired
	private PersonManagementService personManagementService;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test(expected = PersistenceException.class)
	public void testRegistration() {
		Person person = new Person();
		person.setUsername("asd31");
		person.setName("John Doe");
		person.setEmail("asd31@gmail.com");
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
	}

}
