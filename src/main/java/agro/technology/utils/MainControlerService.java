package agro.technology.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import agro.technology.Farms.FarmCLI;
import agro.technology.SellManager.SellCLI;
import org.springframework.stereotype.Service;

import agro.technology.utils.CLI.Colors;

@Service
public final class MainControlerService {

    private final SellCLI sellCLI;

    private final FarmCLI farmCLI;

    private final Map<String, ConsoleEditable> editableMap;

    private final CLI terminal;

    public MainControlerService(
            FarmCLI farmConsoleService,
            CLI terminal,
            List<ConsoleEditable> ediatableList,
            FarmCLI farmCLI,
            SellCLI sellCLI) {
        this.terminal = terminal;
        this.farmCLI = farmCLI;
        // ConsoleEditable::getLabel то саме, що й el->el.getLabel()
        this.editableMap = ediatableList.stream().collect(Collectors.toMap(ConsoleEditable::getLabel, el -> el));
        this.sellCLI = sellCLI;
    }

    /**
     * меню з викликом основного інтерфейсу програми
     */
    public void initMainChoice() {
        while (true) {
            String[] actionList = { "view farms", "sell", "help", "settings", "exit" };
            int selected1 = terminal.initOptions(actionList, null, () -> {
                terminal.print(terminal.colorize("\tMAIN MENU\n", Colors.PINK, true));
            });

            switch (selected1) {
                case 0:// farms
                    farmCLI.consoleFarmsView();
                    break;

                case 1:// product
                    sellCLI.sellActions();
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
                    if (selected == -1)
                        continue;

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
