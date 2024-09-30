package ruclinic;

/**
 * Class representing a visit to the clinic.
 * Each visit is associated with an appointment and can link to the next visit, 
 * forming a linked list of visits.
 * 
 * @author Paldeep Sekhon
 */
public class Visit {
    private Appointment appointment; // a reference to an appointment object
    private Visit next; // a reference to the next visit in the list

     /**
     * Constructor for the Visit class.
     * Initializes the visit with an appointment and sets the next visit to null.
     * 
     * @param appointment The appointment associated with the visit.
     */
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }

    /**
     * Getter for the appointment.
     * 
     * @return The appointment associated with this visit.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Getter for the next visit in the list.
     * 
     * @return The next visit linked to this one.
     */
    public Visit getNext() {
        return next;
    }

   /**
     * Setter for the next visit in the list.
     * 
     * @param next The next visit to link to this one.
     */
    public void setNext(Visit next) {
        this.next = next;
    }

     /**
     * Overrides the toString() method to return the string representation 
     * of the appointment associated with this visit.
     * 
     * @return A string representing the visit (using the appointment's toString).
     */
    @Override
    public String toString() {
        return appointment.toString();
    }
}
