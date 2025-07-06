package src;

public class Medication {
    private String name;
    private Integer acbScore;
    private String drugClass;
    private String drugSubclass;
    
    public Medication(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {return name;}
    public Integer getAcbScore() {return acbScore;}
    public String getDrugClass() {return drugClass;}
    public String getDrugSubclass() {return drugSubclass;}

    // Setters
    public void setName(String name) {this.name = name;}
    public void setAcbScore(Integer acbScore) {this.acbScore = acbScore;}
    public void setDrugClass(String drugClass) {this.drugClass = drugClass;}
    public void setDrugSubclass(String drugSubclass) {this.drugSubclass = drugSubclass;}

    @Override
    public String toString() {
        return name + " (ACB: " + acbScore + ", Class: " + drugClass + ")";
    }
    
}
