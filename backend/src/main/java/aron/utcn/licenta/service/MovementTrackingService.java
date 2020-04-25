package aron.utcn.licenta.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.LicensePlateReceiver;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class MovementTrackingService implements CommandLineRunner {

	private final LicensePlateReceiver licensePlateReceiver;
	
	@Override
	public void run(String... args) throws Exception {
		Runnable runnable = licensePlateReceiver;
		Thread thread = new Thread(runnable);
		thread.start();
	}

}
