package aron.licenta.licentaTest;

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
public class AuthTests {
	
	@Autowired
	private PersonManagementService personManagementService;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test
	public void testCorrectLogin() {
		String username = "asd31";
		String password = "123";
		Person person = personManagementService.findByUsername(username);
		assert(passwordEncoder.matches(password, person.getPassword()));
	}
	
	@Test
	public void testIncorrectLogin() {
		String username = "asd31";
		String password = "1234";
		Person person = personManagementService.findByUsername(username);
		assert(!passwordEncoder.matches(password, person.getPassword()));
	}
	
}
