package RUclinic;

public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    // Constructor
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    // Override toString() method
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    // Override equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Date date = (Date) obj;
        return year == date.year && month == date.month && day == date.day;
    }

    // Implement the compareTo() method
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return this.year - other.year;
        } else if (this.month != other.month) {
            return this.month - other.month;
        } else {
            return this.day - other.day;
        }
    }

    public boolean isValid() {
        if (year < 1)
            return false; // Year must be positive
        if (month < 1 || month > 12)
            return false; // Month must be between 1 and 12

        // Days in each month, with February adjusted for leap years
        int[] daysInMonth = { 0, 31, (isLeapYear() ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // Check if the day is within the valid range for the given month
        return day >= 1 && day <= daysInMonth[month];
    }

    private boolean isLeapYear() {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else {
            return year % 4 == 0;
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public static void main(String[] args) {
        // Test Case 1: Year set to 0 (Invalid year)
        Date date1 = new Date(0, 12, 25);
        System.out.println("Test Case 1 - Date1: " + date1); // Should print "12/25/0000"
        System.out.println("Is Date1 valid? " + date1.isValid()); // Should print false

        // Test Case 2: Day exceeds the maximum for April (April 31st, invalid date)
        Date date2 = new Date(2023, 4, 31);
        System.out.println("Test Case 2 - Date2: " + date2); // Should print "4/31/2023"
        System.out.println("Is Date2 valid? " + date2.isValid()); // Should print false

        // Test Case 3: Invalid month less than 1 (Month set to 0)
        Date date3 = new Date(2021, 0, 15);
        System.out.println("Test Case 3 - Date3: " + date3); // Should print "0/15/2021"
        System.out.println("Is Date3 valid? " + date3.isValid()); // Should print false

        // Test Case 4: Day set to 0 (Invalid day)
        Date date4 = new Date(2023, 3, 0);
        System.out.println("Test Case 4 - Date4: " + date4); // Should print "3/0/2023"
        System.out.println("Is Date4 valid? " + date4.isValid()); // Should print false

        // Test Case 5: Valid leap year date (February 29th, 2020)
        Date date5 = new Date(2020, 2, 29);
        System.out.println("Test Case 5 - Date5: " + date5); // Should print "2/29/2020"
        System.out.println("Is Date5 valid? " + date5.isValid()); // Should print true

        // Test Case 6: Valid date in a common year (December 25th, 2023)
        Date date6 = new Date(2023, 12, 25);
        System.out.println("Test Case 6 - Date6: " + date6); // Should print "12/25/2023"
        System.out.println("Is Date6 valid? " + date6.isValid()); // Should print true

    }
}
