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
import aron.utcn.licenta.dto.ReservationDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.model.Person;
import aron.utcn.licenta.service.ParkingPlaceService;
import aron.utcn.licenta.service.PersonManagementService;
import aron.utcn.licenta.service.ReservationManagementService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan("aron.utcn.licenta")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = { ParkingApplication.class })
public class ReservationTests {
	
	@Autowired
	private PersonManagementService personManagementService;
	
	@Autowired
	private ReservationManagementService reservationService;
	
	@Autowired
	private ParkingPlaceService parkingPlaceService;
	
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
		reservation.setLicensePlate("test1");
		reservation.setParkingPlaceId(1);
		reservation.setUserId(1);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		String status = reservationService.findById(reservationId).getStatus();
		assertEquals("reserved", status);
	}
	
	@Test
	public void testReservationCancellation() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test2");
		reservation.setParkingPlaceId(2);
		reservation.setUserId(1);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		reservationService.cancelReservation(reservationId);
		String status = reservationService.findById(reservationId).getStatus();
		assertEquals("canceled", status);
		ParkingPlace parkingPlace = reservationService.findById(reservationId).getParkingPlace();
		assertEquals("free", parkingPlaceService.findById(parkingPlace.getId()).getStatus());
	}
	
	@Test
	public void testReservationExtensionForIdenticalPlates() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test3");
		reservation.setParkingPlaceId(3);
		reservation.setUserId(1);
		reservationService.reserveParkingPlace(reservation);
		
		ReservationDto newreservation = new ReservationDto();
		newreservation.setLicensePlate("test3");
		newreservation.setParkingPlaceId(3);
		newreservation.setUserId(1);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		assertEquals(-1, reservationId);
	}
	
	@Test
	public void testFindAllReservations() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test4");
		reservation.setParkingPlaceId(4);
		reservation.setUserId(1);
		reservationService.reserveParkingPlace(reservation);
		assertEquals(1, parkingPlaceService.findAllReservations(1).size());
	}
	
	@Test
	public void testGetAllParkingPlaces() {
		assertEquals(parkingPlaceService.getAllParkingPlaces().size(), 24);
	}
	
	@Test
	public void testArrivalAndDepartureNotEnoughMoney() {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test5");
		reservation.setParkingPlaceId(4);
		reservation.setUserId(1);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		String status = reservationService.findById(reservationId).getStatus();
		assertEquals("reserved", status);
		
		//when the car enters
		parkingPlaceService.handleScannedCode("1");
		status = reservationService.findById(reservationId).getStatus();
		assertEquals("occupied", status);
		
		//when the car leaves
		parkingPlaceService.handleScannedCode("1");
		status = reservationService.findById(reservationId).getStatus();
		
		//since the user does not have enough money,
		//the state should remain occupied
		assertEquals("occupied", status);
	}
	
	@Test
	public void testArrivalAndDepartureEnoughMoney() throws InterruptedException {
		ReservationDto reservation = new ReservationDto();
		reservation.setLicensePlate("test6s");
		reservation.setParkingPlaceId(4);
		reservation.setUserId(1);
		int reservationId = reservationService.reserveParkingPlace(reservation);
		String status = reservationService.findById(reservationId).getStatus();
		assertEquals("reserved", status);
		
		//add money for the user to be able to pay
		personManagementService.addMoney(1, 12.0);
		double balanceBeforePayment = personManagementService.findById(1).getCurrentSold();
		
		//when the car enters
		parkingPlaceService.handleScannedCode("1");
		status = reservationService.findById(reservationId).getStatus();
		assertEquals("occupied", status);
		
		// "simulate" the stay of the car in the parking lot
		Thread.sleep(5000);
		
		//when the car leaves
		parkingPlaceService.handleScannedCode("1");
		status = reservationService.findById(reservationId).getStatus();
		assertEquals("finished", status);
		
		double balanceAfterPayment = personManagementService.findById(1).getCurrentSold();
		assert(balanceAfterPayment < balanceBeforePayment);
	}

}
