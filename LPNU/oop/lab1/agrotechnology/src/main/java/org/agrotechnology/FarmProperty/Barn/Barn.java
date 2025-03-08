package org.agrotechnology.FarmProperty.Barn;

import org.agrotechnology.HasReport;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.FarmProperty.Barn.Animals.Animal;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.utils.terminal;

public class Barn implements HasReport {

    private Animal type;
    private int size;
    protected int animalsAmount;
    protected int feedAmount;

    /**
     * 
     * @param type - тільки назви класів тварин інтерфейсу Animal.defineType
     * @param size - максимальна кількість тварин що може там знаходитися
     */
    public Barn(String type, int size) {
        this.type = Animal.defineType(type);
        this.size = size;
        this.animalsAmount = 0;
        this.feedAmount = 10;

        new Processes(this);
    }

    public boolean addAnimal(int amount) {//!
        if(Farm.getBudget() - this.type.getPrice() * amount >= 0 && this.size - this.animalsAmount >= 1){
            Farm.updateBudget(-this.type.getPrice() * amount);
            this.animalsAmount+= amount;
            return true;
        }
        return false;
    }

    /**
     * @return повертає feedAmount
     */
    public int putFeed(int amount, String type, WareHouse wareHouse){//!
        if(amount > this.animalsAmount * 32){
            amount = this.animalsAmount * 32;
        }
        int feed = wareHouse.getFood(type, amount);
        this.feedAmount += feed;
        return this.feedAmount;
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(this.type.getName() + " barn"));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue(this.type.getName() + "s amount", this.animalsAmount));

        str.append("\n");

        return str.toString();
    }

}
