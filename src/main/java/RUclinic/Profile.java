package RUclinic;


public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    // Constructor
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    // Override equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        {
        return true;
        }
        if (obj == null || getClass() != obj.getClass())
        { return false;
        }
        Profile profile = (Profile) obj;
        return fname.equals(profile.fname) &&
               lname.equals(profile.lname) &&
               dob.equals(profile.dob);
    }

    // Override compareTo() method
    @Override
    public int compareTo(Profile other) {
        int lastNameComparison = this.lname.compareTo(other.lname);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }

        int firstNameComparison = this.fname.compareTo(other.fname);
        if (firstNameComparison != 0) {
            return firstNameComparison;
        }

        return this.dob.compareTo(other.dob);
    }

    // Override toString() method
    @Override
    public String toString() {
        return String.format("%s %s %s", fname, lname, dob);
    }

    
    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public Date getDob() {
        return dob;
    }

    // Testbed main() method
    public static void main(String[] args) {
        // Create some Date objects for testing
        Date dob1 = new Date(1989, 12, 13);
        Date dob2 = new Date(1990, 7, 21);
        Date dob3 = new Date(1985, 3, 5);

        // Create some Profile objects for testing
        Profile profile1 = new Profile("John", "Doe", dob1);
        Profile profile2 = new Profile("John", "Doe", dob2);
        Profile profile3 = new Profile("Jane", "Doe", dob1);
        Profile profile4 = new Profile("Alice", "Smith", dob3);

        // Test the equals() method
        System.out.println("Profile1 equals Profile2: " + profile1.equals(profile2)); // Should print false
        System.out.println("Profile1 equals Profile3: " + profile1.equals(profile3)); // Should print false
        System.out.println("Profile1 equals Profile1: " + profile1.equals(profile1)); // Should print true

        // Test the compareTo() method
        System.out.println("Profile1 compared to Profile2: " + profile1.compareTo(profile2)); // Should print a negative number (because dob1 < dob2)
        System.out.println("Profile1 compared to Profile3: " + profile1.compareTo(profile3)); // Should print a positive number (because "John" > "Jane")
        System.out.println("Profile1 compared to Profile4: " + profile1.compareTo(profile4)); // Should print a negative number (because "Doe" < "Smith")

        // Test the toString() method
        System.out.println(profile1); // Should print "John Doe 12/13/1989"
    }
}