package aron.utcn.licenta.dto;

import lombok.Data;

@Data
public class UnconfirmedReservationDto {

	private Integer reservationId;
	private int parkingPlaceId;
	private String licensePlate;
	private String status;
}
