package ruclinic;

/**
 * The Scheduler class is responsible for managing appointments in the clinic.
 * It can schedule, cancel, reschedule appointments, and print appointment lists sorted by different criteria.
 * The scheduler also provides billing statements for patients.
 * 
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */

import java.util.Calendar;
import java.util.Scanner;

public class Scheduler {
    private List appointmentList;
      /**
     * Constructor to initialize a Scheduler object with an empty appointment list.
     */
    public Scheduler() {
        appointmentList = new List();
    }
    /**
     * Runs the scheduler, continuously accepting and processing user commands until the 'Q' command is entered.
     * Commands include scheduling, canceling, rescheduling, and printing appointments or billing statements.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Scheduler is running.\n");
        while (true) {
            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty()) {
                continue;
            }

            String[] tokens = commandLine.split(",");
            String command = tokens[0];

            switch (command) {
                case "S":
                    scheduleAppointment(tokens);
                    break;
                case "C":
                    cancelAppointment(tokens);
                    break;
                case "R":
                    rescheduleAppointment(tokens);
                    break;
                case "PA":
                    if (appointmentList.size() > 0) {
                        System.out.println();
                        System.out.println("** Appointments ordered by date/time/provider **");
                        appointmentList.sortByAppointment();
                        for (int i = 0; i < appointmentList.size(); i++) {
                            System.out.println(appointmentList.get(i).toString());
                        }
                        System.out.println("** end of list **");
                    } else {
                        System.out.println("The schedule calendar is empty.");
                    }
                    break;
                case "PP":
                    if (appointmentList != null && appointmentList.size() > 0) {
                        System.out.println();
                        System.out.println("** Appointments ordered by patient/date/time **");
                        appointmentList.sortByPatient();
                        for (int i = 0; i < appointmentList.size(); i++) {
                            System.out.println(appointmentList.get(i).toString());
                        }
                        System.out.println("** end of list **");
                    } else {
                        System.out.println("The schedule calendar is empty.");
                    }
                    break;
                case "PL":
                    if (appointmentList.size() > 0) {
                        System.out.println();
                        System.out.println("** Appointments ordered by county/date/time **");
                        appointmentList.sortByLocation();
                        for (int i = 0; i < appointmentList.size(); i++) {
                            System.out.println(appointmentList.get(i).toString());
                        }
                        System.out.println("** end of list **");
                    } else {
                        System.out.println("The schedule calendar is empty.");
                    }
                    break;
                case "PS":
                    printBillingStatements();
                    break;
                case "Q":
                    System.out.println("Scheduler terminated.");
                    return;
                default:
                    System.out.println("Invalid command!");
                    break;
            }

        }
    }
     /**
     * Schedules an appointment based on input tokens. Validates the appointment date, provider, and time slot before adding it to the appointment list.
     * 
     * @param tokens The input data for the appointment to be scheduled.
     */
    private void scheduleAppointment(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid command!");
            return;
        }

        String[] appointmentDateParts = tokens[1].split("/");
        int appointmentMonth = Integer.parseInt(appointmentDateParts[0]);
        int appointmentDay = Integer.parseInt(appointmentDateParts[1]);
        int appointmentYear = Integer.parseInt(appointmentDateParts[2]);

        String[] dobParts = tokens[5].split("/");
        int dobMonth = Integer.parseInt(dobParts[0]);
        int dobDay = Integer.parseInt(dobParts[1]);
        int dobYear = Integer.parseInt(dobParts[2]);

        Date appointmentDate = new Date(appointmentYear, appointmentMonth, appointmentDay);
        Date dob = new Date(dobYear, dobMonth, dobDay);
        Calendar today = Calendar.getInstance();
        Calendar appointmentCal = Calendar.getInstance();
        appointmentCal.set(appointmentYear, appointmentMonth - 1, appointmentDay);
        Calendar sixMonths = Calendar.getInstance();
        sixMonths.add(Calendar.MONTH, 6);
        Calendar dobCal = Calendar.getInstance();
        dobCal.set(dobYear, dobMonth - 1, dobDay);
        String timeSlot = tokens[2];

