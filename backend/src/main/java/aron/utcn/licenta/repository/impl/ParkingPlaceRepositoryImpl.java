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
	public void makeReservation(int parkingPlaceId, String licensePlate, int userId) {
		ParkingPlace parkingPlace = entityManager.find(ParkingPlace.class, parkingPlaceId);
		parkingPlace.setReserved(licensePlate.toLowerCase(), userId);
	}

	@Override
	public ParkingPlace findById(int id) {
		return entityManager.find(ParkingPlace.class, id);
	}

	@Override
	public void free(int parkingPlaceId) {
		ParkingPlace parkingPlace = entityManager.find(ParkingPlace.class, parkingPlaceId);
		parkingPlace.setFree();
		entityManager.persist(parkingPlace);
	}

	@Override
	public List<ParkingPlace> findReservationsByUser(int userId) {
		return (List<ParkingPlace>)entityManager.createQuery(
				"SELECT parkingPlace FROM ParkingPlace parkingPlace WHERE parkingPlace.userId LIKE :userId")
				.setParameter("userId", userId)
				.getResultList();
	}

	@Override
	public void setArrived(String licensePlate) {
		ParkingPlace parkingPlace = (ParkingPlace)entityManager.createQuery(
				"SELECT parkingPlace FROM ParkingPlace parkingPlace WHERE occupierCarPlate LIKE :licensePlate")
				.setParameter("licensePlate", licensePlate)
				.getResultList().get(0);
		parkingPlace.setOccupied();	
	}

}
