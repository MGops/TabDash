package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MHADataManager {
    private static final String MHA_DIR = "data/mha";
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
            writer.write("admission_date=" + patient.getAdmissionDate());
            writer.newLine();
            writer.write("detention_date=" + formatDate(patient.getAdmissionDate()));
            writer.newLine();
            writer.write("original_detention_date=" + formatDate(patient.getOriginalDetentionDate()));
            writer.newLine();
            
            writer.write("capacity=" + patient.getCapacity());
            writer.newLine();
            writer.write("soad_requested=" + patient.isSoadRequested());
            writer.newLine();
            writer.write("soad_date=" + formatDate(patient.getSoadDate()));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving MHA data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    private static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "null";
    }
}
