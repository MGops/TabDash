package src.service.StoppStartService;

public class StoppRecommendation {
    private String medicationName;
    private String condition;
    private String reason;

    public StoppRecommendation(String medicationName, String condition, String reason) {
        this.medicationName = medicationName;
        this.condition = condition;
        this.reason = reason;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getCondition() {
        return condition;
    }

    public String getReason() {
        return reason;
    }


    public String getTooltipText() {
        return "<html>" + condition + "<br>" + reason + "</html>";
    }

    @Override
    public String toString() {
        return medicationName + " (" + condition + ")";
    }
}