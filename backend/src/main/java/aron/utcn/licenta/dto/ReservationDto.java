package aron.utcn.licenta.dto;

import lombok.Data;

@Data
public class ReservationDto {

	private Integer id;
	private Integer parkingPlaceId;
	private Integer userId;
	private String licensePlate;
	private String status;
}
