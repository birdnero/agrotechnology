package agro.technology.Farms.AnimaFarm;

import java.util.ArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import agro.technology.Farms.Farm;
import agro.technology.Farms.FarmService;
import agro.technology.Farms.IFarmService;
import agro.technology.Farms.AnimaFarm.Barn.Barn;
import agro.technology.Farms.AnimaFarm.Barn.BarnCLI;
import agro.technology.Farms.AnimaFarm.Barn.BarnService;
import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class AnimalFarmCLIService implements IFarmService {

    private final FarmService farmService;

    private final BarnService barnService;

    private final BarnCLI barnCLI;
    private final CLI terminal;
    private final AnimalService animalService;

    public AnimalFarmCLIService(BarnCLI barnCLI, CLI cli, AnimalService animalService, BarnService barnService,
            @Lazy FarmService farmService) {
        this.barnCLI = barnCLI;
        this.terminal = cli;
        this.animalService = animalService;
        this.barnService = barnService;
        this.farmService = farmService;
    }

    @Override
    public Farm createViaCLI(String type, String location, String name, WareHouse wareHouse,
            ArrayList<Worker> workers) {
        Barn barn = barnCLI.create();
        if (barn == null)
            return null;
        return new AnimalFarm(name, location, wareHouse, workers, barn);
    }

    @Override
    public String getFarmType() {
        return AnimalFarm.type;
    }

    @Override
    public void initProcesess(Farm farm) {
        if (farm instanceof AnimalFarm)
            barnService.process(((AnimalFarm) farm).getBarn());
    }

    @Override
    public Farm load(String type, String location, String name, WareHouse wareHouse, ArrayList<Worker> workers,
            JsonObject jsonData) {
        if (type.equals(getFarmType())) {

            Barn barn = barnService.load(jsonData.getAsJsonObject("barn"));
            return new AnimalFarm(name, location, wareHouse, workers, barn);

        } else
            terminal.previewing(this.getClass().getSimpleName() + ": different types when load", Colors.RED);

        return null;
    }

    @Override
    public String specializationActionLabel() {
        return "barn";
    }

    @Override
    public void specializationAction(Farm farm) {
        Barn barn = ((AnimalFarm) farm).getBarn();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("add " + barn.getType());
            options.add("put feed");
            options.add("get production");

            int canAddAnimals = barnService.canAddAnimals(barn);
            String[] addArr = new String[canAddAnimals];
            for (int i = 0; i < canAddAnimals; i++) {
                addArr[i] = (i + 1) + "";
            }

            int arrSize = barnService.canGetProduction(barn, farm.getWareHouse());
            String[] getProductArr = new String[arrSize];
            for (int i = 0; i < arrSize; i++) {
                getProductArr[i] = (i + 1) + "";
            }

            String[][] secondOption = {
                    addArr,
                    null,
                    getProductArr
            };

            int[] selected = terminal.initOptions(
                    options.toArray(),
                    secondOption,
                    null,
                    () -> terminal.print(terminal.colorize("\tBARN:\n", Colors.PINK, true)));

            if (selected[0] == -1) {
                return;
            }
            switch (selected[0]) {
                case 0:

                    if (selected[1] == -1) {
                        terminal.previewing("no unough money or room:(", Colors.RED);
                    } else {
                        terminal.statusMessage(barnService.addAnimal(barn, selected[1] + 1), "added");
                    }

                    break;

                case 1:
                    consolePutFood((AnimalFarm) farm);
                    break;

                case 2:
                    if (selected[1] == -1) {
                        terminal.previewing("no production yet :(", Colors.RED);
                    } else {
                        terminal.statusMessage(barnService.getProduction(barn, selected[1] + 1, farm.getWareHouse()),
                                "taked");

                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void consolePutFood(AnimalFarm farm) {
        Barn barn = farm.getBarn();
        while (true) {

            String[] foodTypes = animalService.getAnimal(barn.getType()).getCanEat();

            if (foodTypes == null || foodTypes.length == 0) {
                terminal.print("this animal can't eat :/");
                while (!terminal.keyAction(127))
                    ;
                return;
            }

            String[][] canPutArr = new String[foodTypes.length][];

            for (int i = 0; i < foodTypes.length; i++) {
                int canPut = barnService.canPutFeed(barn, foodTypes[i], farm.getWareHouse());
                String[] canPutStrArr = new String[canPut];
                for (int j = 0; j < canPutStrArr.length; j++) {
                    canPutStrArr[j] = (j + 1) + "";
                }
                canPutArr[i] = canPutStrArr;
            }

            int[] putted = terminal.initOptions(foodTypes, canPutArr, null,
                    () -> terminal.print(terminal.colorize("\tSELECT FOOD:\n", Colors.PINK, true)));

            if (putted[0] == -1) {
                return;
            }
            if (putted[1] == -1) {
                terminal.previewing("no feed in wareHouse :(", Colors.PINK);
            } else {
                terminal.statusMessage(
                        barnService.putFeed(barn, putted[1] + 1, foodTypes[putted[0]], farm.getWareHouse()),
                        "putted");
            }

            farmService.sync();
        }

    }

    @Override
    public String report(Farm farm) {
        return barnCLI.report(((AnimalFarm) farm).getBarn());
    }

}
