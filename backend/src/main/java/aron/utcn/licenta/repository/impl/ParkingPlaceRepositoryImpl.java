package aron.utcn.licenta.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ParkingPlaceRepositoryImpl implements ParkingPlaceRepository {

	private final EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ParkingSpot> getAllParkingPlaces() {
		return entityManager.createQuery("SELECT p FROM ParkingSpot p left join fetch p.user u order by p.id").getResultList();
	}

	@Override
	public void save(ParkingSpot parkingPlace) {
		entityManager.persist(parkingPlace);
	}

	@Override
	public ParkingSpot findById(int id) {
		return (ParkingSpot) entityManager.createQuery("SELECT p FROM ParkingSpot p left join fetch p.user u WHERE p.id = :id")
				.setParameter("id", id)
				.getResultList().get(0);
	}

	@Override
	public void free(int parkingPlaceId) {
		ParkingSpot parkingPlace = entityManager.find(ParkingSpot.class, parkingPlaceId);
		parkingPlace.setFree();
		entityManager.persist(parkingPlace);
	}

}
