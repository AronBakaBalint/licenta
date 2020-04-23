package aron.utcn.licenta.dto;

import lombok.Data;

@Data
public class ReservationDto {

	private Integer parkingPlaceId;
	private Integer userId;
	private String licensePlate;
}
