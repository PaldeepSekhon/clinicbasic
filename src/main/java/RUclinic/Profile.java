package ruclinic;

/**
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */
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
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
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
        // Date objects for the profiles
        Date dob1 = new Date(1995, 5, 15);
        Date dob2 = new Date(1995, 5, 14);
        Date dob3 = new Date(1995, 5, 16);
        Date dob4 = new Date(1990, 8, 22);

        // Profiles for testing
        Profile profile1 = new Profile("Alice", "Baker", dob1);
        Profile profile2 = new Profile("Bob", "Baker", dob1); // Test Case 1 & 4
        Profile profile3 = new Profile("Alice", "Adams", dob1); // Test Case 2 & 5
        Profile profile4 = new Profile("Alice", "Baker", dob2); // Test Case 3 & 6
        Profile profile5 = new Profile("Alice", "Baker", dob3); // Test Case 6
        Profile profile6 = new Profile("Charlie", "Davis", dob4); // Test Case 7

        // Test Case 1: compareTo() returns -1 (first name comparison)
        System.out.println("Test Case 1 - Profile1 compared to Profile2: " + profile1.compareTo(profile2)); // Should
                                                                                                            // print -1

        // Test Case 2: compareTo() returns -1 (last name comparison)
        System.out.println("Test Case 2 - Profile1 compared to Profile3: " + profile1.compareTo(profile3)); // Should
                                                                                                            // print -1

        // Test Case 3: compareTo() returns -1 (date of birth comparison)
        System.out.println("Test Case 3 - Profile4 compared to Profile1: " + profile4.compareTo(profile1)); // Should
                                                                                                            // print -1

        // Test Case 4: compareTo() returns 1 (first name comparison)
        System.out.println("Test Case 4 - Profile2 compared to Profile1: " + profile2.compareTo(profile1)); // Should
                                                                                                            // print 1

        // Test Case 5: compareTo() returns 1 (last name comparison)
        System.out.println("Test Case 5 - Profile1 compared to Profile3: " + profile3.compareTo(profile1)); // Should
                                                                                                            // print 1

        // Test Case 6: compareTo() returns 1 (date of birth comparison)
        System.out.println("Test Case 6 - Profile5 compared to Profile1: " + profile5.compareTo(profile1)); // Should
                                                                                                            // print 1

        // Test Case 7: compareTo() returns 0 (profiles are identical)
        Profile profile7 = new Profile("Charlie", "Davis", dob4);
        System.out.println("Test Case 7 - Profile6 compared to Profile7: " + profile6.compareTo(profile7)); // Should
                                                                                                            // print 0

        // Testing equals() method
        System.out.println("Profile1 equals Profile2: " + profile1.equals(profile2)); // Should print false
        System.out.println("Profile1 equals Profile1: " + profile1.equals(profile1)); // Should print true

        // Testing toString() method
        System.out.println("Profile1: " + profile1); // Should print "Alice Baker 5/15/1995"

    }
}