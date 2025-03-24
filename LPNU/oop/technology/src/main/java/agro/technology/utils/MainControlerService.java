package agro.technology.utils;

import java.util.List;
import agro.technology.Farms.FarmSyncService;
import org.springframework.stereotype.Service;
import agro.technology.Farms.Farm.FarmConsoleService;
import agro.technology.Farms.Farm.Farm;

@Service
public final class MainControlerService {

    private final FarmSyncService farmSyncService;

    private final FarmConsoleService farmConsoleService;

    private final terminal terminal;

    public MainControlerService(FarmConsoleService farmConsoleService, terminal terminal, FarmSyncService farmSyncService){
        this.terminal = terminal;
        this.farmConsoleService = farmConsoleService;
        this.farmSyncService = farmSyncService;
    }

    /**
     * меню продажу продукції з потрібної ферми
     */
    private void productActions() {
        List<Farm> farms = farmSyncService.getFarms();
        while (true) {
            String[] farmsNames = new String[farms.size()];

            for (int i = 0; i < farms.size(); i++) {
                farmsNames[i] = farms.get(i).getName();
            }

            String[][] avaibleArr = new String[farms.size()][];

            for (int i = 0; i < avaibleArr.length; i++) {
                avaibleArr[i] = farms.get(i).getWareHouse().getListOfStorage();
            }

            int[] selected = terminal.initOptions(farmsNames, avaibleArr, null,
                    () -> terminal.print(terminal.colorize("\tSelect Farm:\n", 0, true)));
            
            if (selected[0] == -1) {
                return;
            }
            if (selected[1] == -1) {
                terminal.previewing("no products:(", 1);
            } else {

                terminal.statusMessage(
                        farms.get(selected[0])
                                .getWareHouse()
                                .sellFood(avaibleArr[selected[0]][selected[1]]),
                        "sold");

            }
        }

    }

    /**
     * меню з викликом основного інтерфейсу програми
     */
    public void initMainChoice() {
        while (true) {
            String[] actionList = { "view farms", "product actions", "help", "exit" };
            int selected1 = terminal.initOptions(actionList, null, () -> {
                terminal.print(terminal.colorize("\tMAIN MENU\n", 0, true));
            });

            switch (selected1) {
                case 0:// farms
                    farmConsoleService.consoleFarmsView();
                    break;

                case 1:// product
                    productActions();
                    break;

                case 2: //
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

                case 3:
                    terminal.exit();
                default:
                    break;
            }
        }
    }
}
