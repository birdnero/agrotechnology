package agro.technology.Farms.Farm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import agro.technology.Farms.FarmConsoleFactoryService;
import agro.technology.Farms.FarmSyncService;
import agro.technology.WareHouses.WareHousesConsoleService;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;
import agro.technology.Worker.WorkerConsole;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class FarmConsoleService {

    private final CLI terminal;

    private final FarmConsoleFactoryService farmConsoleFactoryService;
    private final WareHousesConsoleService wareHousesConsoleService;
    private final WorkerConsole workerConsole;
    private final FarmSyncService farmSyncService;

    public FarmConsoleService(
            FarmConsoleFactoryService farmConsoleFactoryService,
            WareHousesConsoleService wareHousesConsoleService,
            WorkerConsole workerConsole,
            CLI terminal,
            FarmSyncService farmSyncService) {
        this.farmConsoleFactoryService = farmConsoleFactoryService;
        this.wareHousesConsoleService = wareHousesConsoleService;
        this.workerConsole = workerConsole;
        this.terminal = terminal;
        this.farmSyncService = farmSyncService;
    }

    private void consoleCreateFarm() {
        terminal.CtrlC(true);
        ArrayList<String> farmTypes = new ArrayList<>(farmConsoleFactoryService.getFarmsList());

        int type = terminal.initOptions(farmTypes.toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("select type of farm")));

        String name = terminal.input(() -> terminal.print(terminal.optionsLabel("type name of farm")));

        String location = terminal.input(() -> terminal.print(terminal.optionsLabel("location")));

        ////////////////////////////////////////////////////
        WareHouse wareHouse = wareHousesConsoleService.consoleCreateWareHouse();

        ArrayList<Worker> workers = workerConsole.createWorkers(name);

        farmSyncService.getFarms()
                .add(farmConsoleFactoryService.createFarm(farmTypes.get(type), location, name, wareHouse, workers));

        farmSyncService.syncFarms();

        terminal.previewing("seccefully created!", Colors.PINK);

        terminal.CtrlC(false);
    }

    private void consoleFarmEdit(Farm farm) {
        while (true) {

            ArrayList<String> options = new ArrayList<>();

            options.add(farm.getSpecializationName());
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
                    farmConsoleFactoryService.specializationAction(farm);
                    break;
                case 1:
                    wareHousesConsoleService.consoleActions(farm);
                    break;
                case 2:
                    workerConsole.actions(farm);
                    break;

                default:
                    break;
            }
            farmSyncService.syncFarms();
        }

    }

    /**
     * menu to do actions with farm
     */
    public void consoleFarmsView() {
        while (true) {

            List<String> actionList = new ArrayList<String>();
            actionList.add("add");

            int[] usedTo = new int[farmSyncService.getFarms().size()];

            for (int i = 0; i < farmSyncService.getFarms().size(); i++) {
                usedTo[i] = i + 1;
                actionList.add(farmSyncService.getFarms().get(i).getName());
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
                    terminal.print(farmSyncService.getFarms().get(farmIndex).report());
                    boolean back = false;
                    while (!back) {
                        back = terminal.keyAction(127);
                    }

                } else if (actionType == 1) {
                    terminal.clean();
                    consoleFarmEdit(farmSyncService.getFarms().get(farmIndex));

                } else if (actionType == 2) {
                    farmSyncService.getFarms().remove(farmIndex);
                    terminal.clean();
                    farmSyncService.syncFarms();
                    terminal.previewing("seccefully deleted", Colors.PINK);
                }
            }
        }
    }
}
