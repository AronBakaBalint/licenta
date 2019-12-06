package aron.utcn.licenta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ParkingPlace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private boolean occupied;
	private String occupierCarPlate;
	
	public void setOccupiedBy(String carPlate) {
		occupied = true;
		occupierCarPlate = carPlate;
	}
	
	public void setFree() {
		occupied = false;
		occupierCarPlate=null;
	}
}
