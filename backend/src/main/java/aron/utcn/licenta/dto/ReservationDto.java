package aron.utcn.licenta.dto;

import java.util.List;

import aron.utcn.licenta.model.SimpleDate;
import lombok.Data;

@Data
public class ReservationDto {

	private Integer id;
	private Integer parkingPlaceId;
	private Integer userId;
	private String licensePlate;
	private String status;
	private SimpleDate startTime;
	private List<Integer> duration;
}
