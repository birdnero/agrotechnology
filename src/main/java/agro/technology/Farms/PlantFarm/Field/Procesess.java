package agro.technology.Farms.PlantFarm.Field;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;

final class Procesess {


    public Procesess(Field field, PlantService plantService) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

        Runnable grow = () -> {
            double amount = field.sown * 0.1 * field.waterLevel;
            field.sown -= amount;
            field.ripened += amount * plantService.getPlant(field.getType()).getDabler();

            if (field.ripened > field.getSize() * 2) {
                field.ripened /= field.sown * 0.1;
            }
        };

        Runnable water = () -> {
            if (field.waterLevel > 0) {
                field.waterLevel = Math.max(0, field.waterLevel - 0.01);
            }
        };

        timer.scheduleAtFixedRate(grow, 15_000, 15_000, TimeUnit.MILLISECONDS);
        timer.scheduleAtFixedRate(water, 10_000, 10_000, TimeUnit.MILLISECONDS);

    }

}
