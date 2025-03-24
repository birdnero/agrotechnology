package agro.technology.Farms.PlantFarm.Field;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;

final class GrowProcess {

    private PlantService plantService;
    
        @Autowired
        public void setPlantService(PlantService plantService) {
            this.plantService = plantService;
    }

    public GrowProcess(Field field) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

        Runnable grow = () -> {
            double amount = field.sown * 0.1 * field.waterLevel;
            field.sown -= amount;
            field.ripened += amount * plantService.getPlant(field.type).getDabler();

            if (field.ripened > field.getSize() * 2) {
                field.ripened /= field.sown * 0.1;
            }
        };

        Runnable water = () -> {
            if (field.waterLevel > 0) {
                field.waterLevel = Math.max(0, field.waterLevel - 0.01);
            }
        };

        timer.scheduleAtFixedRate(grow, 50_000, 50_000, TimeUnit.MILLISECONDS);
        timer.scheduleAtFixedRate(water, 100_000, 100_000, TimeUnit.MILLISECONDS);

    }

}
