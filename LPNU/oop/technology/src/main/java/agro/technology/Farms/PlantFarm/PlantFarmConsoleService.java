package agro.technology.Farms.PlantFarm;

import java.util.ArrayList;
import agro.technology.utils.terminal;
import org.springframework.stereotype.Service;

import agro.technology.Farms.IFarmConsoleFactory;
import agro.technology.Farms.Farm.Farm;
import agro.technology.Farms.PlantFarm.Field.Field;
import agro.technology.Farms.PlantFarm.Field.FieldConsoleService;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;

@Service
public class PlantFarmConsoleService implements IFarmConsoleFactory {

    private final terminal terminal;

    private final PlantService plantService;

    private final FieldConsoleService fieldConsoleService;

    public PlantFarmConsoleService(FieldConsoleService fieldConsoleService, PlantService plantService, terminal terminal) {
        this.fieldConsoleService = fieldConsoleService;
        this.plantService = plantService;
        this.terminal = terminal;
    }

    @Override
    public Farm create(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {
        Field field = fieldConsoleService.create();
        return new PlantFarm(name, location, wareHouse, workers, field);
    }

    @Override
    public String getType() {
        return "Farm of plants";
    }

    @Override
    public void specializationAction(Farm farm) {
        Field field = ((PlantFarm) farm).getField();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("to sow " + plantService.getPlant(field.getType()).getName());
            options.add("to water");
            options.add("harvest");

            int canSow = field.canSow();
            String[] sowArr = new String[canSow];
            for (int i = 0; i < canSow; i++) {
                sowArr[i] = (i + 1) + "";
            }

            String[][] secondOption = {
                    sowArr,
                    null,
                    null
            };

            int[] selected = terminal.initOptions(
                    options.toArray(),
                    secondOption,
                    null,
                    () -> terminal
                            .print(terminal.colorize(
                                    "\t" + plantService.getPlant(field.getType()).getName().toUpperCase() + " FIELD:\n", 0,
                                    true)));

            if (selected[0] == -1) {
                return;
            }
            switch (selected[0]) {
                case 0:

                    if (selected[1] == -1) {
                        terminal.previewing("no unough money or place :(", 1);
                    } else {
                        terminal.statusMessage(field.toSow(selected[1] + 1), "sowed");
                    }

                    break;

                case 1:
                    if (field.waterField()) {
                        terminal.statusMessage(true, "watered");
                    } else {
                        terminal.previewing("no unough money :(", 1);
                    }
                    break;

                case 2:
                    int amount = field.canTakeRipened(farm.getWareHouse());
                    if (amount <= 0) {
                        terminal.previewing("no production yet :(", 1);
                    } else {
                        terminal.statusMessage(field.takeRipened(amount, farm.getWareHouse()), "taken");
                    }
                    break;

                default:
                    break;
            }
        }
    }

}
