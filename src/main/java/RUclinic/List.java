package RUclinic;

public class List {
    private Appointment[] appointments;
    private int size; // number of appointments in the array
    private static final int NOT_FOUND = -1;
    private static final int INITIAL_CAPACITY = 4;

    // Constructor
    public List() {
        appointments = new Appointment[INITIAL_CAPACITY];
        size = 0;
    }

    // Helper method to find an appointment in the list and return its index
    private int find(Appointment appointment) {
        for (int i = 0; i < size; i++) {
            if (appointments[i].equals(appointment)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    // Helper method to grow the array capacity by 4
    private void grow() {
        Appointment[] newAppointments = new Appointment[appointments.length + 4];
        System.arraycopy(appointments, 0, newAppointments, 0, appointments.length);
        appointments = newAppointments;
    }

    // Check if the list contains a specific appointment
    public boolean contains(Appointment appointment) {
        return find(appointment) != NOT_FOUND;
    }

    // Add a new appointment to the list
    public void add(Appointment appointment) {
        if (size == appointments.length) {
            grow();
        }
        appointments[size++] = appointment;
    }

    // Remove an appointment from the list
    public void remove(Appointment appointment) {
        int index = find(appointment);
        if (index != NOT_FOUND) {
            // Shift elements to the left to fill the gap
            for (int i = index; i < size - 1; i++) {
                appointments[i] = appointments[i + 1];
            }
            appointments[--size] = null; // Clear the last element
        }
    }

    // Print appointments ordered by patient profile, then by date/timeslot
    public void printByPatient() {
        sortByPatient();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i].toString());
        }
    }

    // Print appointments ordered by location, then by date/timeslot
    public void printByLocation() {
        sortByLocation();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i].toString());
        }
    }

    // Print appointments ordered by date/timeslot, then provider name
    public void printByAppointment() {
        sortByAppointment();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i].toString());
        }
    }

    // Sort appointments by patient profile, then by date/timeslot
    private void sortByPatient() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (appointments[j].getPatient().compareTo(appointments[j + 1].getPatient()) > 0) {
                    swap(j, j + 1);
                } else if (appointments[j].getPatient().compareTo(appointments[j + 1].getPatient()) == 0 &&
                        appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Sort appointments by location, then by date/timeslot
    private void sortByLocation() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (appointments[j].getProvider().getLocation()
                        .compareTo(appointments[j + 1].getProvider().getLocation()) > 0) {
                    swap(j, j + 1);
                } else if (appointments[j].getProvider().getLocation()
                        .compareTo(appointments[j + 1].getProvider().getLocation()) == 0 &&
                        appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Sort appointments by date/timeslot, then provider name
    private void sortByAppointment() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0) {
                    swap(j, j + 1);
                } else if (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) == 0 &&
                        appointments[j].getProvider().compareTo(appointments[j + 1].getProvider()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Helper method to swap two elements in the array
    private void swap(int i, int j) {
        Appointment temp = appointments[i];
        appointments[i] = appointments[j];
        appointments[j] = temp;
    }
}
