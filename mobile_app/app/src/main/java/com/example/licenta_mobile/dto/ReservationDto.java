package com.example.licenta_mobile.dto;

public class ReservationDto {

    private int id;
    private Integer parkingPlaceId;
    private String licensePlate;
    private Integer userId;
    private String status;

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
