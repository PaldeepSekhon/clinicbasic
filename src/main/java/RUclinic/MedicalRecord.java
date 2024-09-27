package RUclinic;

public class MedicalRecord {
    private Patient[] patients;
    private int size; // number of patient objects in the array
    private static final int INITIAL_CAPACITY = 4;

    // Constructor
    public MedicalRecord() {
        patients = new Patient[INITIAL_CAPACITY];
        size = 0;
    }

    // Method to add a patient to the array
    public void add(Patient patient) {
        if (size == patients.length) {
            grow();
        }
        patients[size++] = patient;
    }

    // Helper method to grow the array capacity
    private void grow() {
        Patient[] newPatients = new Patient[patients.length + INITIAL_CAPACITY];
        System.arraycopy(patients, 0, newPatients, 0, patients.length);
        patients = newPatients;
    }

    // Method to get the list of patients (if needed)
    public Patient[] getPatients() {
        return patients;
    }

    // Method to get the number of patients (if needed)
    public int getSize() {
        return size;
    }
}

