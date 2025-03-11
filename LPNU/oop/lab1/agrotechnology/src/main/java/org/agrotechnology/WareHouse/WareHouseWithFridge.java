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
     */
    public WareHouseWithFridge(String location, int size) {
        super(WareHouseWithFridge.class.getSimpleName(), location, size);
        fridgeWithAccumulator = 10;
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
        new Process(this) {
            @Override
            public void process(WareHouse wareHOuse){
                if(wareHOuse instanceof WareHouseWithFridge){
                    WareHouseWithFridge wareHouse = ((WareHouseWithFridge) wareHOuse);
                    wareHouse.freshness += 0.01;
                    if (wareHouse.fridgeWithAccumulator > 0) {
                        wareHouse.fridgeWithAccumulator = Math.max(0, wareHouse.fridgeWithAccumulator - 0.2);
                    } 
                    
                    if(wareHouse.fridgeWithAccumulator == 0 && !wareHouse.storage.isEmpty()){
                        wareHouse.storage.removeFirst();    
                    }
                }
            }
        };
    }

    
}