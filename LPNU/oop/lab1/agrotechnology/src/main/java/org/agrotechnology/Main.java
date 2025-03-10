package org.agrotechnology;

import java.util.List;
 
import org.agrotechnology.DialogControls.MainControler;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.utils.terminal;

public class Main {
    private static List<Farm> farms;
    public static DataManager sync;

    public static void main(String[] args) {
        terminal.CtrlC(false);

        sync = new DataManager();
        farms = sync.loadFarms();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            sync.syncFarms(farms);
        }));

        terminal.previewing("Agrotechnology project", 0);

        MainControler.initMainChoice();
        // terminal.print(farms.get(0).report());


        // farms.add(new AnimalFarm("My fucking farm", "my fucking room", 2000, new WareHouse("my fucking room", 340, "fuck"), WorkerUtil.workerArrGenerator(2, "My fucking farm"), new Barn("Chiken", 245)));
        // farms.get(0).getWareHouse().putFood("Corn", 2);
        // sync.syncFarms(farms);

    }

    public static List<Farm> getFarms() {
        return farms;
    }
}