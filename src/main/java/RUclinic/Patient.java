package RUclinic;

public class Patient implements Comparable<Patient> {
    private Profile profile;
    private Visit visits; // a linked list of visits (completed appointments)

    // Constructor
    public Patient(Profile profile) {
        this.profile = profile;
        this.visits = null;
    }

    // Method to compute the total charge by traversing the linked list of visits
    public int charge() {
        int totalCharge = 0;
        Visit currentVisit = visits;

        while (currentVisit != null) {
            totalCharge += currentVisit.getAppointment().getProvider().getSpecialty().getCharge();
            currentVisit = currentVisit.getNext();
        }

        return totalCharge;
    }

    // Override equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Patient patient = (Patient) obj;
        return profile.equals(patient.profile);
    }

    // Override compareTo() method
    @Override
    public int compareTo(Patient other) {
        return this.profile.compareTo(other.profile);
    }

    // Override toString() method
    @Override
    public String toString() {
        return profile.toString();
    }

    // Method to add a visit to the linked list of visits
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

    // Method to remove a visit corresponding to a canceled appointment
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

    // Getter for profile (if needed)
    public Profile getProfile() {
        return profile;
    }

    // Getter for visits (for access in the cancellation logic)
    public Visit getVisits() {
        return visits;
    }

    // Setter for visits (to update the head of the visit list if needed)
    public void setVisits(Visit visits) {
        this.visits = visits;
    }
}
