package src.data_managers;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import src.model.Appointment;
import src.model.Patient;

public class AppointmentDataManager {
    private static final String APPOINTMENTS_DIR = "data/appointments/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void savePatientAppointments(Patient patient) {
        File dir = new File(APPOINTMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = APPOINTMENTS_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Appointment appointment : patient.getAppointments()) {
                StringBuilder line = new StringBuilder();
                line.append(appointment.getSpecialty()).append("|");
                line.append(appointment.getDateTime().format(DATE_FORMATTER)).append("|");
                line.append(appointment.getLocation() != null ? appointment.getLocation() : "").append("|");
                line.append(appointment.getStatus().toString()).append("|");

                writer.write(line.toString());
                writer.newLine();
            }
            
        } catch (Exception e) {
            System.err.println("Error saving appointments for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    public static void loadPatientAppointments(Patient patient) {
        String filename = APPOINTMENTS_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            patient.getAppointments().clear(); // Clear existing appointments

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 5) {
                    String specialty = parts[0].trim();
                    LocalDateTime dateTime = LocalDateTime.parse(parts[1].trim(), DATE_FORMATTER);
                    String location = parts[2].trim().isEmpty() ? null : parts[2].trim();
                    Appointment.Status status = Appointment.Status.valueOf(parts[3].trim());
                    
                    Appointment appointment = new Appointment(specialty, dateTime, location);
                    appointment.setStatus(status);
                    appointment.setPatientId(patient.getPatientId());

                    patient.addAppointment(appointment);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No appointment data found for " + patient.getPatientId());
        } catch (IOException e) {
            System.err.println("Error loading appointments for " + patient.getPatientId() + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing appointment data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
}
