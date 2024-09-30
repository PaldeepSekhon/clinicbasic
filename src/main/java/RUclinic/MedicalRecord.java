package ruclinic;

/**
 * Class that represents a collection of patient medical records.
 * 
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */
public class MedicalRecord {
    private Patient[] patients;
    private int size; // number of patient objects in the array
    private static final int INITIAL_CAPACITY = 4;

    /**
     * Constructor for MedicalRecord.
     * Initializes an array to hold patient records with an initial capacity.
     */
    public MedicalRecord() {
        patients = new Patient[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Adds a new patient to the medical record.
     * If the array capacity is exceeded, it automatically grows.
     * 
     * @param patient The patient object to be added.
     */
    public void add(Patient patient) {
        if (size == patients.length) {
            grow();
        }
        patients[size++] = patient;
    }

    /**
     * Helper method to increase the array capacity when needed.
     * It grows the array by the value of INITIAL_CAPACITY.
     */
    private void grow() {
        Patient[] newPatients = new Patient[patients.length + INITIAL_CAPACITY];
        System.arraycopy(patients, 0, newPatients, 0, patients.length);
        patients = newPatients;
    }

    /**
     * Gets the list of patients currently in the medical record.
     * 
     * @return An array of Patient objects.
     */
    public Patient[] getPatients() {
        return patients;
    }

    /**
     * Gets the current size of the medical record.
     * 
     * @return The number of patients in the medical record.
     */
    public int getSize() {
        return size;
    }

    /**
     * Finds a patient in the medical record by their profile.
     * 
     * @param profile The profile of the patient to be found.
     * @return The Patient object if found, otherwise returns null.
     */
    public Patient findPatient(Profile profile) {
        for (int i = 0; i < size; i++) {
            if (patients[i].getProfile().equals(profile)) {
                return patients[i];
            }
        }
        return null; // Return null if the patient is not found
    }
}
