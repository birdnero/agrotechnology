package org.agrotechnology.WareHouse;

import java.util.ArrayList;

import org.agrotechnology.HasReport;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.utils.terminal;

public class WareHouse implements HasReport {

    protected String location;
    protected int size;
    protected ArrayList<StorageCell> storage = new ArrayList<StorageCell>();
    protected String name;
    protected double freshness;

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

        new Process(this);
    }

    public static class StorageCell {
        String type;
        int amount;
        int price;

        public StorageCell(String type, int amount, int price) {
            this.type = type;
            this.amount = amount;
            this.price = price;
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

    public double getFreshness() {
        return freshness;
    }

    public boolean putFood(String type, int amount, int price) {// !
        for (StorageCell cell : storage) {
            if (cell.type.equals(type)) {
                cell.amount += amount;
                return true;
            }
        }
        storage.add(new StorageCell(type, amount, price));
        this.freshness += 0.05;
        return true;
    }

    public boolean putFood(String type, int amount) {// !
        for (StorageCell cell : storage) {
            if (cell.type.equals(type)) {
                cell.amount += amount;
                return true;
            }
        }
        return false;
    }

    /**
     * дістати щось зі складу
     * @return повертає всю їжу що є якщо на складі не стає
     */
    public int getFood(String type, int amount) {
        for (StorageCell cell : storage) {
            if (cell.type.equals(type)) {
                if (cell.amount >= amount) {
                    cell.amount -= amount;
                    return amount;
                } else {
                    amount = cell.amount;
                    cell.amount = 0;
                    return amount;
                }
            }
        }
        return 0;
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName(this.name));
        str.append(terminal.formatDataValue("located", location));
        str.append(terminal.formatDataValue("size", size + " m²"));
        str.append("items:\n");
        for (StorageCell cell : storage) {
            str.append(terminal.formatDataValue(cell.type, cell.amount + " x " + cell.price + "$"));
        }
        return str.toString();
    }
}
