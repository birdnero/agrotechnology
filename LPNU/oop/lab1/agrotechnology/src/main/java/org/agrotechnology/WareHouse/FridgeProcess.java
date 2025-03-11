package org.agrotechnology.WareHouse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class FridgeProcess {

    public FridgeProcess(WareHouseWithFridge wareHouse) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

        Runnable fridge = () -> {
            wareHouse.freshness += 0.01;
            if (wareHouse.fridgeWithAccumulator > 0) {
                wareHouse.fridgeWithAccumulator = Math.max(0, wareHouse.fridgeWithAccumulator - 0.2);
            } 

            if(wareHouse.fridgeWithAccumulator == 0 && !wareHouse.storage.isEmpty()){
                wareHouse.storage.removeFirst();    
            }
        };

        timer.scheduleAtFixedRate(fridge, 50000, 50000, TimeUnit.MILLISECONDS);
    }

    

}
