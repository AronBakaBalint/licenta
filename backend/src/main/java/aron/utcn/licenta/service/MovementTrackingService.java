package aron.utcn.licenta.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.LicensePlateReceiver;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class MovementTrackingService implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		Runnable runnable = new LicensePlateReceiver();
		Thread thread = new Thread(runnable);
		thread.start();
	}

}
