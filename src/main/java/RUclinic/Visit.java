package ruclinic;

/**
 * @author Paldeep Sekhon
 */
public class Visit {
    private Appointment appointment; // a reference to an appointment object
    private Visit next; // a reference to the next visit in the list

    // Constructor
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }

    // Getter for appointment
    public Appointment getAppointment() {
        return appointment;
    }

    // Getter for next
    public Visit getNext() {
        return next;
    }

    // Setter for next
    public void setNext(Visit next) {
        this.next = next;
    }

    // Override toString() method for displaying visit information (optional)
    @Override
    public String toString() {
        return appointment.toString();
    }
}
