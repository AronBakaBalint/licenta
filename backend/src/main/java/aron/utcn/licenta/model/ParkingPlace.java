package aron.utcn.licenta.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ParkingPlace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	private Person user;
	private String status = "free";
	private String occupierCarPlate;
	private Date arrivalTime;
	
	public void setOccupied() {
		status = "occupied";
	}
	
	public void setReserved(String licensePlate, Person user) {
		occupierCarPlate = licensePlate;
		status = "reserved";
		this.user = user;
	}
	
	public void setFree() {
		status = "free";
		occupierCarPlate = null;
		user = null;
	}
	
	public boolean isOccupied() {
		return status.equals("occupied");
	}
	
}
