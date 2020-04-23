package aron.utcn.licenta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ParkingPlace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer userId;
	private String status = "free";
	private String occupierCarPlate;
	
	public void setOccupied() {
		status = "occupied";
	}
	
	public void setReserved(String licensePlate, Integer userId) {
		occupierCarPlate = licensePlate;
		status = "reserved";
		this.userId = userId;
	}
	
	public void setFree() {
		status = "free";
		occupierCarPlate = null;
		userId = null;
	}
}