        if (!dob.isValid()) {
            System.out.println("Patient dob: " + dob.toString() + " is not a valid calendar date.");
            return;
        }
        if (isSameDay(dobCal, today) || dobCal.after(today)) {
            System.out.println("Patient dob: " + dob.toString() + " is today or a date after today.");
            return;
        }
        String providerLast = tokens[6];
        if (!isValidProvider(providerLast)) {
            System.out.println(providerLast + " - provider doesn't exist.");
            return;
        }
        try {
            int timeSlotIndex = Integer.parseInt(timeSlot);
            if (timeSlotIndex < 1 || timeSlotIndex > Timeslot.values().length) {
                System.out.println(timeSlot + " is not a valid time slot.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(timeSlot + " is not a valid time slot.");
            return;
        }

        String fname = tokens[3];
        String lname = tokens[4];

        if (!isValidAppointment(appointmentDate, tokens[2], tokens[3], tokens[4], dob, tokens[6])) {
            System.out.println("Appointment date: " + appointmentDate.toString() + " is not a valid calendar date.");
            return;
        }

        // Check if date is before or equal to today
        if (isSameDay(appointmentCal, today) || appointmentCal.before(today)) {
            System.out.println("Appointment date: " + appointmentDate.toString() + " is today or a date before today.");
            return;
        }
        // check if its a weekend
        int dayWeek = appointmentCal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == Calendar.SATURDAY || dayWeek == Calendar.SUNDAY) {
            System.out.println("Appointment date: " + appointmentDate.toString() + " is Saturday or Sunday.");
            return;
        }
        // check if date is within next 6 months
        if (appointmentCal.after(sixMonths)) {
            System.out.println("Appointment date: " + appointmentDate.toString() + " is not within six months.");
            return;
        }

        Appointment appointment = new Appointment(appointmentDate, Timeslot.values()[Integer.parseInt(tokens[2]) - 1],
                new Profile(tokens[3], tokens[4], dob), Provider.valueOf(tokens[6].toUpperCase()));
        if (appointmentList.contains(appointment)) {
            System.out.println(
                    fname + " " + lname + " " + dob.toString() + " has an existing appointment at the same time slot");
            return;
        }

        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment existingAppointment = appointmentList.get(i);
            if (existingAppointment.getProvider().equals(appointment.getProvider()) &&
                    existingAppointment.getTimeslot().equals(appointment.getTimeslot()) &&
                    existingAppointment.getDate().equals(appointment.getDate())) {
                System.out.println(
                        "[" + appointment.getProvider().toString() + "]" + " is not available at slot " + timeSlot);
                return;
            }
        }

        appointmentList.add(appointment);
        System.out.println(appointment.toString() + " booked.");
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

     /**
     * Cancels an existing appointment based on input tokens.
     * 
     * @param tokens The input data identifying the appointment to be canceled.
     */
    private void cancelAppointment(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid command!");
            return;
        }

        String[] appointmentDateParts = tokens[1].split("/");
        int appointmentMonth = Integer.parseInt(appointmentDateParts[0]);
        int appointmentDay = Integer.parseInt(appointmentDateParts[1]);
        int appointmentYear = Integer.parseInt(appointmentDateParts[2]);

        String[] dobParts = tokens[5].split("/");
        int dobMonth = Integer.parseInt(dobParts[0]);
        int dobDay = Integer.parseInt(dobParts[1]);
        int dobYear = Integer.parseInt(dobParts[2]);

        Date appointmentDate = new Date(appointmentYear, appointmentMonth, appointmentDay);
        Date dob = new Date(dobYear, dobMonth, dobDay);

