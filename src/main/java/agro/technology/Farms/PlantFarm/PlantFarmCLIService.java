package agro.technology.Farms.PlantFarm;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import agro.technology.Farms.Farm;
import agro.technology.Farms.IFarmService;
import agro.technology.Farms.PlantFarm.Field.Field;
import agro.technology.Farms.PlantFarm.Field.FieldCLI;
import agro.technology.Farms.PlantFarm.Field.FieldService;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class PlantFarmCLIService implements IFarmService {

    private final PlantService plantService;

    private final FieldService fieldService;

    private final FieldCLI fieldCLI;
    private final CLI terminal;

    public PlantFarmCLIService(CLI cli, FieldCLI fieldCLI, FieldService fieldService, PlantService plantService) {
        this.fieldCLI = fieldCLI;
        this.fieldService = fieldService;
        this.terminal = cli;
        this.plantService = plantService;
    }

    @Override
    public Farm createViaCLI(String type, String location, String name, WareHouse wareHouse,
            ArrayList<Worker> workers) {
        Field field = fieldCLI.create();
        if (field == null)
            return null;
        return new PlantFarm(name, location, wareHouse, workers, field);
    }

    @Override
    public String getFarmType() {
        return PlantFarm.type;
    }

    @Override
    public void initProcesess(Farm farm) {
        if (farm instanceof PlantFarm)
            fieldService.process(((PlantFarm) farm).getField());
    }

    @Override
    public Farm load(String type, String location, String name, WareHouse wareHouse, ArrayList<Worker> workers,
            JsonObject jsonData) {
        if (type.equals(getFarmType())) {

            Field field = fieldService.load(jsonData.getAsJsonObject("field"));
            return new PlantFarm(name, location, wareHouse, workers, field);

        } else
            terminal.previewing(this.getClass().getSimpleName() + ": different types when load", Colors.RED);

        return null;
    }

    @Override
    public void specializationAction(Farm farm) {
        Field field = ((PlantFarm) farm).getField();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("to sow " + plantService.getPlant(field.getType()).getName());
            options.add("to water");
            options.add("harvest");

            int canSow = fieldService.canSow(field);
            String[] sowArr = new String[canSow];
            for (int i = 0; i < canSow; i++) {
                sowArr[i] = (i + 1) + "";
            }
            
            Supplier<String[][]> valuesOptionsSupply = () -> {

                return new String[][] {
                        sowArr,
                        new String[] { (int)(field.getWaterLevel() * 100) + "%" },
                        new String[] { field.getRipened() + "" }
                };
            };

            Supplier<String[]> optionsSupply = () -> options.toArray(String[]::new);

            int[] selected = terminal.initHotOptions(
                    optionsSupply,
                    valuesOptionsSupply,
                    null,
                    () -> terminal
                            .print(terminal.colorize(
                                    "\t" + plantService.getPlant(field.getType()).getName().toUpperCase() + " FIELD:\n",
                                    Colors.PINK,
                                    true)));

            if (selected[0] == -1) {
                return;
            }
            switch (selected[0]) {
                case 0:

                    if (selected[1] == -1) {
                        terminal.previewing("no unough money or place :(", Colors.RED);
                    } else {
                        terminal.statusMessage(fieldService.toSow(field, selected[1] + 1), "sowed");
                    }

                    break;

                case 1:
                    if (fieldService.waterField(field)) {
                        terminal.statusMessage(true, "watered");
                    } else {
                        terminal.previewing("no unough money :(", Colors.RED);
                    }
                    break;

                case 2:
                    int amount = fieldService.canTakeRipened(field, farm.getWareHouse());
                    if (amount <= 0) {
                        terminal.previewing("no production yet :(", Colors.PINK);
                    } else {
                        terminal.statusMessage(fieldService.takeRipened(field, amount, farm.getWareHouse()), "taken");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public String report(Farm farm) {
        return fieldCLI.report(((PlantFarm) farm).getField());
    }

    @Override
    public String specializationActionLabel() {
        return "field";
    }

}
