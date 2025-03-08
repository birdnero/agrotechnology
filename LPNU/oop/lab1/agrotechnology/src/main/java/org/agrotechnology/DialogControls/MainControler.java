package org.agrotechnology.DialogControls;

import java.util.ArrayList;
import java.util.List;

import org.agrotechnology.Main;
import org.agrotechnology.Farm.AnimalFarm;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.Farm.PlantFarm;
import org.agrotechnology.FarmProperty.Barn.Barn;
import org.agrotechnology.FarmProperty.Barn.Animals.Animal;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.WareHouse.WareHouseWithFridge;
import org.agrotechnology.Worker.Worker;
import org.agrotechnology.Worker.WorkerUtil;
import org.agrotechnology.utils.terminal;

public final class MainControler {

    private static void farmsView() {
        List<String> actionList = new ArrayList<String>();
        actionList.add("add");

        int[] usedTo = new int[Main.getFarms().size()];

        for (int i = 0; i < Main.getFarms().size(); i++) {
            usedTo[i] = i + 1;
            actionList.add(Main.getFarms().get(i).getName());
        }

        int[] selected = terminal.initOptions(actionList.toArray(), new String[] { "view", "edit", "add" }, usedTo,
                () -> initMainChoice(), null);

        if (selected[0] == -1) {
            return;
        }

        if (selected[0] == 0) {

            terminal.CtrlC(true);
            int[] type = terminal.initOptions(new String[] { "type" }, new String[] { "Animal farm", "Plant farm" },
                    new int[] { 0 }, null, null);
            String name = terminal.input("Farm name: ");
            String location = terminal.input("location: ");
            int size = terminal.inputNumber("size (m²): ");

            int wareHouseSize = terminal.inputNumber("WareHouse size (m²): ");
            String wareHouseName = terminal.input("WareHouse name: ");

            /////////////////////////////////////////////////////
            int[] useFridge = terminal.initOptions(new String[] { "use fridge in wareHouse?" },
                    new String[] { "yes", "no" },
                    new int[] { 0 }, null, null);

            WareHouse wareHouse = new WareHouse(location, wareHouseSize, wareHouseName);
            if (useFridge[1] == 0) {
                wareHouse = new WareHouseWithFridge(location, wareHouseSize, wareHouseName);
            }
            ////////////////////////////////////////////////////

            Integer[] workersAmount = new Integer[10];
            for (int i = 0; i < 10; i++) {
                workersAmount[i] = i + 1;
            }
            int[] workersAm = terminal.initOptions(new String[] { "workers to hire" }, workersAmount, new int[] { 0 },
                    null, null);
            ArrayList<Worker> workers = new ArrayList<>();
            for (int i = 0; i < workersAm[1] + 1; i++) {
                workers = WorkerUtil.workerArrGenerator(1, name);
            }

            Farm farm = null;

            if (type[1] == 0) {

                int barnType = terminal.initOptions(Animal.getTypes(), null, () -> {
                    terminal.print(terminal.colorize("\tSelect type of animals\n", 0, true));
                });

                Barn barn = new Barn(Animal.getTypes()[barnType], 0);

                farm = new AnimalFarm(name, location, size, wareHouse, workers, null, barn);
            } else {
                farm = new PlantFarm(name, location, size, wareHouse, workers, null, null);
            }

            terminal.CtrlC(false);
            // ! make add farm

        } else {
            int actionType = selected[1];
            Farm farm = Main.getFarms().get(selected[0] - 1);

        }

        ///////////////////////////////////////////////////

    }

    private static void employeeView() {
        terminal.keyAction(8, () -> initMainChoice());

    }

    private static void productActions() {
        terminal.keyAction(8, () -> initMainChoice());

    }

    public static void initMainChoice() {
        String[] actionList = { "view farms", "view employee", "product actions", "help", "exit" };
        int selected1 = terminal.initOptions(actionList, null, () -> {
            terminal.print(terminal.colorize("\tMAIN MENU\n", 0, true));
        });

        switch (selected1) {
            case 0:// farms
                farmsView();
                break;

            case 1: // employee
                employeeView();
                break;

            case 2:// product
                productActions();
                break;

            case 3: //
                try {
                    terminal.clean();
                    terminal.cursorOnOff(false);

                    terminal.print(
                            "If you \033[31mАнтон\033[0m, please leave this place peacfully.. else if you don't, welcome here!\n\tNow you can select farms department and create something new!!\nalso click backspace when you want to get back, even now\nGood luck ;)");

                    terminal.keyAction(8, () -> {
                        initMainChoice();
                    });

                    initMainChoice();
                } catch (Exception e) {
                    terminal.print(e);
                    terminal.cursorOnOff(true);
                    terminal.errorExit("Main controler error");
                }

                break;

            case 4:
                terminal.exit();
            default:
                break;
        }
    }
}
