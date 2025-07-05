package src;

public class Medication {
    private String name;
    private Integer acbScore;
    private String drugClass;
    
    public Medication(String name) {
        this.name = name;
    }

    public String getName() {return name;}
    public Integer getAcbScore() {return acbScore;}
    public String getDrugClass() {return drugClass;}

    public void setName(String name) {this.name = name;}
    public void setAcbScore(Integer acbScore) {this.acbScore = acbScore;}
    public void setDrugClass(String drugClass) {this.drugClass = drugClass;}

    @Override
    public String toString() {
        return name + " (ACB: " + acbScore + ", Class: " + drugClass + ")";
    }
    
}
