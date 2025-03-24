package agro.technology.Farms.AnimaFarm;

import java.util.ArrayList;
import agro.technology.utils.terminal;
import org.springframework.stereotype.Service;

import agro.technology.Farms.IFarmConsoleFactory;
import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.Farms.AnimaFarm.Barn.Barn;
import agro.technology.Farms.AnimaFarm.Barn.BarnConsoleService;
import agro.technology.Farms.Farm.Farm;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;

@Service
public class AnimalFarmConsoleService implements IFarmConsoleFactory {

    private final AnimalService animalService;

    private final terminal terminal;

    private final BarnConsoleService barnConsoleService;

    public AnimalFarmConsoleService(BarnConsoleService barnConsoleService, terminal terminal, AnimalService animalService) {
        this.barnConsoleService = barnConsoleService;
        this.terminal = terminal;
        this.animalService = animalService;
    }

    @Override
    public Farm create(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {
        Barn barn = barnConsoleService.create();
        return new AnimalFarm(name, location, wareHouse, workers, barn);
    }

    @Override
    public String getType() {
        return "Farm of plants";
    }

    @Override
    public void specializationAction(Farm farm) {
        Barn barn = ((AnimalFarm) farm).getBarn();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("add " + barn.getType());
            options.add("put feed");
            options.add("get production");

            int canAddAnimals = barn.canAddAnimals();
            String[] addArr = new String[canAddAnimals];
            for (int i = 0; i < canAddAnimals; i++) {
                addArr[i] = (i + 1) + "";
            }

            int arrSize = barn.canGetProduction(farm.getWareHouse());
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
                    () -> terminal.print(terminal.colorize("\tBARN:\n", 0, true)));

            if (selected[0] == -1) {
                return;
            }
            switch (selected[0]) {
                case 0:

                    if (selected[1] == -1) {
                        terminal.previewing("no unough money or room:(", 1);
                    } else {
                        terminal.statusMessage(barn.addAnimal(selected[1] + 1), "added");
                    }

                    break;

                case 1:
                    consolePutFood((AnimalFarm) farm);
                    break;

                case 2:
                    if (selected[1] == -1) {
                        terminal.previewing("no production yet :(", 1);
                    } else {
                        terminal.statusMessage(barn.getProduction(selected[1] + 1, farm.getWareHouse()), "taked");

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
            String[][] canPutArr = new String[foodTypes.length][];

            for (int i = 0; i < foodTypes.length; i++) {
                int canPut = barn.canPutFeed(foodTypes[i], farm.getWareHouse());
                String[] canPutStrArr = new String[canPut];
                for (int j = 0; j < canPutStrArr.length; j++) {
                    canPutStrArr[j] = (j + 1) + "";
                }
                canPutArr[i] = canPutStrArr;
            }

            int[] putted = terminal.initOptions(foodTypes, canPutArr, null,
                    () -> terminal.print(terminal.colorize("\tSELECT FOOD:\n", 0, true)));

            if (putted[0] == -1) {
                return;
            }
            if (putted[1] == -1) {
                terminal.previewing("no feed in wareHouse :(", 1);
            } else {
                terminal.statusMessage(barn.putFeed(putted[1] + 1, foodTypes[putted[0]], farm.getWareHouse()),
                        "putted");
            }

        }

    }
    
}
