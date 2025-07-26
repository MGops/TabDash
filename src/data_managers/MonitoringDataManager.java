package src.data_managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import src.model.MonitoringIssue;
import src.model.Patient;

public class MonitoringDataManager {
    private static final String MONITORING_DIR = "data/monitoring/";

    public static void savePatientMonitoring(Patient patient) {
        File dir = new File(MONITORING_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = MONITORING_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(MonitoringIssue issue : patient.getMonitoringIssues()) {
                writer.write(issue.getIssueType() + "|" + 
                           (issue.getNotes() != null ? issue.getNotes() : "") + "|" + 
                           issue.isActive());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving monitoring data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }


    public static void loadPatientMonitoring(Patient patient) {
        String filename = MONITORING_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            patient.getMonitoringIssues().clear(); // clear existing issues

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 3) {
                    String issueType = parts[0].trim();
                    String notes = parts[1].trim().isEmpty() ?null : parts[1].trim();
                    boolean active = Boolean.parseBoolean(parts[2].trim());

                    MonitoringIssue issue = new MonitoringIssue(issueType, notes);
                    issue.setActive(active);
                    patient.addMonitoringIssue(issue);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No monitoring data found for " + patient.getPatientId());
        } catch (IOException e) {
            System.err.println("Error loading monitoring data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
}
