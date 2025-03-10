package org.agrotechnology.FarmProperty.Field;

import org.agrotechnology.HasReport;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.FarmProperty.Field.Plants.Plant;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

public class Field implements HasReport {
    // ? to sow - сіяти
    // ? ripened - достигло
    @Expose
    protected Plant type;
    @Expose
    private int size;
    @Expose
    protected int sown;
    @Expose
    protected double ripened;
    @Expose
    protected double waterLevel;

    /**
     * @param type - тільки назви з PlantUtils.getTypes
     * @param size - максимальна кількість росллин що може там знаходитися
     */
    public Field(String type, int size) {
        this.size = size;
        this.type = Plant.defineType(type);
        this.ripened = 0;
        this.waterLevel = 1;
        new GrowProcess(this);
    }

    
/**
 * GSON constructor
 */
    public Field(Plant type, int size, int sown, double ripened, double waterLevel) {
        this.type = type;
        this.size = size;
        this.sown = sown;
        this.ripened = ripened;
        this.waterLevel = waterLevel;
    }



    /**
     * посіяти ще
     * 
     * @return
     */
    public boolean toSow(int amount) {// !
        if (Farm.getBudget() - this.type.getBuyPrice() * amount >= 0 && this.size - this.sown >= amount) {
            Farm.updateBudget(-this.type.getBuyPrice() * amount);
            this.sown += amount;
            return true;
        }
        return false;
    }

    public boolean waterField() {// !
        if (Farm.getBudget() - 150 >= 0) {
            Farm.updateBudget(-150);
            this.waterLevel = 1;
            return true;
        }
        return false;
    }

    /**
     * 
     * @return зібрати врожай
     */
    public int takeRipened(int amount, WareHouse wareHouse) {
        int freeSpace = wareHouse.isSpaceFor(amount);
        if (freeSpace < 0) {
            return -1;
        }
        if (ripened >= amount && freeSpace == amount) {
            ripened -= amount;
            wareHouse.putFood(this.type.getName(), amount);
            return amount;
        }

        int value = (int) (this.ripened);
        if (freeSpace >= value) {
            wareHouse.putFood(this.type.getName(), value);
            this.ripened = 0;
            return value;
        }
        wareHouse.putFood(this.type.getName(), freeSpace);
        this.ripened -= freeSpace;
        return freeSpace;
    }

    public void process(){
        new GrowProcess(this);
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName("Field of " + type.getName()));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue("sown", this.sown));
        str.append(terminal.formatDataValue("has ripened", this.sown * this.ripened));
        str.append("\n");

        return str.toString();
    }

    public int getSize() {
        return size;
    }

    public Plant getType() {
        return type;
    }

    public int getSown() {
        return sown;
    }

    public double getRipened() {
        return ripened;
    }

    public double getWaterLevel() {
        return waterLevel;
    }
}
