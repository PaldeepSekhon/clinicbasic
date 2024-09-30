package RUclinic;

public enum Location {
    BRIDGEWATER("Somerset", "08807"),
    EDISON("Middlesex", "08817"),
    PISCATAWAY("Middlesex", "08854"),
    PRINCETON("Mercer", "08542"),
    MORRISTOWN("Morris", "07960"),
    CLARK("Union", "07066");

    private final String county;
    private final String zip;

    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    // Getter for the city (name of the enum)
    public String getCity() {
        return this.name();
    }

    // Getter for the county
    public String getCounty() {
        return county;
    }

    // Getter for the zip
    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return String.format("%s, %s %s", this.name(), county, zip); // Output: CITY, COUNTY ZIP
    }
}
