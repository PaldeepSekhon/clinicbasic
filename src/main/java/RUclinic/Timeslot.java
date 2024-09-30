package ruclinic;

/**
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */
public enum Timeslot {

    SLOT1(9, 0),
    SLOT2(10, 45),
    SLOT3(11, 15),
    SLOT4(13, 30),
    SLOT5(15, 0),
    SLOT6(16, 15);

    private final int hour;
    private final int minute;

    Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d %s",
                (hour == 12 || hour == 0) ? 12 : hour % 12, // No leading zero for hours
                minute, // Keep leading zero for minutes
                (hour >= 12) ? "PM" : "AM"); // AM/PM indicator
    }

    public static Timeslot fromString(String input) {
        try {
            int slotNumber = Integer.parseInt(input);
            if (slotNumber < 1 || slotNumber > Timeslot.values().length) {
                return null; // Invalid slot number
            }
            return Timeslot.values()[slotNumber - 1]; // Adjust for zero-based index
        } catch (NumberFormatException e) {
            return null; // Input was not a valid number
        }
    }
}
