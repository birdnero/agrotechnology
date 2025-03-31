package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import agro.technology.Farms.FarmSyncService;
import agro.technology.Farms.Farm.Farm;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.WareHouses.WareHouseWithFridge.WareHouseWithFridge;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class WareHousesConsoleService {

    private final CLI terminal;

    private final WareHousesFactoryService wareHousesFactoryService;
    private final FarmSyncService farmSyncService;

    public WareHousesConsoleService(WareHousesFactoryService wareHousesFactoryService, CLI terminal, FarmSyncService farmSyncService) {
        this.wareHousesFactoryService = wareHousesFactoryService;
        this.terminal = terminal;
        this.farmSyncService = farmSyncService;
    }

    public WareHouse consoleCreateWareHouse() {
        int wareHouseSize = terminal.inputNumber("WareHouse size (m²): ");
        String location = terminal.input("WareHouse location: ");

        /////////////////////////////////////////////////////
        int type = terminal.initOptions(wareHousesFactoryService.getWareHousesList().toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("Select type of WareHouse")));

        return wareHousesFactoryService.createWareHouse(wareHousesFactoryService.getWareHousesList().get(type),
                location, wareHouseSize);
    }

    public void consoleActions(Farm farm) {
        WareHouse wareHouse = farm.getWareHouse();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("clean");
            options.add("transfer");

            List<String> actions = wareHousesFactoryService.getActions(farm.getType());

            for (String action : actions)
                options.add(action);

            if (wareHouse instanceof WareHouseWithFridge) {
                options.add("charge fridge");
            }

            int selected = terminal.initOptions(
                    options.toArray(),
                    null,
                    () -> terminal.print(terminal.colorize("\tWAREHOUSE:\n", Colors.PINK, true)));

            switch (selected) {
                case -1:
                    return;

                case 0:
                    if (wareHouse.fullyCleanWareHouse()) {
                        terminal.previewing("seccefully cleaned", Colors.GREEN);
                    } else {
                        terminal.previewing("no unough money :(", Colors.RED);
                    }
                    break;
                case 1:
                    consoleTransfer(farm);

                default:
                    break;
            }

            if (selected > 1)
                wareHousesFactoryService.takeAction(wareHouse, actions.get(selected - 2));

        }
    }

    private void consoleTransfer(Farm farm) {
        List<Farm> farms = farmSyncService.getFarms();
        while (true) {
            ArrayList<Farm> farmsLocal = new ArrayList<>();

            for (int i = 0; i < farms.size(); i++) {
                if (!farms.get(i).getName().equals(farm.getName())) {
                    farmsLocal.add(farms.get(i));
                }
            }
            String[] farmsNames = new String[farmsLocal.size()];

            for (int i = 0; i < farmsLocal.size(); i++) {
                farmsNames[i] = farmsLocal.get(i).getName();
            }

            String[] products = farm.getWareHouse().getListOfStorage();
            int[] usedTo = new int[farmsNames.length];
            for (int i = 0; i < usedTo.length; i++) {
                usedTo[i] = i;
            }

            if (products.length == 0) {
                terminal.previewing("no products:(", Colors.RED);
                return;
            }

            int[] selected = terminal.initOptions(farmsNames, products, usedTo, null,
                    () -> terminal.print(terminal.colorize("\tSelect WareHouse :\n", Colors.PINK, true)));

            if (selected[0] == -1) {
                return;
            }

            if (selected[1] == -1) {
                terminal.previewing("no products:(", Colors.PINK);
            } else {
                if (farm.getWareHouse().transferFood(products[selected[1]],
                        farmsLocal.get(selected[0]).getWareHouse())) {
                    terminal.statusMessage(true, "transfered");
                } else {
                    terminal.previewing("no free space :(", Colors.RED);
                }

            }

        }
    }

}
