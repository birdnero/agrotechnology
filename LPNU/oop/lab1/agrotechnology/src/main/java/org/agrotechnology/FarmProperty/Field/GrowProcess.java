package org.agrotechnology.FarmProperty.Field;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class GrowProcess {

    public GrowProcess(Field field) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

        Runnable grow = () -> {
            double amount = field.sown * 0.1 * field.waterLevel;
            field.sown -= amount;
            field.ripened += amount * field.type.getDoubler();

            if (field.ripened > field.getSize() * 2) {
                field.ripened /= field.sown * 0.1;
            }
        };

        Runnable water = () -> {
            if (field.waterLevel > 0) {
                field.waterLevel = Math.max(0, field.waterLevel - 0.01);
            }
        };

        timer.scheduleAtFixedRate(grow, 5000, 50000, TimeUnit.MILLISECONDS);
        timer.scheduleAtFixedRate(water, 10000, 10000, TimeUnit.MILLISECONDS);

    }

}
