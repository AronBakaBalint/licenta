package aron.utcn.licenta.service;

import java.text.ParseException;

import aron.utcn.licenta.dto.ReservationDto;

public interface ReservationManagementService {

	public void reserveParkingPlace(ReservationDto reservation) throws ParseException;
	
}
