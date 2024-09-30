package ruclinic;

/**
 * The List class manages a dynamic array of Appointment objects for the
 * RUclinic system.
 * 
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */
public class List {
    private Appointment[] appointments;
    private int size; // number of appointments in the array
    private static final int NOT_FOUND = -1;
    private static final int INITIAL_CAPACITY = 4;

    /**
     * Constructs an empty list of appointments with an initial capacity.
     */
    public List() {
        appointments = new Appointment[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Finds the index of the specified appointment in the array.
     * 
     * @param appointment The appointment to search for.
     * @return The index of the appointment if found, -1 otherwise.
     */
    private int find(Appointment appointment) {
        for (int i = 0; i < size; i++) {
            if (appointments[i].equals(appointment)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Increases the size of the appointment array by 4.
     */
    private void grow() {
        Appointment[] newAppointments = new Appointment[appointments.length + 4];
        System.arraycopy(appointments, 0, newAppointments, 0, appointments.length);
        appointments = newAppointments;
    }

    /**
     * Checks if the list contains a specific appointment.
     * 
     * @param appointment The appointment to check for.
     * @return true if the list contains the appointment, false otherwise.
     */
    public boolean contains(Appointment appointment) {
        return find(appointment) != NOT_FOUND;
    }

    /**
     * Adds a new appointment to the list. If the array is full, its capacity is
     * increased.
     * 
     * @param appointment The appointment to add to the list.
     */
    public void add(Appointment appointment) {
        if (size == appointments.length) {
            grow();
        }
        appointments[size++] = appointment;
    }

    /**
     * Removes an appointment from the list. The array elements are shifted to fill
     * the gap.
     * 
     * @param appointment The appointment to remove from the list.
     */
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

    /**
     * Gets the number of appointments in the list.
     * 
     * @return The number of appointments in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves an appointment at a given index.
     * 
     * @param index The index of the appointment to retrieve.
     * @return The appointment at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Appointment get(int index) {
        if (index >= 0 && index < size) {
            return appointments[index];
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
    }

    /**
     * Sorts appointments by patient profile, and then by date and timeslot.
     */
    public void sortByPatient() {
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

    /**
     * Sorts appointments by date and timeslot, then by provider name.
     */
    public void sortByAppointment() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0) {
                    swap(j, j + 1);
                } else if (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) == 0 &&
                        appointments[j].getTimeslot().compareTo(appointments[j + 1].getTimeslot()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    /**
     * Sorts appointments by provider location, then by date and timeslot.
     */
    public void sortByLocation() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (appointments[j].getProvider().getLocation().getCounty()
                        .compareTo(appointments[j + 1].getProvider().getLocation().getCounty()) > 0) {
                    swap(j, j + 1);
                }
                // If counties are the same, compare by date
                else if (appointments[j].getProvider().getLocation().getCounty()
                        .compareTo(appointments[j + 1].getProvider().getLocation().getCounty()) == 0 &&
                        appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0) {
                    swap(j, j + 1);
                }
                // If counties and dates are the same, compare by timeslot
                else if (appointments[j].getProvider().getLocation().getCounty()
                        .compareTo(appointments[j + 1].getProvider().getLocation().getCounty()) == 0 &&
                        appointments[j].getDate().compareTo(appointments[j + 1].getDate()) == 0 &&
                        appointments[j].getTimeslot().compareTo(appointments[j + 1].getTimeslot()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    /**
     * Swaps two appointments in the array.
     * 
     * @param i The index of the first appointment.
     * @param j The index of the second appointment.
     */
    private void swap(int i, int j) {
        Appointment temp = appointments[i];
        appointments[i] = appointments[j];
        appointments[j] = temp;
    }
}
