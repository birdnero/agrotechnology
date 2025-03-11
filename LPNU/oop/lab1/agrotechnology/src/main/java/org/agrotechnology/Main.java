package org.agrotechnology;

import java.util.List;

import org.agrotechnology.Farm.Farm;
import org.agrotechnology.utils.DataManager;
import org.agrotechnology.utils.MainControler;
import org.agrotechnology.utils.terminal;

public class Main {
    private static List<Farm> farms;
    public static DataManager sync;

    public static void main(String[] args) {
        //відключення курсору
        terminal.CtrlC(false);

        //зчитування бюджету
        Farm.readBudget();

        // клас для роботи з збереженням/завантаженням
        sync = new DataManager();
        //завантаження збереної версії даних
        farms = sync.loadFarms();

        //фігня щоб перед виходом (коректним) все збереглося
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sync.syncFarms(farms);
            Farm.syncBudget();
        }));

        //інтро
        terminal.previewing("Agrotechnology project", 0);

        //Ініціалізація основного меню
        MainControler.initMainChoice();
    }

    

    public static List<Farm> getFarms() {
        return farms;
    }
}