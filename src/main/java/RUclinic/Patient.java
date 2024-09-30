package ruclinic;

/**
 * * The Patient class represents a patient in the clinic, containing a profile
 * and a linked list of completed visits.
 * 
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */
public class Patient implements Comparable<Patient> {
    private Profile profile;
    private Visit visits; // a linked list of visits (completed appointments)

    /**
     * Constructor to initialize a Patient object with a profile.
     * 
     * @param profile The profile of the patient.
     */
    public Patient(Profile profile) {
        this.profile = profile;
        this.visits = null;
    }

    /**
     * Constructor to initialize a Patient object with a profile.
     * 
     * @param profile The profile of the patient.
     */
    public int charge() {
        int totalCharge = 0;
        Visit currentVisit = visits;

        while (currentVisit != null) {
            totalCharge += currentVisit.getAppointment().getProvider().getSpecialty().getCharge();
            currentVisit = currentVisit.getNext();
        }

        return totalCharge;
    }

    /**
     * Checks if two Patient objects are equal based on their profile.
     * 
     * @param obj The object to compare against.
     * @return true if the profiles are the same, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Patient patient = (Patient) obj;
        return profile.equals(patient.profile);
    }

    /**
     * Compares this patient with another patient by their profile.
     * 
     * @param other The other patient to compare against.
     * @return A negative integer, zero, or a positive integer as this patient is
     *         less than, equal to, or greater than the other.
     */
    @Override
    public int compareTo(Patient other) {
        return this.profile.compareTo(other.profile);
    }

    /**
     * Returns a string representation of the patient's profile.
     * 
     * @return A string containing the patient's profile.
     */

    @Override
    public String toString() {
        return profile.toString();
    }

    /**
     * Adds a visit to the linked list of visits. If the visit already exists, it
     * will not be added.
     * 
     * @param visit The visit to be added to the list.
     */

    public void addVisit(Visit visit) {
        if (visits == null) {
            visits = visit;
        } else {
            Visit current = visits;
            while (current.getNext() != null) {
                // Check for duplicate appointment before adding
                if (current.getAppointment().equals(visit.getAppointment())) {
                    return; // Duplicate visit found, do not add
                }
                current = current.getNext();
            }
            // Ensure to check the last visit before adding the new one
            if (!current.getAppointment().equals(visit.getAppointment())) {
                current.setNext(visit);
            }
        }
    }

    /**
     * Removes a visit corresponding to a canceled appointment from the linked list.
     * 
     * @param appointment The appointment to be removed.
     */
    public void removeVisit(Appointment appointment) {
        Visit current = visits;
        Visit previous = null;

        // Traverse the visit list to find the matching appointment
        while (current != null) {
            if (current.getAppointment().equals(appointment)) {
                if (previous == null) {
                    visits = current.getNext(); // Remove first visit
                } else {
                    previous.setNext(current.getNext()); // Remove current visit
                }
                current = null; // Explicitly nullify current to help garbage collection
                return;
            }
            previous = current;
            current = current.getNext();
        }
    }

    /**
     * Gets the patient's profile.
     * 
     * @return The patient's profile.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Gets the linked list of visits for the patient.
     * 
     * @return The linked list of visits.
     */
    public Visit getVisits() {
        return visits;
    }

    /**
     * Sets the linked list of visits for the patient.
     * 
     * @param visits The linked list of visits to be set.
     */
    public void setVisits(Visit visits) {
        this.visits = visits;
    }
}
