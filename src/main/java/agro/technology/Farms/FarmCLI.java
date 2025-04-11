package agro.technology.Farms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import agro.technology.WareHouses.WareHouse;
import agro.technology.WareHouses.WareHouseCLI;
import agro.technology.Worker.Worker;
import agro.technology.Worker.WorkerConsole;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class FarmCLI {

    private final WareHouseCLI wareHouseCLI;

    private final CLI terminal;

    private final WorkerConsole workerConsole;
    private final FarmService farmService;

    public FarmCLI(
            FarmService farmService,
            WorkerConsole workerConsole,
            CLI terminal,
            WareHouseCLI wareHouseCLI) {
        this.farmService = farmService;
        this.workerConsole = workerConsole;
        this.terminal = terminal;
        this.wareHouseCLI = wareHouseCLI;

    }

    public void viewFarm(Farm farm) {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName("Farm \"" + farm.name + "\""));
        str.append(terminal.formatDataValue("location", farm.location));

        str.append(wareHouseCLI.report(farm.wareHouse));
        if (!farm.workers.isEmpty()) {
            str.append(terminal.formatName("WORKERS"));

            for (Worker worker : farm.workers) {
                str.append(workerConsole.report(worker));
            }
        }
        str.append(farmService.searchService(farm.type).report(farm));
        str.append("\n");

        terminal.clean();
        terminal.print(str.toString());
        boolean back = false;
        while (!back) {
            back = terminal.keyAction(127);
        }
    }

    private void consoleCreateFarm() {
        terminal.CtrlC(true);
        ArrayList<String> farmTypes = new ArrayList<>();
        for (IFarmService farmS : farmService.getFarmServices())
            farmTypes.add(farmS.getFarmType());

        int type = terminal.initOptions(farmTypes.toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("select type of farm")));

        if (type == -1)
            return;

        String name = terminal.input(() -> terminal.print(terminal.optionsLabel("type name of farm")));

        if (name.equals("#back"))
            return;

        String location = terminal.input(() -> terminal.print(terminal.optionsLabel("location")));

        if (location.equals("#back"))
            return;

        ////////////////////////////////////////////////////
        WareHouse wareHouse = wareHouseCLI.consoleCreateWareHouse();

        if (wareHouse == null)
            return;

        ArrayList<Worker> workers = workerConsole.createWorkers(name);
        if (workers == null)
            return;

        Farm farm = farmService.createFarm(farmTypes.get(type), location, name, wareHouse, workers);
        if (farm == null)
            return;

        farmService.getFarms().add(farm);

        farmService.sync();

        terminal.previewing("seccefully created!", Colors.PINK);

        terminal.CtrlC(false);
    }

    private void consoleFarmEdit(Farm farm) {
        while (true) {

            ArrayList<String> options = new ArrayList<>();

            options.add(farmService.searchService(farm.getType()).specializationActionLabel());
            options.add("wareHouse");
            options.add("workers");

            int selected = terminal.initOptions(options.toArray(), null, () -> {
                terminal.print(terminal.colorize("\tACTIONS:\n", Colors.PINK, true));
            });
            if (selected == -1) {
                return;
            }

            switch (selected) {
                case 0:
                    farmService.searchService(farm.getType()).specializationAction(farm);
                    break;
                case 1:
                    wareHouseCLI.consoleActions(farm);
                    break;
                case 2:
                    workerConsole.actions(farm);
                    break;

                default:
                    break;
            }
            farmService.sync();
        }

    }

    /**
     * menu to do actions with farm
     */
    public void consoleFarmsView() {
        while (true) {

            List<String> actionList = new ArrayList<String>();
            actionList.add("add");

            int[] usedTo = new int[farmService.getFarms().size()];

            for (int i = 0; i < farmService.getFarms().size(); i++) {
                usedTo[i] = i + 1;
                actionList.add(farmService.getFarms().get(i).getName());
            }

            int[] selected = terminal.initOptions(actionList.toArray(), new String[] { "view", "actions", "delete" },
                    usedTo,
                    null, () -> terminal.print(terminal.optionsLabel("Farms")));

            if (selected[0] == -1) {
                return;
            }

            if (selected[0] == 0) {
                consoleCreateFarm();
            } else {
                int actionType = selected[1];
                int farmIndex = selected[0] - 1;

                if (actionType == 0) {
                    viewFarm(farmService.getFarms().get(farmIndex));

                } else if (actionType == 1) {
                    terminal.clean();
                    consoleFarmEdit(farmService.getFarms().get(farmIndex));

                } else if (actionType == 2) {
                    farmService.getFarms().remove(farmIndex);
                    terminal.clean();
                    farmService.sync();
                    terminal.previewing("seccefully deleted", Colors.PINK);
                }
            }
        }
    }
}
