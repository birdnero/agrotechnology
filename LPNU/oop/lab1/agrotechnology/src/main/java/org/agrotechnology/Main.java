package org.agrotechnology;

import java.util.List;

import org.agrotechnology.DialogControls.MainControler;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.utils.terminal;

public class Main {
    private static List<Farm> farms;

    public static void main(String[] args) {
        terminal.CtrlC(false);

        DataManager sync = new DataManager();
        farms = sync.loadFarms();

        terminal.previewing("Agrotechnology project", 0);

        MainControler.initMainChoice();

    }

    public static List<Farm> getFarms() {
        return farms;
    }
}