        Appointment appointment = findAppointment(appointmentDate, tokens[2], tokens[3], tokens[4], dob);
        if (appointment != null) {
            appointmentList.remove(appointment);
            System.out.println(appointment.getDate() + " " + appointment.getTimeslot() + " " + appointment.getPatient()
                    + " " + "has been canceled.");
        } else {
            String timeslotString = Timeslot.values()[Integer.parseInt(tokens[2]) - 1].toString();
            System.out.println(appointmentDate + " " + timeslotString + " " + tokens[3] + " " + tokens[4]
                    + " " + dob + " does not exist.");
        }
    }
     /**
     * Reschedules an existing appointment to a new time slot based on input tokens.
     * 
     * @param tokens The input data identifying the appointment to be rescheduled.
     */
    private void rescheduleAppointment(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid command!");
            return;
        }

        String[] appointmentDateParts = tokens[1].split("/");
        int appointmentMonth = Integer.parseInt(appointmentDateParts[0]);
        int appointmentDay = Integer.parseInt(appointmentDateParts[1]);
        int appointmentYear = Integer.parseInt(appointmentDateParts[2]);

        String[] dobParts = tokens[5].split("/");
        int dobMonth = Integer.parseInt(dobParts[0]);
        int dobDay = Integer.parseInt(dobParts[1]);
        int dobYear = Integer.parseInt(dobParts[2]);

        Date appointmentDate = new Date(appointmentYear, appointmentMonth, appointmentDay);
        Date dob = new Date(dobYear, dobMonth, dobDay);

        Appointment appointment = findAppointment(appointmentDate, tokens[2], tokens[3], tokens[4], dob);

        if (appointment == null) {
            String timeslotString = Timeslot.values()[Integer.parseInt(tokens[2]) - 1].toString();
            System.out.println(appointmentDate + " " + timeslotString + " " + tokens[3] + " " + tokens[4] + " " + dob
                    + " does not exist.");
            return;
        }

        if (!isValidTimeSlot(tokens[6])) {
            System.out.println(tokens[6] + " is not a valid time slot.");
            return;
        }

        int newTimeSlotIndex = Integer.parseInt(tokens[6]) - 1;

        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment existingAppointment = appointmentList.get(i);
            if (existingAppointment.getProvider().equals(appointment.getProvider()) &&
                    existingAppointment.getDate().equals(appointment.getDate()) &&
                    existingAppointment.getTimeslot().ordinal() == newTimeSlotIndex) {
                System.out.println(
                        "[" + appointment.getProvider().toString() + "]" + " is not available at slot " + tokens[6]);
                return;
            }
        }

        appointment.setTimeSlot(Timeslot.values()[newTimeSlotIndex]);
        System.out.println("Rescheduled to " + appointment.toString());
    }
    /**
 * Validates the appointment's date, time slot, patient details, and provider.
 * Ensures that the date and provider are valid, and checks that the time slot is within the valid range.
 * 
 * @param date The date of the appointment.
 * @param timeSlot The time slot of the appointment.
 * @param fname The first name of the patient.
 * @param lname The last name of the patient.
 * @param dob The date of birth of the patient.
 * @param providerLastName The last name of the provider.
 * @return true if the appointment details are valid, false otherwise.
 */
    private boolean isValidAppointment(Date date, String timeSlot, String fname, String lname, Date dob,
            String providerLastName) {
        if (!date.isValid() || !dob.isValid() || !isValidProvider(providerLastName))
            return false;

        try {
            int timeSlotIndex = Integer.parseInt(timeSlot);
            if (timeSlotIndex < 1 || timeSlotIndex > Timeslot.values().length)
                return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
/**
 * Validates if the provider's last name corresponds to a valid provider in the system.
 * 
 * @param providerLastName The last name of the provider.
 * @return true if the provider exists, false otherwise.
 */
    private boolean isValidProvider(String providerLastName) {
        try {
            Provider.valueOf(providerLastName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
/**
 * Finds an appointment based on the input data for date, time slot, patient name, and date of birth.
 * Searches through the appointment list to find a matching appointment.
 * 
 * @param date The date of the appointment.
 * @param timeSlot The time slot of the appointment.
 * @param fname The first name of the patient.
 * @param lname The last name of the patient.
 * @param dob The date of birth of the patient.
 * @return The Appointment object if found, or null if not found.
 */
    private Appointment findAppointment(Date date, String timeSlot, String fname, String lname, Date dob) {
        Profile patientProfile = new Profile(fname, lname, dob);
        int timeSlotIndex = Integer.parseInt(timeSlot) - 1;

        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment appt = appointmentList.get(i);
            if (appt.getDate().equals(date) && appt.getPatient().equals(patientProfile)
                    && appt.getTimeslot().ordinal() == timeSlotIndex) {
                return appt;
            }
        }
        return null;
    }
/**
 * Validates if the time slot provided is within the valid range of time slots.
 * 
 * @param timeSlot The time slot to validate.
 * @return true if the time slot is valid, false otherwise.
 */
    private boolean isValidTimeSlot(String timeSlot) {
        try {
            int slot = Integer.parseInt(timeSlot);
            return slot >= 1 && slot <= Timeslot.values().length;
        } catch (NumberFormatException e) {
            return false;
        }
    }
/**
 * Prints billing statements for all patients, sorted by patient.
 * Displays the total amount due for each patient based on the specialty of their provider.
 */
    private void printBillingStatements() {
        appointmentList.sortByPatient();
        System.out.println();
        System.out.println("** Billing statement ordered by patient **");
        if (appointmentList.size() == 0) {
            System.out.println("** end of list **");
            return;
        }

        int count = 1;
        Appointment firstAppointment = appointmentList.get(0);
        Profile currentPatient = firstAppointment.getPatient();
        double totalAmountDue = 0;

        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment appointment = appointmentList.get(i);
            Profile patient = appointment.getPatient();
            int charge = calculateCharge(appointment.getProvider().getSpecialty());

            if (patient.equals(currentPatient)) {
                totalAmountDue += charge;
            } else {
                System.out.printf("(%d) %s [amount due: $%,.2f]%n", count++, currentPatient, totalAmountDue);
                currentPatient = patient;
                totalAmountDue = charge;
            }
        }

        System.out.printf("(%d) %s [amount due: $%,.2f]%n", count, currentPatient, totalAmountDue);
        System.out.println("** end of list **");

        appointmentList = null;
    }
/**
 * Calculates the charge for a given specialty.
 * The charge is determined by the specialty of the provider.
 * 
 * @param specialty The specialty of the provider.
 * @return The charge amount based on the specialty.
 */
    private int calculateCharge(Specialty specialty) {
        switch (specialty) {
            case FAMILY:
                return 250;
            case PEDIATRICIAN:
                return 300;
            case ALLERGIST:
                return 350;
            default:
                return 0;
        }
    }
}
