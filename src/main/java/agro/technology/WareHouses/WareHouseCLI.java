package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import agro.technology.Farms.Farm;
import agro.technology.Farms.FarmService;
import agro.technology.Product.Product;
import agro.technology.Product.ProductService;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class WareHouseCLI {

    private final ProductService productService;

    private final FarmService farmService;

    private final WareHouseService wareHouseService;

    private final CLI terminal;

    public WareHouseCLI(CLI terminal, WareHouseService wareHouseService, FarmService farmService,
            ProductService productService) {
        this.terminal = terminal;
        this.wareHouseService = wareHouseService;
        this.farmService = farmService;
        this.productService = productService;
    }

    public String report(WareHouse wareHouse) {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName("WareHouse"));
        str.append(terminal.formatDataValue("located", wareHouse.getLocation()));
        str.append(terminal.formatDataValue("size", wareHouse.getSize() + " m²"));
        if (!wareHouse.storage.isEmpty()) {
            str.append(terminal.formatName("storage items"));
            for (Product cell : wareHouse.storage) {
                str.append(productService.report(cell));
            }

        }
        return str.toString();
    }

    public WareHouse consoleCreateWareHouse() {
        int wareHouseSize = terminal.inputNumber("WareHouse size (m²): ");
        String location = terminal.input("WareHouse location: ");

        List<String> wareHouseTypes = new ArrayList<>();
        for (IWareHouseService iwareHouseService : wareHouseService.getWareHouseServices())
            wareHouseTypes.add(iwareHouseService.getWareHouseType());

        int type = terminal.initOptions(wareHouseTypes.toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("Select type of WareHouse")));

        if (type == -1)
            return null;

        WareHouse wareHouse = wareHouseService.create(wareHouseTypes.get(type), location, wareHouseSize);
        wareHouseService.searchWareHouseService(wareHouseTypes.get(type)).initProcesess(wareHouse);

        return wareHouse;
    }

    public void consoleActions(Farm farm) {
        WareHouse wareHouse = farm.getWareHouse();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("clean");
            options.add("transfer");

            List<String> actions = wareHouseService.searchWareHouseService(wareHouse.getType()).getActionsList();

            for (String action : actions)
                options.add(action);

            int selected = terminal.initOptions(
                    options.toArray(),
                    null,
                    () -> terminal.print(terminal.colorize("\tWAREHOUSE:\n", Colors.PINK, true)));

            switch (selected) {
                case -1:
                    return;

                case 0:
                    if (wareHouseService.fullyCleanWareHouse(wareHouse)) {
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
                wareHouseService.searchWareHouseService(wareHouse.getType()).doAction(wareHouse,
                        actions.get(selected - 2));

        }
    }

    private void consoleTransfer(Farm farm) {
        List<Farm> farms = farmService.getFarms();
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

            String[] products = wareHouseService.getListOfStorage(farm.getWareHouse());

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
                if (wareHouseService.transferFood(farm.getWareHouse(), products[selected[1]],
                        farmsLocal.get(selected[0]).getWareHouse())) {
                    terminal.statusMessage(true, "transfered");
                } else {
                    terminal.previewing("no free space :(", Colors.RED);
                }

            }
            farmService.sync();

        }
    }
}
