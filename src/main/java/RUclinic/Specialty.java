package RUclinic;

public enum Specialty {
    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    private final int charge;

    Specialty(int charge) {
        this.charge = charge;
    }

    public int getCharge() {
        return charge;
    }

    @Override
    public String toString() {
        return this.name() + " - $" + charge;
    }

    public String getNameOnly() {
        return this.name();
    }
}
