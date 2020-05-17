package aron.utcn.licenta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	private ParkingPlace parkingPlace;
	
	@OneToOne
	private Person user;
	private String status;
	private String licensePlate;
	
	public void cancel() {
		status = "canceled";
	}
	
	public void setOccupied() {
		status = "occupied";
	}
	
	public void setFinished() {
		status = "finished";
	}
	
	public boolean isFinished() {
		return status.equals("finished");
	}
	
	public boolean isCancelled() {
		return status.equals("canceled");
	}
	
	public boolean isExpired() {
		return isCancelled() || isFinished();
	}
}
