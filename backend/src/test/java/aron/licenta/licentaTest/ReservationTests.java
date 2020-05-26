package aron.licenta.licentaTest;

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
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.service.PersonManagementService;
import aron.utcn.licenta.service.ReservationManagementService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan("aron.utcn.licenta")
@ContextConfiguration(classes = { ParkingApplication.class })
public class ReservationTests {
	
	@Autowired
	private PersonManagementService personManagementService;
	
	@Autowired
	private ReservationManagementService reservationService;
	
	@Autowired
	private ParkingPlaceRepository parkingPlaceRepository;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test(expected = PersistenceException.class)
	public void testRegistrationWithExistingUsername() {
		Person person = new Person();
		person.setUsername("asd31");
		personManagementService.save(person);
	}
	
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
	
	@Test
	public void testRegistrationWithNewUsername() {
		Integer nrOfUsersBeforeInsert = personManagementService.getAll().size();
		Person person = new Person();
		person.setUsername("anythingElse");
		person.setName("John Doe");
		person.setEmail("jdoe@gmail.com");
		person.setPassword(passwordEncoder.encode("123"));
		personManagementService.save(person);
		Integer nrOfUsersAfterInsert = personManagementService.getAll().size();
		assert(nrOfUsersBeforeInsert + 1 == nrOfUsersAfterInsert);
	}
	
	@Test
	public void testMoneyTransfer() {
		double amount = 5.0;
		double balanceBeforeTransaction = personManagementService.findById(1).getCurrentSold();
		personManagementService.addMoney(1, amount);
		double balanceAfterTransaction = personManagementService.findById(1).getCurrentSold();
		assert(balanceBeforeTransaction + amount == balanceAfterTransaction);
	}
	
	@Test
	public void testReservation() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test12");
		reservation.setParkingPlaceId(1);
		reservation.setUserId(2);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		String status = reservationService.findById(reservationId).getStatus();
		assert("reserved".equals(status));
	}
	
	@Test
	public void testReservationCancellation() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test123");
		reservation.setParkingPlaceId(1);
		reservation.setUserId(2);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		reservationService.cancelReservation(reservationId);
		String status = reservationService.findById(reservationId).getStatus();
		assert("canceled".equals(status));
		ParkingPlace parkingPlace = reservationService.findById(reservationId).getParkingPlace();
		assert(parkingPlaceRepository.findById(parkingPlace.getId()).getStatus().equals("free"));
	}
	
	@Test
	public void testReservationExtensionForIdenticalPlates() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test123");
		reservation.setParkingPlaceId(1);
		reservation.setUserId(2);
		reservationService.reserveParkingPlace(reservation);
		
		ReservationDto newreservation = new ReservationDto();
		newreservation.setLicensePlate("test123");
		newreservation.setParkingPlaceId(2);
		newreservation.setUserId(3);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		assert(reservationId == -1);
		
	}

}
