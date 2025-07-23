package src.data_managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import src.model.Patient;

public class MHADataManager {
    private static final String MHA_DIR = "data/mha/";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void savePatientMHA(Patient patient) {
        File dir = new File(MHA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Create filename from patient ID (replace spaces with underscores)
        String filename = MHA_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write each MHA field as key-value pairs
            writer.write("mh03_completed=" + patient.isMh03Completed());
            writer.newLine();
            writer.write("section_status=" + patient.getSectionStatus()); 
            writer.newLine();
            //Handle dates (might be null)
            writer.write("admission_date=" + formatDate(patient.getAdmissionDate()));
            writer.newLine();
            writer.write("detention_date=" + formatDate(patient.getDetentionDate()));
            writer.newLine();
            writer.write("original_detention_date=" + formatDate(patient.getOriginalDetentionDate()));
            writer.newLine();
            
            writer.write("capacity=" + patient.getCapacity());
            writer.newLine();
            writer.write("soad_requested=" + patient.isSoadRequested());
            writer.newLine();
            writer.write("soad_date=" + formatDate(patient.getSoadDate()));
            writer.newLine();
            writer.write("soad_reference=" + (patient.getSoadReference() != null ? patient.getSoadReference() : ""));
            writer.newLine();
            writer.write("s62_completed=" + patient.isS62Completed());
            writer.newLine();
            writer.write("s62_date=" + formatDate(patient.getS62Date()));
            writer.newLine();
            writer.write("t3_provided=" + patient.isT3Provided());
            writer.newLine();
            writer.write("t3_date=" + formatDate(patient.getT3Date()));
            writer.newLine();
            writer.write("t3_review_date=" + formatDate(patient.getT3ReviewDate()));
            writer.newLine();
            writer.write("t2_completed=" + patient.isT2Completed());
            writer.newLine();
            writer.write("t2_date=" + formatDate(patient.getT2Date()));
            writer.newLine();
            writer.write("t2_review_date=" + formatDate(patient.getT2ReviewDate()));
            writer.newLine();
            writer.write("tribunal_date=" + formatDate(patient.getTribunalDate()));
            writer.newLine();
            writer.write("tribunal_type=" + (patient.getTribunalType() != null ? patient.getTribunalType() : ""));
            writer.newLine();
            writer.write("report_due_date=" + formatDate(patient.getReportDueDate()));
            writer.newLine();
            writer.write("emergency_medical_leave=" + patient.isEmergencyMedicalLeave());
            writer.newLine();
            writer.write("other_leave=" + (patient.getOtherLeave() != null ? patient.getOtherLeave().replace("\n", "\\n") : ""));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving MHA data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    private static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "null";
    }

    public static void loadPatientMHAdata(Patient patient) {
        String filename = MHA_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    switch (key) {
                        case "mh03_completed":
                            patient.setMh03Completed(Boolean.parseBoolean(value));
                            break;
                        case "section_status":
                            patient.setSectionStatus(value);
                            break;
                        case "admission_date":
                            patient.setAdmissionDate(parseDate(value)); 
                            break;
                        case "detention_date":
                            patient.setDetentionDate(parseDate(value));
                            break;
                        case "original_detention_date":
                            patient.setOriginalDetentionDate(parseDate(value));
                            break;
                        case "capacity":
                            patient.setCapacity(value);
                            break;
                        case "soad_requested":
                            patient.setSoadRequested(Boolean.parseBoolean(value));
                            break;
                        case "soad_date":
                            patient.setSoadDate(parseDate(value));
                            break;
                        case "soad_reference":
                            patient.setSoadReference(value.isEmpty() ? null : value);
                            break;
                        case "s62_completed":
                            patient.sets62Completed(Boolean.parseBoolean(value));
                            break;
                        case "s62_date":
                            patient.setS62Date(parseDate(value));
                            break;
                        case "t3_provided":
                            patient.setT3Provided(Boolean.parseBoolean(value));
                            break;
                        case "t3_date":
                            patient.setT3Date(parseDate(value));
                            break;
                        case "t3_review_date":
                            patient.setT3ReviewDate(parseDate(value));
                            break;
                        case "t2_completed":
                            patient.setT2Completed(Boolean.parseBoolean(value));
                            break;
                        case "t2_date":
                            patient.setT2Date(parseDate(value));
                            break;
                        case "t2_review_date":
                            patient.setT2ReviewDate(parseDate(value));
                            break;
                        case "tribunal_date":
                            patient.setTribunalDate(parseDate(value));
                            break;
                        case "tribunal_type":
                            patient.setTribunalType(value.isEmpty() ? null : value);
                            break;
                        case "report_due_date":
                            patient.setReportDueDate(parseDate(value));
                            break;
                        case "emergency_medical_leave":
                            patient.setEmergencyMedicalLeave(Boolean.parseBoolean(value));
                            break;
                        case "other_leave":
                            patient.setOtherLeave(value.replace("\\n", "\n"));
                            break;
                        default:
                            System.out.println("Unknown MHA field: " + key);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // No file exists yet- eg. for new patients
            System.out.println("No MHA data found for " + patient.getPatientId()); 
        } catch (IOException e) {
            System.err.println("Error loading MHA data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    private static Date parseDate (String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }
}
