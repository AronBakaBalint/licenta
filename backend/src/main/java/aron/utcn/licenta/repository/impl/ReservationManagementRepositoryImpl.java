package aron.utcn.licenta.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ReservationManagementRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationManagementRepositoryImpl implements ReservationManagementRepository {

	private final EntityManager entityManager;
	
	@Override
	public void reserveParkingLot(Reservation reservation) {
		entityManager.persist(reservation);		
	}

}
