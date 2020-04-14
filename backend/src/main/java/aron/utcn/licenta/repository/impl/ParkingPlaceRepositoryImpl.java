package aron.utcn.licenta.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingPlaceRepositoryImpl implements ParkingPlaceRepository {

	private final EntityManager entityManager;
	
	@Override
	public List<ParkingPlace> getAllParkingPlaces() {
		return entityManager.createQuery("SELECT p FROM ParkingPlace p").getResultList();
	}

	@Override
	public void save(ParkingPlace parkingPlace) {
		entityManager.persist(parkingPlace);
	}

	@Override
	public void makeReservation(int parkingPlaceId, String licensePlate) {
		ParkingPlace parkingPlace = entityManager.find(ParkingPlace.class, parkingPlaceId);
		parkingPlace.setReserved(licensePlate);
	}

	@Override
	public ParkingPlace findById(int id) {
		return entityManager.find(ParkingPlace.class, id);
	}

}
