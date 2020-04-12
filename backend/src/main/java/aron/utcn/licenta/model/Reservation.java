package aron.utcn.licenta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reservationId;
	private Integer parkingPlaceId;
	private String licensePlateNumber;
	private String status;
}
