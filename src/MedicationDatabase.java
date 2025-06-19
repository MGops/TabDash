package src;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MedicationDatabase {
    private HashMap<String, Integer> acbScore;

    public MedicationDatabase() {
        acbScore = new HashMap<>();
        loadCSVdata();
    }

    private void loadCSVdata() {
        try (BufferedReader csvReader = new BufferedReader(new FileReader("data/acb_scores.csv"))) {
            String line;
            csvReader.readLine();
            while ((line = csvReader.readLine()) != null) {
                String[] parts = line.split(",");
                Integer score = Integer.parseInt(parts[1]);
                acbScore.put(parts[0], score);
            }
            System.out.println("Loaded " + acbScore.size() + " medications");
            System.out.println("Test lookup - aripiprazole: " + acbScore.get("aripiprazole"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getACBScore(String medName) {
        return acbScore.get(medName);
    }
}
