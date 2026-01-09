package com.vinaekal.sisehat.model.content;

public class BookingContent {
    private int id;
    private int user_id;
    private String hospital;
    private String department;
    private String doctor;
    private String queue_number;
    private String booking_date; // ISO string
    private String symptoms;
    private String status;
    private String created_at;

    // Getter
    public int getId() { return id; }
    public int getUser_id() { return user_id; }
    public String getHospital() { return hospital; }
    public String getDepartment() { return department; }
    public String getDoctor() { return doctor; }
    public String getQueue_number() { return queue_number; }
    public String getBooking_date() { return booking_date; }
    public String getSymptoms() { return symptoms; }
    public String getStatus() { return status; }
    public String getCreated_at() { return created_at; }
}
