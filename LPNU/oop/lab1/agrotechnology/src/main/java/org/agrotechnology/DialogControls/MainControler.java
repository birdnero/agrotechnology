package org.agrotechnology.DialogControls;

import java.util.ArrayList;
import java.util.List;

import org.agrotechnology.Main;
import org.agrotechnology.Farm.AnimalFarm;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.Farm.PlantFarm;
import org.agrotechnology.FarmProperty.Barn.Barn;
import org.agrotechnology.FarmProperty.Field.Field;
import org.agrotechnology.FarmProperty.Field.Plants.Plant;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.WareHouse.WareHouseWithFridge;
import org.agrotechnology.Worker.Worker;
import org.agrotechnology.Worker.WorkerUtil;
import org.agrotechnology.utils.terminal;

public final class MainControler {

    /**
     * full terminal farm creator master
     */
    private static void farmMaster() {
        terminal.CtrlC(true);
        int[] type = terminal.initOptions(new String[] { "type" }, new String[] { "Animal farm", "Plant farm" },
                new int[] { 0 }, null, null);
        String name = terminal.input("Farm name: ");
        String location = terminal.input("location: ");

        int wareHouseSize = terminal.inputNumber("WareHouse size (m²): ");
        String wareHouseName = terminal.input("WareHouse name: ");

        /////////////////////////////////////////////////////
        int[] useFridge = terminal.initOptions(new String[] { "use fridge in wareHouse?" },
                new String[] { "yes", "no" },
                new int[] { 0 }, () -> {
                }, null);

        WareHouse wareHouse = new WareHouse(location, wareHouseSize, wareHouseName);
        if (useFridge[1] == 0) {
            wareHouse = new WareHouseWithFridge(location, wareHouseSize, wareHouseName);
        }
        ////////////////////////////////////////////////////

        Integer[] workersAmount = new Integer[10];
        for (int i = 0; i < 10; i++) {
            workersAmount[i] = i + 1;
        }
        int[] workersAm = terminal.initOptions(new String[] { "workers to hire" }, workersAmount,
                new int[] { 0 },
                () -> {
                }, null);
        ArrayList<Worker> workers = new ArrayList<>();
        for (int i = 0; i < workersAm[1] + 1; i++) {
            workers = WorkerUtil.workerArrGenerator(1, name);
        }

        if (type[1] == 0) {

            Barn barn = Barn.consoleCreateBarn();

            AnimalFarm farm = new AnimalFarm(name, location, wareHouse, workers, barn);

            Main.getFarms().add(farm);
        } else {
            int fieldSize = terminal.inputNumber("Field size (m²): ");

            int fieldType = terminal.initOptions(Plant.getTypes(), () -> {
            }, () -> {
                terminal.print(terminal.colorize("\tSelect type of plant\n", 0, true));
            });

            Field field = new Field(Plant.getTypes()[fieldType], fieldSize);

            PlantFarm farm = new PlantFarm(name, location, wareHouse, workers, field);

            Main.getFarms().add(farm);
        }

        Main.sync.syncFarms(Main.getFarms());
        terminal.previewing("seccefully created!", 0);

        terminal.CtrlC(false);
    }

    private static void farmEdit(Farm farm) {
        while (true) {

            ArrayList<String> options = new ArrayList<>();

            if (farm instanceof AnimalFarm) {
                options.add("barn");
            } else if (farm instanceof PlantFarm) {
                options.add("field");
            }
            options.add("wareHouse");
            options.add("workers");

            int selected = terminal.initOptions(options.toArray(), null, () -> {
                terminal.print(terminal.colorize("\tACTIONS:\n", 0, true));
            });
            if (selected == -1) {
                return;
            }

            switch (selected) {
                case 0:
                    if (farm instanceof AnimalFarm) {
                        Barn.consoleActions((AnimalFarm) farm);
                    } else if (farm instanceof PlantFarm) {
                        fieldEdit(((PlantFarm) farm).getField());
                    }
                    break;
                case 1:
                    wareHouseEdit(farm.getWareHouse());
                    break;
                case 2:
                    workersEdit(farm.getWorkers());
                    break;

                default:
                    break;
            }
            Main.sync.syncFarms(Main.getFarms());
        }

    }

    

    private static void fieldEdit(Field field) {
        //!TODO
        // ArrayList<String> options = new ArrayList<>();
        // options.add("name");

        // options.add("wareHouse");
        // options.add("workers");

        // int selected = terminal.initOptions(options.toArray(), null, () -> {
        // terminal.print(terminal.colorize("\tEDIT:\n", 0, true));
        // });
    }

    private static void wareHouseEdit(WareHouse wareHouse) {
        //!TODO
        // ArrayList<String> options = new ArrayList<>();
        // options.add("name");

        // options.add("wareHouse");
        // options.add("workers");

        // int selected = terminal.initOptions(options.toArray(), null, () -> {
        // terminal.print(terminal.colorize("\tEDIT:\n", 0, true));
        // });
    }

    private static void workersEdit(ArrayList<Worker> workers) {
        //!TODO
        // ArrayList<String> options = new ArrayList<>();
        // options.add("name");

        // options.add("wareHouse");
        // options.add("workers");

        // int selected = terminal.initOptions(options.toArray(), null, () -> {
        // terminal.print(terminal.colorize("\tEDIT:\n", 0, true));
        // });

    }

    /**
     * menu to do actions with farm
     */
    private static void farmsView() {
        while (true) {

            List<String> actionList = new ArrayList<String>();
            actionList.add("add");

            int[] usedTo = new int[Main.getFarms().size()];

            for (int i = 0; i < Main.getFarms().size(); i++) {
                usedTo[i] = i + 1;
                actionList.add(Main.getFarms().get(i).getName());
            }

            int[] selected = terminal.initOptions(actionList.toArray(), new String[] { "view", "actions", "delete" },
                    usedTo,
                    null, null);

            if (selected[0] == -1) {
                return;
            }

            if (selected[0] == 0) {
                farmMaster();
            } else {
                int actionType = selected[1];
                int farmIndex = selected[0] - 1;

                if (actionType == 0) {
                    terminal.clean();
                    terminal.print(Main.getFarms().get(farmIndex).report());
                    boolean back = false;
                    while (!back) {
                        back = terminal.keyAction(127);
                    }

                } else if (actionType == 1) {
                    terminal.clean();
                    farmEdit(Main.getFarms().get(farmIndex));

                } else if (actionType == 2) {
                    Main.getFarms().remove(farmIndex);
                    terminal.clean();
                    Main.sync.syncFarms(Main.getFarms());
                    terminal.previewing("seccefully deleted", 0);
                }
            }
        }

        ///////////////////////////////////////////////////

    }

    private static void employeeView() {
        boolean back = false;
        while (!back) {
            back = terminal.keyAction(127);
        }

    }

    private static void productActions() {
        boolean back = false;
        while (!back) {
            back = terminal.keyAction(127);
        }

    }

    public static void initMainChoice() {
        while (true) {
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

                        boolean back = false;
                        while (!back) {
                            back = terminal.keyAction(127);
                        }

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
}
