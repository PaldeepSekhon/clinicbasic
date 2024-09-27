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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
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
        if (year < 1) return false; // Year must be positive
        if (month < 1 || month > 12) return false; // Month must be between 1 and 12
        
        // Days in each month, with February adjusted for leap years
        int[] daysInMonth = {0, 31, (isLeapYear() ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
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

    public static void main(String[] args) {
        Date date1 = new Date(2024, 2, 29);
        System.out.println("Date1: " + date1);  // Should print "2/29/2024"
        System.out.println("Is Date1 valid? " + date1.isValid());  // Should print true
    
        Date date2 = new Date(2023, 2, 29);
        System.out.println("Date2: " + date2);  // Should print "2/29/2023"
        System.out.println("Is Date2 valid? " + date2.isValid());  // Should print false
    
        System.out.println("Comparison: " + date1.compareTo(date2)); // Should print positive number
    }
}
