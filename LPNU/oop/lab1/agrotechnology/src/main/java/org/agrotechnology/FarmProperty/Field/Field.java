package org.agrotechnology.FarmProperty.Field;

import org.agrotechnology.HasReport;
import org.agrotechnology.utils.terminal;

public class Field implements HasReport {
    // ? to sow - сіяти
    // ? ripened - достигло

    private int size;
    private int sown;
    protected double ripenedPercent;
    protected double waterLevel;
    private String typeOfProduct;

    public Field(int size, String typeOfProduct) {
        this.size = size;
        this.typeOfProduct = typeOfProduct;
        this.ripenedPercent = 0;
        this.waterLevel = 0.5;
        new GrowProcess(this);
    }

    public boolean toSow(int amount, String type) {
        if (this.size - this.sown >= amount && this.typeOfProduct.equals(type)) {
            this.sown += amount;
            return true;
        }
        return false;
    }

    public int getProperty(double percent) {
        if (ripenedPercent >= percent) {
            int amount = (int) (this.sown * percent);
            this.sown -= this.sown * percent;
            this.ripenedPercent -= percent;
            return amount;
        }
        int amount = this.sown;
        this.sown = 0;
        this.ripenedPercent = 0;
        return amount;

    }

    public int getProperty(int amount) {
        int avaible = (int) (this.sown * this.ripenedPercent);
        if (avaible >= amount) {
            int last = avaible - amount;
            this.sown -= amount;
            this.ripenedPercent = last / this.sown;
            return amount;
        }
        this.sown = 0;
        this.ripenedPercent = 0;
        return avaible;
    }


    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName("Field of " + typeOfProduct));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue("sown", this.sown));
        str.append(terminal.formatDataValue("has ripened", this.sown * this.ripenedPercent));
        str.append("\n");

        return null;
    }

}
