package com.example.licenta_mobile.dto;

import com.example.licenta_mobile.model.SimpleDate;

import java.util.Date;
import java.util.List;

public class ReservationDto {

    private int id;
    private Integer parkingPlaceId;
    private String licensePlate;
    private Integer userId;
    private String status;
    private SimpleDate startTime;
    private List<Integer> duration;

    public SimpleDate getStartTime() {
        return startTime;
    }

    public void setStartTime(SimpleDate startTime) {
        this.startTime = startTime;
    }

    public List<Integer> getDuration() {
        return duration;
    }

    public void setDuration(List<Integer> duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingPlaceId() {
        return parkingPlaceId;
    }

    public void setParkingPlaceId(Integer parkingPlaceId) {
        this.parkingPlaceId = parkingPlaceId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getReservationId() {
        return id;
    }

    public void setReservationId(int reservationId) {
        this.id = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
