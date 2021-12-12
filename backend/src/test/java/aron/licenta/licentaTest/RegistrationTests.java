package aron.licenta.licentaTest;

import static org.junit.Assert.assertEquals;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import aron.utcn.licenta.ParkingApplication;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.PersonManagementService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan("aron.utcn.licenta")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = { ParkingApplication.class })
public class RegistrationTests {

	@Autowired
	private PersonManagementService personManagementService;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test(expected = PersistenceException.class)
	public void testRegistrationWithExistingUsername() {
		Person person = new Person();
		person.setUsername("asd31");
		personManagementService.save(person);
	}
	
	@Test
	public void testRegistrationWithNewUsername() {
		int nrOfUsersBeforeInsert = personManagementService.getAll().size();
		Person person = new Person();
		person.setUsername("anythingElse");
		person.setName("John Doe");
		person.setEmail("jdoe@gmail.com");
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
		int nrOfUsersAfterInsert = personManagementService.getAll().size();
		assertEquals(nrOfUsersBeforeInsert + 1, nrOfUsersAfterInsert);
	}
}
