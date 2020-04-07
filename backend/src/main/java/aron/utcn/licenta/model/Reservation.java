package aron.utcn.licenta.model;

import java.util.Date;

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
	private Date reservationStartTime;
	private String licensePlateNumber;
	private Boolean arrived;
}
