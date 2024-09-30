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

    // Override equals() method to compare all fields
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Appointment that = (Appointment) obj;
        return date.equals(that.date) &&
                timeslot.equals(that.timeslot) &&
                patient.equals(that.patient) &&
                provider.equals(that.provider); // Compare the provider as well
    }

    // Override compareTo() method for sorting by date, then timeslot, then patient
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

    // Override toString() method for output in the desired format
    @Override
    public String toString() {
        return String.format("%s %s %s %s %s [%s, %s, %s %s, %s]",
                this.date, // Appointment date
                this.timeslot, // Timeslot
                this.patient.getFname(), // Patient's first name
                this.patient.getLname(), // Patient's last name
                this.patient.getDob(), // Patient's date of birth
                this.provider.name(), // Provider's name
                this.provider.getLocation().getCity(), // Provider's location city
                this.provider.getLocation().getCounty(), // Provider's location county
                this.provider.getLocation().getZip(), // Provider's location ZIP code
                this.provider.getSpecialty().getNameOnly() // Provider's specialty (without price)
        );
    }

    // Getters for accessing fields in the Scheduler class
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

    // Setter for Timeslot to allow rescheduling
    public void setTimeSlot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    // Additional setter in case you want to update provider or other fields (if
    // needed)
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
