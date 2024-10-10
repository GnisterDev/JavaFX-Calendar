package calendar.ui;

public class TimeSlot {
    private String displayText; // What the user sees
    private Integer startTime; // Underlying start time value

    public TimeSlot(String displayText, Integer startTime) {
        this.displayText = displayText;
        this.startTime = startTime;
    }

    // Override toString to display the text in ChoiceBox
    @Override
    public String toString() {
        return displayText;
    }

    // Getters
    public Integer getStartTime() {
        return startTime;
    }

}
