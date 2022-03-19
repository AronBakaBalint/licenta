package aron.licenta.licentaTest.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import aron.utcn.licenta.ParkingApplication;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.service.ReservationManagementService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan("aron.utcn.licenta")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = { ParkingApplication.class })
public class ReservationTests {
	
	@Autowired
	private ReservationManagementService reservationManagementService;
	
	@Test
	public void testFindAllReservations() {
		assertEquals(2, reservationManagementService.findAllReservationsOfUser(1).size());
	}
	
	@Test
	public void testCancelReservation() {
		List<Reservation> reservations = reservationManagementService.findReservationsByUser(1);
		reservationManagementService.cancelReservation(reservations.get(0).getId());
		Reservation reservation = reservationManagementService.findById(reservations.get(0).getId());
		assertEquals("cancelled", reservation.getStatus());
	}
	
	@Test
	public void testIsReservationPendingWhenTrue() {
		boolean isPending = reservationManagementService.isReservationPending(1);
		assert(isPending);
	}
	
	@Test
	public void testIsReservationPendingWhenFalse() {
		boolean isPending = reservationManagementService.isReservationPending(3);
		assertFalse(isPending);
	}
	
	@Test
	public void testGetReservationSchedule() {
		List<Reservation> reservations = reservationManagementService.getReservationSchedule(1, LocalDate.now());
		assertEquals(2, reservations.size());
	}
	
	@Test
	public void testUnconfirmedReservations() {
		List<Reservation> reservations = reservationManagementService.findUnconfirmedReservationsOfUser(1);
		assertEquals(2, reservations.size());
	}
	
}
