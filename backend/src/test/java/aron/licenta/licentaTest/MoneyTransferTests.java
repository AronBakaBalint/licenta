package aron.licenta.licentaTest;

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
import aron.utcn.licenta.facade.PersonFacade;
import aron.utcn.licenta.service.PersonManagementService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan("aron.utcn.licenta")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = { ParkingApplication.class })
public class MoneyTransferTests {

	@Autowired
	private PersonManagementService personManagementService;
	
	@Autowired
	private PersonFacade personFacade;
	
	@Test
	public void testMoneyTransfer() {
		double amount = 5.0;
		double balanceBeforeTransaction = personFacade.findById(1).getBalance();
		personManagementService.addMoney(1, amount);
		double balanceAfterTransaction = personFacade.findById(1).getBalance();
		assert(balanceBeforeTransaction + amount == balanceAfterTransaction);
	}
}
