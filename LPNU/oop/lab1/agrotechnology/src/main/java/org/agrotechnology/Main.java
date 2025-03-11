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

        Farm.readBudget();
        sync = new DataManager();
        farms = sync.loadFarms();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sync.syncFarms(farms);
            Farm.syncBudget();
        }));

        terminal.previewing("Agrotechnology project", 0);

        MainControler.initMainChoice();
    }

    

    public static List<Farm> getFarms() {
        return farms;
    }
}