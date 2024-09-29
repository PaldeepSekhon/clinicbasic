package RUclinic;

import java.util.Scanner;

public class Scheduler {
    private Appointment[] appointments;
    private int appointmentCount;
    private MedicalRecord medicalRecord;
    private static final int INITIAL_CAPACITY = 10;

    // Constructor
    public Scheduler() {
        appointments = new Appointment[INITIAL_CAPACITY];
        appointmentCount = 0;
        medicalRecord = new MedicalRecord();
    }

    // Method to process commands
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Scheduler is running.\n");

        while (true) {
            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty())
                continue;

            String[] tokens = commandLine.split(",");
            String command = tokens[0];

            switch (command) {
                case "S":
                    handleScheduleCommand(tokens);
                    break;
                case "C":
                    handleCancelCommand(tokens);
                    break;
                case "R":
                    handleRescheduleCommand(tokens);
                    break;
                case "PA":
                    printAppointmentsByAppointment();
                    break;
                case "PP":
                    printAppointmentsByPatient();
                    break;
                case "PL":
                    printAppointmentsByLocation();
                    break;
                case "PS":
                    printBillingStatements();
                    break;
                case "Q":
                    System.out.println("Scheduler is terminated.");
                    return;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }
    }

    // Helper methods for command processing
    private void handleScheduleCommand(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid input format for scheduling.");
            return;
        }

        // Validate and parse the time slot
        String timeSlotInput = tokens[2];
        int timeSlotNumber;
        try {
            timeSlotNumber = Integer.parseInt(timeSlotInput);
            if (timeSlotNumber < 1 || timeSlotNumber > Timeslot.values().length) {
                System.out.printf("%s is not a valid time slot.%n", timeSlotInput);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.printf("%s is not a valid time slot.%n", timeSlotInput);
            return;
        }

        Timeslot timeslot = Timeslot.values()[timeSlotNumber - 1];

        Date date = parseDate(tokens[1]);
        Profile patientProfile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));
        Patient patient = new Patient(patientProfile);

        // Check if the provider is valid
        Provider provider = Provider.fromString(tokens[6]);
        if (provider == null) {
            System.out.printf("%s - provider doesn't exist.%n", tokens[6]);
            return;
        }

        // Check if the patient's date of birth is valid
        if (!patientProfile.getDob().isValid()) {
            System.out.printf("Patient dob: %s is not a valid calendar date.%n", patientProfile.getDob().toString());
            return;
        }

        // Check if the appointment date is valid
        if (!date.isValid()) {
            System.out.printf("Appointment date: %s is not a valid calendar date.%n", date.toString());
            return;
        }

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar months are 0-based
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        Date today = new Date(year, month, day);

        if (date.compareTo(today) <= 0) {
            System.out.printf("Appointment date: %s is today or a date before today.%n", date);
            return;
        }

        java.util.Calendar calendar2 = java.util.Calendar.getInstance();
        calendar2.add(java.util.Calendar.MONTH, 6);
        int year2 = calendar2.get(java.util.Calendar.YEAR);
        int month2 = calendar2.get(java.util.Calendar.MONTH) + 1; // Calendar months are 0-based
        int day2 = calendar2.get(java.util.Calendar.DAY_OF_MONTH);
        Date sixmonth = new Date(year2, month2, day2);

        if (date.compareTo(sixmonth) >= 0) {
            System.out.printf("Appointment date: %s is not within six months.%n", date);
            return;
        }

        if (isWeekend(date)) {
            System.out.printf("Appointment date: %s is Saturday or Sunday.%n", date);
            return;
        }

        // Check if the provider is available at the specified timeslot
        for (int i = 0; i < appointmentCount; i++) {
            Appointment existingAppointment = appointments[i];
            if (existingAppointment.getDate().equals(date) &&
                    existingAppointment.getTimeslot().equals(timeslot)) {
                if (existingAppointment.getProvider().equals(provider)) {
                    // If provider is unavailable at the same slot
                    System.out.printf("[%s, %s, %s %s] is not available at slot %s.%n",
                            provider.name(),
                            provider.getLocation().name(),
                            provider.getLocation().toString(),
                            provider.getSpecialty().getNameOnly(),
                            timeSlotNumber);

                    return;
                } else if (existingAppointment.getPatient().equals(patient)) {
                    // If patient already has an appointment at the same slot
                    System.out.printf("%s %s %s has an existing appointment at the same time slot.%n",
                            patientProfile.getFname(),
                            patientProfile.getLname(),
                            patientProfile.getDob().toString());
                    return;
                }
            }
        }

        // Schedule the new appointment
        Appointment newAppointment = new Appointment(date, timeslot, patientProfile, provider);
        addAppointment(newAppointment);
        Patient existingPatient = medicalRecord.findPatient(patientProfile); // Assuming findPatient method exists
        if (existingPatient == null) {
            medicalRecord.add(patient);
            existingPatient = patient; // Now the patient is in the medical record
        }

        Visit newVisit = new Visit(newAppointment);
        existingPatient.addVisit(newVisit); // Add the visit to the patient's history
        System.out.printf("%s %s %s %s %s [%s, %s, %s, %s] booked.%n",
                date,
                timeslot,
                patientProfile.getFname(),
                patientProfile.getLname(),
                patientProfile.getDob(),
                provider.name(),
                provider.getLocation().name(),
                provider.getLocation().toString(),
                provider.getSpecialty().getNameOnly());

    }

    private void handleCancelCommand(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid input format for cancellation.");
            return;
        }

        Date date = parseDate(tokens[1]);
        Timeslot timeslot = Timeslot.fromString(tokens[2]);
        Profile patientProfile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));

        boolean found = false; // To track if the appointment was found and canceled

        for (int i = 0; i < appointmentCount; i++) {
            Appointment appointment = appointments[i];

            // Check if the appointment matches date, timeslot, and patient
            if (appointment.getDate().equals(date) &&
                    appointment.getTimeslot().equals(timeslot) &&
                    appointment.getPatient().equals(patientProfile)) {

                // Find the patient in the medical record
                Patient patient = medicalRecord.findPatient(patientProfile);
                if (patient != null) {
                    // Remove the corresponding visit from the patient's visit list
                    patient.removeVisit(appointment);
                }

                // Remove the appointment from the appointments array
                removeAppointment(i); // Remove the appointment from the list

                // Print the successful cancellation message
                System.out.printf("%s %s %s %s %s has been canceled.%n",
                        date, timeslot, patientProfile.getFname(), patientProfile.getLname(), patientProfile.getDob());

                found = true; // Mark that the appointment has been found and canceled
                break; // Exit the loop once the appointment is found and canceled
            }
        }

        if (!found) {
            // Print the failed cancellation message if no appointment was found
            System.out.printf("%s %s %s %s %s does not exist.%n",
                    date, timeslot, patientProfile.getFname(), patientProfile.getLname(), patientProfile.getDob());
        }
    }

    private void handleRescheduleCommand(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid input format for rescheduling.");
            return;
        }

        Date oldDate = parseDate(tokens[1]);
        Timeslot oldTimeslot = Timeslot.fromString(tokens[2]);
        Profile patientProfile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));

        boolean found = false; // To track if the appointment to be rescheduled is found

        // Check if the old appointment exists
        for (int i = 0; i < appointmentCount; i++) {
            Appointment existingAppointment = appointments[i];

            if (existingAppointment.getDate().equals(oldDate) &&
                    existingAppointment.getTimeslot().equals(oldTimeslot) &&
                    existingAppointment.getPatient().equals(patientProfile)) {

                found = true; // Mark that the appointment is found

                // Now validate the new time slot after confirming the appointment exists
                int newTimeSlotNumber;
                try {
                    newTimeSlotNumber = Integer.parseInt(tokens[6]);
                    if (newTimeSlotNumber < 1 || newTimeSlotNumber > Timeslot.values().length) {
                        System.out.printf("%d is not a valid time slot.%n", newTimeSlotNumber);
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.printf("%s is not a valid time slot.%n", tokens[6]);
                    return;
                }

                Timeslot newTimeslot = Timeslot.values()[newTimeSlotNumber - 1];

                // Ensure no conflict with the new timeslot
                for (int j = 0; j < appointmentCount; j++) {
                    if (appointments[j].getDate().equals(oldDate) &&
                            appointments[j].getTimeslot().equals(newTimeslot) &&
                            appointments[j].getProvider().equals(existingAppointment.getProvider())) {
                        System.out.printf("[%s, %s, %s, %s] is not available at slot %d.%n",
                                existingAppointment.getProvider().name(),
                                existingAppointment.getProvider().getLocation().name(),
                                existingAppointment.getProvider().getLocation().toString(),
                                existingAppointment.getProvider().getSpecialty().getNameOnly(),
                                newTimeSlotNumber);
                        return;
                    }
                }

                // Reschedule the appointment to the new timeslot
                appointments[i] = new Appointment(oldDate, newTimeslot, patientProfile,
                        existingAppointment.getProvider());
                System.out.printf("Rescheduled to %s %s %s %s %s [%s, %s, %s, %s]%n",
                        oldDate, // Appointment date
                        newTimeslot, // New time slot
                        patientProfile.getFname(), // Patient's first name
                        patientProfile.getLname(), // Patient's last name
                        patientProfile.getDob().toString(), // Patient's date of birth
                        existingAppointment.getProvider().name(), // Provider's name
                        existingAppointment.getProvider().getLocation().name(), // Location name
                        existingAppointment.getProvider().getLocation().toString(), // Location county and zip
                        existingAppointment.getProvider().getSpecialty().getNameOnly() // Provider's specialty
                );

                return; // Exit after successful rescheduling
            }
        }

        // If no appointment was found to reschedule, print the failed message
        if (!found) {
            System.out.printf("%s %s %s %s %s does not exist.%n",
                    oldDate, oldTimeslot, patientProfile.getFname(),
                    patientProfile.getLname(), patientProfile.getDob().toString());
        }
    }

    private void printAppointmentsByAppointment() {
        // If no appointments exist, print the empty message and return
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }

        // Print the heading and an empty line
        System.out.println();
        System.out.println("** Appointments ordered by date/time/provider **");

        // Sort appointments by date, then timeslot, then provider
        sortAppointmentsBy((a, b) -> {
            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0)
                return dateComparison;

            int timeslotComparison = a.getTimeslot().compareTo(b.getTimeslot());
            if (timeslotComparison != 0)
                return timeslotComparison;

            return a.getProvider().compareTo(b.getProvider());
        });

        // Print each appointment
        for (int i = 0; i < appointmentCount; i++) {
            Appointment appt = appointments[i];
            Profile profile = appt.getPatient(); // Access Profile directly
            Provider provider = appt.getProvider();

            // Format output: date, time, patient full name and dob, provider details
            System.out.printf("%s %s %s %s %s [%s, %s, %s, %s]%n",
                    appt.getDate(), // Date
                    appt.getTimeslot(), // Time slot
                    profile.getFname(), // Patient's first name
                    profile.getLname(), // Patient's last name
                    profile.getDob(), // Patient's date of birth
                    provider.name(), // Provider's name
                    provider.getLocation().name(), // Location name
                    provider.getLocation().toString(), // Location (county and zip)
                    provider.getSpecialty().getNameOnly() // Specialty without the charge
            );

        }

        // Add ** end of list ** after printing the appointments
        System.out.println("** end of list **");
    }

    private void printAppointmentsByPatient() {
        // If no appointments exist, print the empty message and return
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }

        // Print the heading and an empty line
        System.out.println();
        System.out.println("** Appointments ordered by patient/date/time **");

        // Sort appointments by patient, then date, then timeslot
        sortAppointmentsBy((a, b) -> {
            int patientComparison = a.getPatient().compareTo(b.getPatient());
            if (patientComparison != 0)
                return patientComparison;

            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0)
                return dateComparison;

            return a.getTimeslot().compareTo(b.getTimeslot());
        });

        // Print each appointment
        for (int i = 0; i < appointmentCount; i++) {
            Appointment appt = appointments[i];
            Profile profile = appt.getPatient(); // Access Profile directly
            Provider provider = appt.getProvider();

            // Format output: date, time, patient full name and dob, provider details
            System.out.printf("%s %s %s %s %s [%s, %s, %s, %s]%n",
                    appt.getDate(), // Date
                    appt.getTimeslot(), // Time slot
                    profile.getFname(), // Patient's first name
                    profile.getLname(), // Patient's last name
                    profile.getDob(), // Patient's date of birth
                    provider.name(), // Provider's name
                    provider.getLocation().name(), // Location name
                    provider.getLocation().toString(), // Location (county and zip)
                    provider.getSpecialty().getNameOnly() // Specialty without the charge
            );

        }

        // Add ** end of list ** after printing the appointments
        System.out.println("** end of list **");
    }

    private void printAppointmentsByLocation() {
        // If no appointments exist, print the empty message and return
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }

        // Print the heading and an empty line
        System.out.println();
        System.out.println("** Appointments ordered by county/date/time **");

        // Sort appointments by provider location, then date, then timeslot
        sortAppointmentsBy((a, b) -> {
            int locationComparison = a.getProvider().getLocation().compareTo(b.getProvider().getLocation());
            if (locationComparison != 0)
                return locationComparison;

            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0)
                return dateComparison;

            return a.getTimeslot().compareTo(b.getTimeslot());
        });

        // Print each appointment
        for (int i = 0; i < appointmentCount; i++) {
            Appointment appt = appointments[i];
            Profile profile = appt.getPatient(); // Access Profile directly
            Provider provider = appt.getProvider();

            // Format output: date, time, patient full name and dob, provider details
            System.out.printf("%s %s %s %s %s [%s, %s, %s, %s]%n",
                    appt.getDate(), // Date
                    appt.getTimeslot(), // Time slot
                    profile.getFname(), // Patient's first name
                    profile.getLname(), // Patient's last name
                    profile.getDob(), // Patient's date of birth
                    provider.name(), // Provider's name
                    provider.getLocation().name(), // Location name
                    provider.getLocation().toString(), // Location (county and zip)
                    provider.getSpecialty().getNameOnly() // Specialty without the charge
            );

        }

        // Add ** end of list ** after printing the appointments
        System.out.println("** end of list **");
    }

    private void printBillingStatements() {
        // Sort the patients by their profile information (names)
        Patient[] patients = medicalRecord.getPatients();
        java.util.Arrays.sort(patients, 0, medicalRecord.getSize(),
                (p1, p2) -> p1.getProfile().compareTo(p2.getProfile()));
        System.out.println();
        System.out.println("** Billing statement ordered by patient **");

        for (int i = 0; i < medicalRecord.getSize(); i++) {
            Patient patient = patients[i];
            Date dob = patient.getProfile().getDob(); // Get the date of birth object

            // Manually format the date of birth without square brackets
            String formattedDob = String.format("%d/%d/%d", dob.getMonth(), dob.getDay(), dob.getYear());

            // Printing the billing statement with the correct format
            System.out.printf("(%d) %s %s %s [amount due: $%.2f]%n",
                    (i + 1), // Index starts at 1
                    patient.getProfile().getFname(), // Patient's first name
                    patient.getProfile().getLname(), // Patient's last name
                    formattedDob, // Formatted date of birth
                    (double) patient.charge()); // The amount due, formatted to two decimal places
        }

        System.out.println("** end of list **");
    }

    // Utility methods
    private void addAppointment(Appointment appointment) {
        if (appointmentCount == appointments.length) {
            growAppointmentArray();
        }
        appointments[appointmentCount++] = appointment;
    }

    private void removeAppointment(int index) {
        // Shift elements to the left to fill the gap
        for (int i = index; i < appointmentCount - 1; i++) {
            appointments[i] = appointments[i + 1];
        }
        // Clear the last appointment and decrease the count
        appointments[--appointmentCount] = null;

    }

    private void sortAppointmentsBy(java.util.Comparator<Appointment> comparator) {
        java.util.Arrays.sort(appointments, 0, appointmentCount, comparator);
    }

    private void growAppointmentArray() {
        Appointment[] newAppointments = new Appointment[appointments.length * 2];
        System.arraycopy(appointments, 0, newAppointments, 0, appointments.length);
        appointments = newAppointments;
    }

    private Date parseDate(String dateStr) {
        String[] parts = dateStr.split("/");
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return new Date(year, month, day);
    }

    private Timeslot parseTimeslot(String timeslotStr) {
        int slotNumber = Integer.parseInt(timeslotStr);

        return Timeslot.values()[slotNumber - 1];
    }

    private Provider parseProvider(String providerStr) {
        return Provider.valueOf(providerStr.toUpperCase());
    }

    private boolean isWeekend(Date date) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDay()); // Months are 0-based in Calendar
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        return (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY);
    }

}