package aron.utcn.licenta.dto;

import lombok.Data;

@Data
public class ParkingSpotDto {
	
	private Integer id;
	private String status;
	private Integer color;
	private String occupierCarPlate;
}
