package org.agrotechnology.WareHouse;

import org.agrotechnology.Farm.Farm;

import com.google.gson.annotations.Expose;

public class WareHouseWithFridge extends WareHouse {
    @Expose
    protected double fridgeWithAccumulator;

    public double getFridgeWithAccumulator() {
        return fridgeWithAccumulator;
    }

    /**
     * склад з спеціальним холодильником
     * 
     * @param location - те саме що й у ферми
     * @param size
     * @param name     - назва/тип складу
     */
    public WareHouseWithFridge(String location, int size) {
        super(WareHouseWithFridge.class.getSimpleName(), location, size);
        fridgeWithAccumulator = 10;
        new FridgeProcess(this);
    }

    /**
     * заряджає холодильник якщо є гроші у ферм
     * 
     * @return повертає рівень заряду холодильника
     */
    public boolean chargeFridge() {
        if (Farm.updateBudget(-20) >= 0) {
            this.fridgeWithAccumulator += 2;
            return true;
        }
        return false;
    }

    public double getFridgeAccumulatorLevel() {
        return fridgeWithAccumulator;
    }

    @Override
    public void processHook() {
        new FridgeProcess(this);
    }

    
}