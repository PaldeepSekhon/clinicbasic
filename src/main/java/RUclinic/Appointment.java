package RUclinic;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private Timeslot timeslot;
    private Profile patient;
    private Provider provider;

    // Constructor
    public Appointment(Date date, Timeslot timeslot, Profile patient, Provider provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    // Override equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Appointment that = (Appointment) obj;
        return date.equals(that.date) &&
                timeslot.equals(that.timeslot) &&
                patient.equals(that.patient); // Compare the patient profiles using the equals() method of Profile
    }

    // Override compareTo() method
    @Override
    public int compareTo(Appointment other) {
        // First compare by date
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        // Then compare by timeslot
        int timeslotComparison = this.timeslot.compareTo(other.timeslot);
        if (timeslotComparison != 0) {
            return timeslotComparison;
        }
        // Finally compare by patient profile
        return this.patient.compareTo(other.patient);
    }

    // Override toString() method
    @Override
    public String toString() {
        return String.format("%s %s %s [%s]",
                date,
                timeslot,
                patient,
                provider);
    }

    // Getters (optional, in case you need to access the fields externally)
    public Date getDate() {
        return date;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public Profile getPatient() {
        return patient;
    }

    public Provider getProvider() {
        return provider;
    }
}
