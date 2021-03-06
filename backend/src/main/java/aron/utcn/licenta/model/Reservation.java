package aron.utcn.licenta.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
	private ParkingSpot parkingSpot;
	
	@OneToOne
	private Person user;
	private String status;
	private String licensePlate;
	private Date reservationMakingDate;
	private Date reservationDate;
	private Integer duration;
	
	public void cancel() {
		status = "cancelled";
	}
	
	public void setOccupied() {
		status = "occupied";
	}
	
	public void setFinished() {
		status = "finished";
	}
	
	public void setReserved() {
		status = "reserved";
	}
	
	public boolean isReserved() {
		return status.equals("reserved");
	}
	
	public boolean isFinished() {
		return status.equals("finished");
	}
	
	public boolean isCancelled() {
		return status.equals("cancelled");
	}
	
	public boolean isOccupied() {
		return status.equals("occupied");
	}
	
	public boolean isExpired() {
		return isCancelled() || isFinished();
	}
	
	public Integer getParkingSpotId() {
		return parkingSpot.getId();
	}
	
	public boolean hasDate(SimpleDate date) {
		LocalDate localDate = reservationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if(date.getDay() != localDate.getDayOfMonth()) {
			return false;
		} else if(date.getMonth() != localDate.getMonthValue()) {
			return false;
		}
		return date.getYear() == localDate.getYear();
	}
}
