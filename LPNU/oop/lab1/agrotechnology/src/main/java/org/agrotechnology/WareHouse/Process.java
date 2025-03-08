package org.agrotechnology.WareHouse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Process {
    public Process(WareHouse wareHouse) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

        Runnable fridge = () -> {
            wareHouse.freshness = Math.max(0, wareHouse.freshness - 0.03);

            if (wareHouse.freshness <= 0 && !wareHouse.storage.isEmpty()) {
                wareHouse.storage.removeFirst();
            }
        };

        timer.scheduleAtFixedRate(fridge, 5000, 50000, TimeUnit.MILLISECONDS);
    }

}
