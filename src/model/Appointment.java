package src.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    public enum Status {
        TO_REFER, REFERRED, SCHEDULED, ATTENDED, MISSED
    }

    private String specialty;
    private LocalDateTime dateTime;
    private String location;
    private String notes;
    private Status status;
    private String patientId;

    public Appointment(String specialty, LocalDateTime dateTime, String location) {
        this.specialty = specialty;
        this.dateTime = dateTime;
        this.location = location;
        this.status = Status.TO_REFER;
        this.notes = "";       
    }

    // Getters & Setters
    public String getSpecialty() {return specialty;}
    public void setSpecialty(String specialty) {this.specialty = specialty;}

    public LocalDateTime getDateTime() {return dateTime;}
    public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}

    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}

    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}

    public String getPatientId() {return patientId;}
    public void setPatientId(String patientId) {this.patientId = patientId;}

    public String getFormattedDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getDisplayText() {
        return "<html><b>" + specialty + "</b><br>" + getFormattedDateTime() + "<br>" + location + "</html>";
    }

    @Override
    public String toString() {
        return specialty + " - " + getFormattedDateTime();
    }
}
