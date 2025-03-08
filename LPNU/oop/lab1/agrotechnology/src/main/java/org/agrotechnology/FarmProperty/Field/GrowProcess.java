package org.agrotechnology.FarmProperty.Field;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class GrowProcess {

    public GrowProcess(Field field) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

        Runnable grow = () -> {
            if (field.ripenedPercent != 1) {
                field.ripenedPercent = Math.min(1, field.ripenedPercent + 0.002 * field.waterLevel);
            }
        };

        Runnable water = () -> {
            if (field.waterLevel > 0) {
                field.waterLevel = Math.max(0, field.waterLevel - 0.01);
            }
        };

        timer.scheduleAtFixedRate(grow, 5000, 5000, TimeUnit.MILLISECONDS);
        timer.scheduleAtFixedRate(water, 10000, 10000, TimeUnit.MILLISECONDS);

    }

}
