package RUclinic;


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
        return String.format("%02d:%02d %s", 
            (hour == 12 || hour == 0) ? 12 : hour % 12, 
            minute, 
            (hour >= 12) ? "PM" : "AM");
    }

    public static Timeslot fromString(String input) {
        try {
            int slotNumber = Integer.parseInt(input);
            if (slotNumber < 1 || slotNumber > Timeslot.values().length) {
                System.out.println("Not Valid");
                return null;  // Invalid slot number
            }
            return Timeslot.values()[slotNumber - 1];  // Adjust for zero-based index
        } catch (NumberFormatException e) {
            System.out.println("Not Valid");
            return null;  // Input was not a valid number
        }
    }
}
