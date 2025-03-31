package agro.technology.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import agro.technology.Farms.Farm;
import agro.technology.Farms.FarmCLI;
import agro.technology.Farms.FarmService;
import agro.technology.WareHouses.WareHouseService;
import org.springframework.stereotype.Service;

import agro.technology.utils.CLI.Colors;

@Service
public final class MainControlerService {

    private final WareHouseService wareHouseService;

    private final FarmCLI farmCLI;

    private final FarmService farmService;



    private final Map<String, ConsoleEditable> editableMap;

    private final CLI terminal;

    public MainControlerService(
            FarmCLI farmConsoleService,
            CLI terminal,
            List<ConsoleEditable> ediatableList, 
            FarmService farmService, 
            FarmCLI farmCLI, 
            WareHouseService wareHouseService) {
        this.terminal = terminal;
        this.farmService = farmService;
        this.farmCLI = farmCLI;
        this.wareHouseService = wareHouseService;
        // ConsoleEditable::getLabel то саме, що й el->el.getLabel()
        this.editableMap = ediatableList.stream().collect(Collectors.toMap(ConsoleEditable::getLabel, el -> el));
    }

    /**
     * меню продажу продукції з потрібної ферми
     */
    private void productActions() {
        List<Farm> farms = farmService.getFarms();
        while (true) {
            String[] farmsNames = new String[farms.size()];

            for (int i = 0; i < farms.size(); i++) {
                farmsNames[i] = farms.get(i).getName();
            }

            String[][] avaibleArr = new String[farms.size()][];

            for (int i = 0; i < avaibleArr.length; i++) {
                avaibleArr[i] = wareHouseService.getListOfStorage(farms.get(i).getWareHouse());
            }

            if(farms == null || farms.isEmpty()){
                terminal.previewing("no farms, no products..", Colors.YELLOW);
            }

            int[] selected = terminal.initOptions(farmsNames, avaibleArr, null,
                    () -> terminal.print(terminal.colorize("\tSelect Farm:\n", Colors.PINK, true)));

            if (selected[0] == -1) {
                return;
            }
            if (selected[1] == -1) {
                terminal.previewing("no products:(", Colors.RED);
            } else {

                terminal.statusMessage(
                                wareHouseService.sellFood(farms.get(selected[0])
                                .getWareHouse(), avaibleArr[selected[0]][selected[1]]),
                        "sold");

            }
        }

    }

    /**
     * меню з викликом основного інтерфейсу програми
     */
    public void initMainChoice() {
        while (true) {
            String[] actionList = { "view farms", "product actions", "help", "settings", "exit" };
            int selected1 = terminal.initOptions(actionList, null, () -> {
                terminal.print(terminal.colorize("\tMAIN MENU\n", Colors.PINK, true));
            });

            switch (selected1) {
                case 0:// farms
                    farmCLI.consoleFarmsView();
                    break;

                case 1:// product
                    productActions();
                    break;

                case 2: //
                    terminal.clean();
                    terminal.cursorOnOff(false);

                    terminal.print(
                            "If you " + Colors.RED.getColor() + "Антон" + Colors.NONE.getColor()
                                    + ", please leave this place peacfully.. else if you don't, welcome here!\n\tNow you can select farms department and create something new!!\nalso click backspace when you want to get back, even now\nGood luck ;)");

                    boolean back = false;
                    while (!back) {
                        back = terminal.keyAction(127);
                    }
                    break;

                case 3:
                    if (editableMap == null || editableMap.isEmpty())
                        terminal.previewing("not avaible, yet.. ", Colors.YELLOW);

                    String[] optList = editableMap
                            .keySet()
                            .stream()
                            .toArray(String[]::new);

                    int selected = terminal.initOptions(optList, null,
                            () -> terminal.print(terminal.optionsLabel("settings")));

                    editableMap.get(optList[selected]).consoleEditAction();
                    break;

                case 4:
                    terminal.exit();
                default:
                    break;
            }
        }
    }
}
