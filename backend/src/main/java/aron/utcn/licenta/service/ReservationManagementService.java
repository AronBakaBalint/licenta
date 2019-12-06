package aron.utcn.licenta.service;

import java.text.ParseException;

import aron.utcn.licenta.dto.ReservationDto;

public interface ReservationManagementService {

	public void reserveParkingLot(ReservationDto reservation) throws ParseException;
}
