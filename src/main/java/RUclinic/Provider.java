package ruclinic;

/**
 * Enum representing the providers at the clinic.
 * Each provider has a designated location and specialty.
 * 
 * @author Paldeep Sekhon
 * @author Aditya Ponni
 */
public enum Provider {
    PATEL(Location.BRIDGEWATER, Specialty.FAMILY),
    LIM(Location.BRIDGEWATER, Specialty.PEDIATRICIAN),
    ZIMNES(Location.CLARK, Specialty.FAMILY),
    HARPER(Location.CLARK, Specialty.FAMILY),
    KAUR(Location.PRINCETON, Specialty.ALLERGIST),
    TAYLOR(Location.PISCATAWAY, Specialty.PEDIATRICIAN),
    RAMESH(Location.MORRISTOWN, Specialty.ALLERGIST),
    CERAVOLO(Location.EDISON, Specialty.PEDIATRICIAN);

    private final Location location;
    private final Specialty specialty;

    /**
     * Constructor for the Provider enum.
     * 
     * @param location  The location where the provider works.
     * @param specialty The specialty of the provider.
     */
    Provider(Location location, Specialty specialty) {
        this.location = location;
        this.specialty = specialty;
    }
     /**
     * Gets the location of the provider.
     * 
     * @return The location of the provider.
     */
    public Location getLocation() {
        return location;
    }
    /**
     * Gets the specialty of the provider.
     * 
     * @return The specialty of the provider.
     */
    public Specialty getSpecialty() {
        return specialty;
    }
     /**
     * Overrides the default toString method to provide a formatted string 
     * representing the provider, including their name, location, and specialty.
     * 
     * @return A string in the format "PROVIDER_NAME, LOCATION, SPECIALTY".
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s", this.name(), location, specialty);
    }
    /**
     * Static method that converts a string input (representing the provider's name)
     * into a Provider enum constant.
     * 
     * @param name The string representing the provider's name (e.g., "PATEL").
     * @return The corresponding Provider enum, or null if the input is invalid.
     */
    public static Provider fromString(String name) {
        try {
            return Provider.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the provider does not exist
        }
    }
}
