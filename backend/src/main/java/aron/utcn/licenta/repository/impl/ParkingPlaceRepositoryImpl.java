package aron.utcn.licenta.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ParkingPlaceRepositoryImpl implements ParkingPlaceRepository {

	private final EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ParkingPlace> getAllParkingPlaces() {
		return entityManager.createQuery("SELECT p FROM ParkingPlace p left join fetch p.user u order by p.id").getResultList();
	}

	@Override
	public void save(ParkingPlace parkingPlace) {
		entityManager.persist(parkingPlace);
	}

	@Override
	public ParkingPlace findById(int id) {
		return (ParkingPlace) entityManager.createQuery("SELECT p FROM ParkingPlace p left join fetch p.user u WHERE p.id = :id")
				.setParameter("id", id)
				.getResultList().get(0);
	}

	@Override
	public void free(int parkingPlaceId) {
		ParkingPlace parkingPlace = entityManager.find(ParkingPlace.class, parkingPlaceId);
		parkingPlace.setFree();
		entityManager.persist(parkingPlace);
	}

}
