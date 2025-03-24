package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import agro.technology.Farms.FarmSyncService;
import agro.technology.Farms.Farm.Farm;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.WareHouses.WareHouseWithFridge.WareHouseWithFridge;
import agro.technology.utils.terminal;

@Service
public class WareHousesConsoleService {

    private final terminal terminal;

    private final WareHousesFactoryService wareHousesFactoryService;
    private final FarmSyncService farmSyncService;

    public WareHousesConsoleService(WareHousesFactoryService wareHousesFactoryService, terminal terminal, FarmSyncService farmSyncService) {
        this.wareHousesFactoryService = wareHousesFactoryService;
        this.terminal = terminal;
        this.farmSyncService = farmSyncService;
    }

    public WareHouse consoleCreateWareHouse() {
        int wareHouseSize = terminal.inputNumber("WareHouse size (m²): ");
        String location = terminal.input("WareHouse location: ");

        /////////////////////////////////////////////////////
        int type = terminal.initOptions(wareHousesFactoryService.getWareHousesList().toArray(), () -> {
        }, () -> terminal.optionsLabel("Select type of WareHouse"));

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
                    () -> terminal.print(terminal.colorize("\tWAREHOUSE:\n", 0, true)));

            switch (selected) {
                case -1:
                    return;

                case 0:
                    if (wareHouse.fullyCleanWareHouse()) {
                        terminal.previewing("seccefully cleaned", 2);
                    } else {
                        terminal.previewing("no unough money :(", 1);
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
                terminal.previewing("no products:(", 1);
                return;
            }

            int[] selected = terminal.initOptions(farmsNames, products, usedTo, null,
                    () -> terminal.print(terminal.colorize("\tSelect WareHouse :\n", 0, true)));

            if (selected[0] == -1) {
                return;
            }

            if (selected[1] == -1) {
                terminal.previewing("no products:(", 1);
            } else {
                if (farm.getWareHouse().transferFood(products[selected[1]],
                        farmsLocal.get(selected[0]).getWareHouse())) {
                    terminal.statusMessage(true, "transfered");
                } else {
                    terminal.previewing("no free space :(", 1);
                }

            }

        }
    }

}
