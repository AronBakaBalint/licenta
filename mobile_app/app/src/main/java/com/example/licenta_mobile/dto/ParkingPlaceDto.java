package com.example.licenta_mobile.dto;

public class ParkingPlaceDto {

    private Integer id;
    private String status;
    private String occupierCarPlate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOccupierCarPlate() {
        return occupierCarPlate;
    }

    public void setOccupierCarPlate(String occupierCarPlate) {
        this.occupierCarPlate = occupierCarPlate;
    }
}
