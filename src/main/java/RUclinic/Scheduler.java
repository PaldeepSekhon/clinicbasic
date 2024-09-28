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
        System.out.println("Scheduler is running.");

        while (true) {
            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty()) continue;

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
                    System.out.println("Scheduler terminated.");
                    return;
                default:
                    System.out.println("Invalid command.");
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
        
        java.util.Calendar calendar = java.util.Calendar.getInstance();
    int year = calendar.get(java.util.Calendar.YEAR);
    int month = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar months are 0-based
    int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
     
     Date today = new Date(year, month, day);

     java.util.Calendar calendar2 = java.util.Calendar.getInstance();
     calendar2.add(java.util.Calendar.MONTH, 6);
     int year2 = calendar2.get(java.util.Calendar.YEAR);
    int month2 = calendar2.get(java.util.Calendar.MONTH) + 1; // Calendar months are 0-based
    int day2 = calendar2.get(java.util.Calendar.DAY_OF_MONTH);
    Date sixmonth = new Date(year2, month2, day2);


        

        Date date = parseDate(tokens[1]);
      //  Timeslot timeslot = parseTimeslot(tokens[2]);
        Profile patientProfile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));
        Patient patient = new Patient(patientProfile);
        //Provider provider = parseProvider(tokens[6]);

        Provider provider = Provider.fromString(tokens[6]);
        if (provider == null) {
            System.out.println("Invalid provider.");
            return;
        }
        Timeslot timeslot = Timeslot.fromString(tokens[2]);
        if(timeslot==null)
        {
            System.out.println("invalid timeslot");
            return;
        }

        if(!patientProfile.getDob().isValid())
        {
            System.out.print("Invalid Birth Date");
            return;
        }
        if(patientProfile.getDob().compareTo(today)>=0)
        {
            System.out.print("Invalid Birth Date");
            return;
        }

        if(!date.isValid())
        {
            System.out.print("Invalid Date");
            return;
        }

        if (date.compareTo(today) <= 0) {
        System.out.println("The appointment date cannot be Today or day before today.");
        return;

        
    }
    if (date.compareTo(sixmonth) >= 0) {
        System.out.println("The appointment date is not within 6 months.");
        return;
    }

    if (isWeekend(date)) {
        System.out.println("The appointment date falls on a weekend (Saturday or Sunday).");
        return;
    }



   
   

   



      

        Appointment newAppointment = new Appointment(date, timeslot, patientProfile, provider);
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].equals(newAppointment)) {
                System.out.println("Appointment already exists.");
                return;
            }
        }

        addAppointment(newAppointment);
        medicalRecord.add(patient);
        System.out.println("Appointment scheduled successfully.");
    }

    private void handleCancelCommand(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid input format for cancellation.");
            return;
        }

        Date date = parseDate(tokens[1]);
        Timeslot timeslot = parseTimeslot(tokens[2]);
        Profile patientProfile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));

        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].equals(new Appointment(date, timeslot, patientProfile, null))) {
                removeAppointment(i);
                System.out.println("Appointment cancelled successfully.");
                return;
            }
        }

        System.out.println("No appointment found to cancel.");
    }

    private void handleRescheduleCommand(String[] tokens) {
        if (tokens.length != 7) {
            System.out.println("Invalid input format for rescheduling.");
            return;
        }

        Date oldDate = parseDate(tokens[1]);
        Timeslot oldTimeslot = parseTimeslot(tokens[2]);
        Profile patientProfile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));
        Timeslot newTimeslot = parseTimeslot(tokens[6]);

        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].equals(new Appointment(oldDate, oldTimeslot, patientProfile, null))) {
                appointments[i] = new Appointment(oldDate, newTimeslot, patientProfile, appointments[i].getProvider());
                System.out.println("Appointment rescheduled successfully.");
                return;
            }
        }

        System.out.println("No appointment found to reschedule.");
    }

    private void printAppointmentsByAppointment() {
        sortAppointmentsBy((a, b) -> {
            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) return dateComparison;

            int timeslotComparison = a.getTimeslot().compareTo(b.getTimeslot());
            if (timeslotComparison != 0) return timeslotComparison;

            return a.getProvider().compareTo(b.getProvider());
        });
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println(appointments[i]);
        }

        if(appointmentCount==0)
        {
            System.out.println("The schedule calender is empty ");
        }
    }

    private void printAppointmentsByPatient() {
        sortAppointmentsBy((a, b) -> {
            int patientComparison = a.getPatient().compareTo(b.getPatient());
            if (patientComparison != 0) return patientComparison;

            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) return dateComparison;

            return a.getTimeslot().compareTo(b.getTimeslot());
        });
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println(appointments[i]);
        }

        if(appointmentCount==0)
        {
            System.out.println("The schedule calender is empty ");
        }
    }

    private void printAppointmentsByLocation() {
        sortAppointmentsBy((a, b) -> {
            int locationComparison = a.getProvider().getLocation().compareTo(b.getProvider().getLocation());
            if (locationComparison != 0) return locationComparison;

            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) return dateComparison;

            return a.getTimeslot().compareTo(b.getTimeslot());
        });
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println(appointments[i]);
        }

        if(appointmentCount==0)
        {
            System.out.println("The schedule calender is empty ");
        }
    }

    private void printBillingStatements() {
        for (int i = 0; i < medicalRecord.getSize(); i++) {
            Patient patient = medicalRecord.getPatients()[i];
            System.out.println("Billing Statement for: " + patient);
            System.out.println("Total Charges: $" + patient.charge());
            System.out.println();
        }
    }

    // Utility methods
    private void addAppointment(Appointment appointment) {
        if (appointmentCount == appointments.length) {
            growAppointmentArray();
        }
        appointments[appointmentCount++] = appointment;
    }

    private void removeAppointment(int index) {
        for (int i = index; i < appointmentCount - 1; i++) {
            appointments[i] = appointments[i + 1];
        }
        appointmentCount--;
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

