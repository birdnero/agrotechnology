package org.agrotechnology.Farm;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.agrotechnology.Main;
import org.agrotechnology.FarmProperty.Barn.Barn;
import org.agrotechnology.FarmProperty.Field.Field;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;
import org.agrotechnology.Worker.WorkerUtil;
import org.agrotechnology.utils.HasReport;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

/**
 * основний абстрактний клас, у нього збираються всі деталі і вже ними звідси можна оперувати
 */
public abstract class Farm implements HasReport {
    // #lab використано інкапсуляцію (її використано практично всюди)
    @Expose
    private String type;
    @Expose
    protected String location;
    @Expose
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    private static int BUDGET = 0;

    // dependence injection
    @Expose
    protected WareHouse wareHouse;
    @Expose
    protected ArrayList<Worker> workers;

    protected Farm(
            String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {

        this.type = type;
        this.name = name;
        this.location = location;
        this.wareHouse = wareHouse;
        this.workers = workers;
    }

    /**
     * 
     * @param value - сума яка додастся або відніметься
     * @return - повертає оновлений бюджет або -1 якщо грошей недостатньо
     */
    public static int updateBudget(int value) {
        if (BUDGET + value > 0) {
            BUDGET += value;
            return BUDGET;
        }
        return -1;
    }

    public static int getBudget() {
        return BUDGET;
    }

    public static void readBudget() {
        try {

            String budget = Files.readString(Path.of("src/main/java/org/agrotechnology/data/BUGET.txt"));
            Farm.updateBudget(Integer.parseInt(budget));

        } catch (IOException e) {
            terminal.errorExit("budget reading error");
        }
    }

    public static void syncBudget() {
        try (FileWriter file = new FileWriter(Path.of("src/main/java/org/agrotechnology/data/BUGET.txt").toFile())) {
            file.write((Farm.getBUDGET() + ""));
        } catch (IOException e) {
            terminal.errorExit("budget sync error");
        }
    }

    public void initProcess() {
        wareHouse.process();
        initProcessHook();
    }

    abstract protected void initProcessHook();

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName("Farm \"" + this.name + "\""));
        str.append(terminal.formatDataValue("budget", getBudget()));
        str.append(terminal.formatDataValue("location", this.location));

        str.append(this.wareHouse.report());
        if (!workers.isEmpty()) {
            str.append(terminal.formatName("WORKERS"));

            for (Worker worker : workers) {
                str.append(worker.report());
            }
        }
        str.append(useReportHook());
        str.append("\n");

        return str.toString();
    }

    protected String useReportHook() {
        return "";
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public static int getBUDGET() {
        return BUDGET;
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public String getType() {
        return type;
    }

    // ? </-- CONSOLE METHODS --/>

    private static void consoleCreateFarm() {
        terminal.CtrlC(true);
        int[] type = terminal.initOptions(new String[] { "type" }, new String[] { "Animal farm", "Plant farm" },
                new int[] { 0 }, null, null);
        String name = terminal.input("Farm name: ");
        String location = terminal.input("location: ");

        ////////////////////////////////////////////////////
        WareHouse wareHouse = WareHouse.consoleCreateWareHouse();

        ArrayList<Worker> workers = WorkerUtil.consoleCreateWorkers(name);

        if (type[1] == 0) {

            Barn barn = Barn.consoleCreateBarn();

            AnimalFarm farm = new AnimalFarm(name, location, wareHouse, workers, barn);

            Main.getFarms().add(farm);
        } else {
            Field field = Field.consoleCreateField();

            PlantFarm farm = new PlantFarm(name, location, wareHouse, workers, field);

            Main.getFarms().add(farm);
        }

        Main.sync.syncFarms(Main.getFarms());
        terminal.previewing("seccefully created!", 0);

        terminal.CtrlC(false);
    }

    private static void consoleFarmEdit(Farm farm) {
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
                        Field.consoleActions(((PlantFarm) farm));
                    }
                    break;
                case 1:
                    WareHouse.consoleActions(farm);
                    break;
                case 2:
                    WorkerUtil.consoleActions(farm);
                    break;

                default:
                    break;
            }
            Main.sync.syncFarms(Main.getFarms());
        }

    }

    /**
     * menu to do actions with farm
     */
    public static void consoleFarmsView() {
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
                consoleCreateFarm();
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
                    consoleFarmEdit(Main.getFarms().get(farmIndex));

                } else if (actionType == 2) {
                    Main.getFarms().remove(farmIndex);
                    terminal.clean();
                    Main.sync.syncFarms(Main.getFarms());
                    terminal.previewing("seccefully deleted", 0);
                }
            }
        }
    }

}
