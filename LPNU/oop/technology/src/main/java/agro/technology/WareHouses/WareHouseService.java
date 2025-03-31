package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import agro.technology.Budget.BudgetService;
import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.WareHouses.WareHouse.StorageCell;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class WareHouseService {

    private final PlantService plantService;

    private final AnimalService animalService;

    private final BudgetService budgetService;

    private final CLI terminal;

    private final List<IWareHouseService> wareHouseServices;

    public WareHouseService(List<IWareHouseService> wareHouseServices, CLI cli, BudgetService budgetService,
            AnimalService animalService, PlantService plantService) {
        this.wareHouseServices = wareHouseServices;
        this.budgetService = budgetService;
        this.terminal = cli;
        this.animalService = animalService;
        this.plantService = plantService;
    }

    public IWareHouseService searchWareHouseService(String type) {
        for (IWareHouseService wareHouseService : wareHouseServices) {
            if (wareHouseService.getWareHouseType().equals(type))
                return wareHouseService;
        }
        terminal.previewing("can't find type " + type, Colors.RED);
        return null;

    }

    public WareHouse create(String type, String location, int size) {
        WareHouse wareHouse = searchWareHouseService(type).create(type, location, size, 0, 1,
                new ArrayList<StorageCell>());
        initProcesess(wareHouse);
        searchWareHouseService(type).initProcesess(wareHouse);

        return wareHouse;
    }

    public WareHouse load(JsonObject json) {
        String type = json.get("type").getAsString();
        String location = json.get("location").getAsString();
        int size = json.get("size").getAsInt();
        int capacity = json.get("capacity").getAsInt();
        double freshness = json.get("freshness").getAsDouble();

        JsonArray storageArray = json.getAsJsonArray("storage");
        ArrayList<StorageCell> storage = new ArrayList<>();

        for (var element : storageArray) {
            JsonObject cellJson = element.getAsJsonObject();
            String typeOfProduct = cellJson.get("type").getAsString();
            int amount = cellJson.get("amount").getAsInt();
            storage.add(new StorageCell(typeOfProduct, amount));
        }

        WareHouse wareHouse = searchWareHouseService(type).load(type, location, size, capacity, freshness, storage, json);
        initProcesess(wareHouse);
        searchWareHouseService(type).initProcesess(wareHouse);

        return wareHouse;
    }

    public List<IWareHouseService> getWareHouseServices() {
        return wareHouseServices;
    }

    /**
     * за 200 оновлює свіжість до 1
     */
    public boolean fullyCleanWareHouse(WareHouse wareHouse) {

        if (budgetService.updateBudget(-200) >= 0) {
            wareHouse.freshness = 1;
            return true;
        }
        return false;
    }

    /**
     * перевіряє вміст
     * 
     * @return -1 - якщо немає місця, amount - якщо місця достатньо, щось менше -
     *         вільне місце
     */
    public int isSpaceFor(WareHouse wareHouse, int amount) {
        if (wareHouse.capacity + amount < wareHouse.getSize()) {
            return amount;
        } else if (wareHouse.capacity < wareHouse.getSize()) {
            return wareHouse.getSize() - wareHouse.capacity;
        }
        return -1;
    }

    public int getFreeSpace(WareHouse wareHouse) {
        return wareHouse.getSize() - wareHouse.capacity;
    }

    public String[] getListOfStorage(WareHouse wareHouse) {
        ArrayList<String> prod = new ArrayList<>();
        for (int i = 0; i < wareHouse.storage.size(); i++) {
            if (wareHouse.storage.get(i).getAmount() > 0) {
                prod.add(wareHouse.storage.get(i).getType());
            }
        }

        String[] products = new String[prod.size()];
        for (int i = 0; i < prod.size(); i++) {
            products[i] = prod.get(i);
        }
        return products;
    }

    public boolean putFood(WareHouse wareHouse, String type, int amount) {
        if (wareHouse.getSize() >= wareHouse.capacity + amount) {
            wareHouse.capacity += amount;
            for (StorageCell cell : wareHouse.storage) {
                if (cell.type.equals(type)) {
                    cell.amount += amount;
                    return true;
                }
            }
            wareHouse.storage.add(new StorageCell(type, amount));
        }
        return false;
    }

    /**
     * дістати щось зі складу
     * 
     * @return повертає всю їжу що є якщо на складі не стає
     */
    public int getFood(WareHouse wareHouse, String type, int amount) {
        if (amount <= 0) {
            return 0;
        }
        for (StorageCell cell : wareHouse.storage) {
            if (cell.type.equals(type)) {
                if (cell.amount >= amount) {
                    wareHouse.capacity -= amount;
                    cell.amount -= amount;
                    return amount;
                } else {
                    amount = cell.amount;
                    wareHouse.capacity -= amount;
                    cell.amount = 0;
                    return amount;
                }
            }
        }
        return 0;
    }

    /**
     * дістати щось зі складу
     * 
     * @return повертає всю їжу що є якщо на складі не стає
     */
    public int checkIfIsFood(WareHouse wareHouse, String type) {
        for (StorageCell cell : wareHouse.storage) {
            if (cell.type.equals(type)) {
                return cell.getAmount();
            }
        }
        return 0;
    }

    public boolean transferFood(WareHouse wareHouseFrom, String type, WareHouse wareHouseTo) {
        int isAvaible = Math.min(checkIfIsFood(wareHouseFrom, type), 50);
        int freeSpace = isSpaceFor(wareHouseTo, isAvaible);
        if (freeSpace > 0) {
            putFood(wareHouseTo, type, getFood(wareHouseFrom, type, freeSpace));
            return true;
        }
        return false;
    }

    // ! робить код більш зв'язним, якщо розширюватиму позбутися
    public boolean sellFood(WareHouse wareHouse, String type) {
        int food = getFood(wareHouse, type, wareHouse.getSize());
        int price = 0;

        for (int i = 0; i < animalService.getAnimalTypes().size(); i++) {
            for (int j = 0; j < animalService.getAnimals().get(i).getProducts().length; j++) {
                if (animalService.getAnimals().get(i).getProducts()[j].equals(type)) {
                    price = animalService.getAnimals().get(i).getSellPrice();
                    break;
                }
            }
        }
        if (price > 0) {
            budgetService.updateBudget(price * food);
            return true;
        }

        for (int i = 0; i < plantService.getPlantTypes().size(); i++) {
            if (plantService.getPlants().get(i).getName().equals(type)) {
                price = plantService.getPlants().get(i).getSelPrice();
            }
        }
        if (price > 0) {
            budgetService.updateBudget(price * food);
            return true;
        }
        return false;
    }

    public void initProcesess(WareHouse wareHouse) {
        new Procesess(wareHouse) {
            @Override
            public void process(WareHouse wareHouse) {
                wareHouse.freshness = Math.max(0, wareHouse.freshness - 0.03);

                if (wareHouse.freshness <= 0 && !wareHouse.storage.isEmpty()) {
                    wareHouse.storage.removeFirst();
                }
            }
        };
        searchWareHouseService(wareHouse.getType()).initProcesess(wareHouse);
    }

}
