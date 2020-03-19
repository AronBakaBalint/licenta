package aron.utcn.licenta.dto;

import lombok.Data;

@Data
public class ReservationDto {

	private Integer parkingLotId;
	private String licensePlateNumber;
}

/*
"parkingLotId": 3,
"licensePlateNumber": "CJ25BBA",
"reservationStartTime": "07/12/2019 14:00:00",
"reservationDuration": 4
 */
