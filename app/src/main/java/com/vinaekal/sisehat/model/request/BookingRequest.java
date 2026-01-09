package com.vinaekal.sisehat.model.request;

public class BookingRequest {
    private int user_id;
    private String hospital;
    private String department;
    private String doctor;
    private String queue_number;
    private String booking_date;
    private String symptoms;
    private String status;

    public BookingRequest(int user_id, String hospital, String department, String doctor, String queue_number, String booking_date, String symptoms, String status) {
        this.user_id = user_id;
        this.hospital = hospital;
        this.department = department;
        this.doctor = doctor;
        this.queue_number = queue_number;
        this.booking_date = booking_date;
        this.symptoms = symptoms;
        this.status = status;
    }
}
