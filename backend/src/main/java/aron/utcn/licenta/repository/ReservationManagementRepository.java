package aron.utcn.licenta.repository;

import aron.utcn.licenta.model.Reservation;

public interface ReservationManagementRepository {

	public void reserveParkingLot(Reservation reservation);
}
