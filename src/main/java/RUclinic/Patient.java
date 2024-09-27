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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
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
                current = current.getNext();
            }
            current.setNext(visit);
        }
    }

    // Getter for profile (if needed)
    public Profile getProfile() {
        return profile;
    }
}
