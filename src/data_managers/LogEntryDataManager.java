package src.data_managers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import src.model.LogEntry;
import src.model.Patient;

public class LogEntryDataManager {
    private static final String LOG_DIR = "data/logs/";

    public static void savePatientLogEntries(Patient patient) {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Create filename from patient ID (replace spaces with underscores)
        String filename = LOG_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (LogEntry entry : patient.getLogEntries()) {
                writer.write(entry.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving log entries for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    public static void loadPatientLogEntries(Patient patient) {
        String filename = LOG_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            patient.getLogEntries().clear(); // Clear existing log entries

            while ((line = reader.readLine()) != null) {
                LogEntry entry = LogEntry.fromFileString(line.trim());
                if (entry != null) {
                    patient.addLogEntry(entry);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No log entries found for " + patient.getPatientId());
        } catch (IOException e) {
            System.err.println("Error loading log entries for " + patient.getPatientId() + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing log entry data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
}