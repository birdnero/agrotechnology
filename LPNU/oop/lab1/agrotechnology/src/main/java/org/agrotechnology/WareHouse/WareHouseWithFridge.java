package org.agrotechnology.WareHouse;

import org.agrotechnology.Farm.Farm;

public class WareHouseWithFridge extends WareHouse {
    protected double fridgeWithAccumulator;

    /**
     * склад з спеціальним холодильником
     * 
     * @param location - те саме що й у ферми
     * @param size
     * @param name     - назва/тип складу
     */
    public WareHouseWithFridge(String location, int size, String name) {
        super(location, size, name);
        fridgeWithAccumulator = 10;
        new FridgeProcess(this);
    }

    /**
     * заряджає холодильник якщо є гроші у ферм
     * 
     * @return повертає рівень заряду холодильника
     */
    public double chargeFridge() {
        if (Farm.updateBudget(-20) >= 0) {
            this.fridgeWithAccumulator += 2;
        }
        return this.fridgeWithAccumulator;
    }

    public double getFridgeAccumulatorLevel() {
        return fridgeWithAccumulator;
    }

}