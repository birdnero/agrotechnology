package org.agrotechnology.WareHouse;

import java.util.ArrayList;

import org.agrotechnology.HasReport;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

public class WareHouse implements HasReport {

    @Expose
    protected String location;
    @Expose
    protected int size;
    @Expose
    protected int capacity;
    @Expose
    protected String name;
    @Expose
    protected double freshness;
    @Expose
    protected ArrayList<StorageCell> storage = new ArrayList<StorageCell>();

    /**
     * стандартний склад
     * 
     * @param location - те саме що й у ферми
     * @param size
     * @param name     - назва/тип складу
     */
    public WareHouse(String location, int size, String name) {
        this.location = location;
        this.size = size;
        this.name = name;
        this.freshness = 1;
        this.capacity = 0;

        new Process(this);
    }


    public static class StorageCell {
        @Expose
        String type;
        @Expose
        int amount;

        public StorageCell(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public int getAmount() {
            return amount;
        }
    }

    /**
     * за 200 оновлює свіжість до 1
     */
    public boolean fullyCleanWareHouse() {// !
        if (Farm.updateBudget(-200) >= 0) {
            this.freshness = 1;
            return true;
        }
        return false;
    }

    /**
     * перевіряє вміст
     * 
     * @return -1 - якщо немає місця, amount - якщо місця достатньо, щось менше -
     *         вільне місце
     */
    public int isSpaceFor(int amount) {
        if (this.capacity + amount < this.size) {
            return amount;
        } else if (this.capacity < this.size) {
            return this.size - this.capacity;
        }
        return -1;
    }

    public int getFreeSpace(){
        return this.size - this.capacity;
    }

    public boolean putFood(String type, int amount) {
        if (this.size >= this.capacity + amount) {
            this.capacity += amount;
            for (StorageCell cell : storage) {
                if (cell.type.equals(type)) {
                    cell.amount += amount;
                    return true;
                }
            }
            storage.add(new StorageCell(type, amount));
        }
        return false;
    }

    /**
     * дістати щось зі складу
     * 
     * @return повертає всю їжу що є якщо на складі не стає
     */
    public int getFood(String type, int amount) {
        if(amount <= 0){
            return 0;
        }
        for (StorageCell cell : storage) {
            if (cell.type.equals(type)) {
                if (cell.amount >= amount) {
                    this.capacity -= amount;
                    cell.amount -= amount;
                    return amount;
                } else {
                    amount = cell.amount;
                    this.capacity -= amount;
                    cell.amount = 0;
                    return amount;
                }
            }
        }
        return 0;
    }

    public void process(){
        new Process(this);
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName(this.name));
        str.append(terminal.formatDataValue("located", location));
        str.append(terminal.formatDataValue("size", size + " m²"));
        if (!storage.isEmpty()) {
            str.append(terminal.formatName("STORAGE ITEMS"));
            for (StorageCell cell : storage) {
                str.append(terminal.formatDataValue(cell.type, cell.amount));
            }

        }
        return str.toString();
    }

    public double getFreshness() {
        return freshness;
    }

    public String getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public ArrayList<StorageCell> getStorage() {
        return storage;
    }

}
