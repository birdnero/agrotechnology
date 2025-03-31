package agro.technology.WareHouses;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * абстрактний клас для виклику незалежних процесів
 */
// #lab використано абстрактний клас
public abstract class Procesess {
    public Procesess(WareHouse wareHouse) {

        ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

        Runnable processAction = () -> {
            process(wareHouse);
        };

        timer.scheduleAtFixedRate(processAction, 50000, 500000, TimeUnit.MILLISECONDS);
    }

    abstract public void process(WareHouse wareHouse);

}
