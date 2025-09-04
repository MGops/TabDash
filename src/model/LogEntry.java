package src.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {
    public enum ChartType {
        FLUID("Fluid chart", "#ADD8E6"),
        FOOD("Food chart", "#E6ADD8"),
        STOOL("Stool chart", "#b69c79");

        public final String displayName;
        public final String colorHex;

        ChartType(String displayName, String colorHex) {
            this.displayName = displayName;
            this.colorHex = colorHex;
        }
    }

    public enum Action {
        STARTED("started"),
        STOPPED("stopped");

        public final String displayName;

        Action(String displayName) {
            this.displayName = displayName;
        }
    }

    private Date timestamp;
    private ChartType chartType;
    private Action action;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public LogEntry(Date timestamp, ChartType chartType, Action action) {
        this.timestamp = timestamp;
        this.chartType = chartType;
        this.action = action;
    }
    
    // Getters
    public Date getTimestamp() {
        return timestamp;
    }
    
    public ChartType getChartType() {
        return chartType;
    }
    
    public Action getAction() {
        return action;
    }
    
    // Setters
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }
    
    public void setAction(Action action) {
        this.action = action;
    }
    
    // Utility methods
    public String getFormattedTimestamp() {
        return dateFormat.format(timestamp);
    }
    
    public String getDisplayText() {
        return getFormattedTimestamp() + ": " + chartType.displayName + " " + action.displayName;
    }
    
    @Override
    public String toString() {
        return getDisplayText();
    }
    
    // For serialization/deserialization
    public String toFileString() {
        return timestamp.getTime() + "|" + chartType.name() + "|" + action.name();
    }
    
    public static LogEntry fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        if (parts.length >= 3) {
            Date timestamp = new Date(Long.parseLong(parts[0]));
            ChartType chartType = ChartType.valueOf(parts[1]);
            Action action = Action.valueOf(parts[2]);
            return new LogEntry(timestamp, chartType, action);
        }
        return null;
    }
    
}
