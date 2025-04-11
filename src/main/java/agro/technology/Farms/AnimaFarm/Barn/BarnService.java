package agro.technology.Farms.AnimaFarm.Barn;

import org.springframework.stereotype.Service;
import agro.technology.WareHouses.WareHouseService;
import com.google.gson.JsonObject;

import agro.technology.Budget.BudgetService;
import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.WareHouses.WareHouse;

@Service
public class BarnService {

    private final WareHouseService wareHouseService;

    private final AnimalService animalService;
    private final BudgetService budgetService;

    public BarnService(AnimalService animalService, BudgetService budgetService, WareHouseService wareHouseService) {
        this.animalService = animalService;
        this.budgetService = budgetService;
        this.wareHouseService = wareHouseService;
    }

    public Barn create(String type, int size) {
        Barn barn = new Barn(type, size, 0, 0, 0);
        return barn;
    }

    public Barn load(JsonObject json) {
        String type = json.get("type").getAsString();
        int size = json.get("size").getAsInt();
        int animalsAmount = json.get("animalsAmount").getAsInt();
        int feedAmount = json.get("feedAmount").getAsInt();
        int productionAmount = json.get("productionAmount").getAsInt();

        Barn barn = new Barn(type, size, animalsAmount, feedAmount, productionAmount);

        return barn;
    }

    public boolean addAnimal(Barn barn, int amount) {
        if (amount < 0) {
            return false;
        }
        if (budgetService.getBudget() - animalService.getAnimal(barn.getType()).getBuyPrice() * amount >= 0
                && barn.getSize() - barn.animalsAmount >= amount) {
            budgetService.updateBudget(-animalService.getAnimal(barn.getType()).getBuyPrice() * amount);
            barn.animalsAmount += amount;
            return true;
        }
        return false;
    }

    public int canAddAnimals(Barn barn) {
        int budget = budgetService.getBudget();
        if (animalService.getAnimal(barn.getType()).getBuyPrice() != 0 &&
                barn.getSize()
                        - barn.animalsAmount >= (int) (budget / animalService.getAnimal(barn.getType()).getBuyPrice())) {

            return (int) (budget / animalService.getAnimal(barn.getType()).getBuyPrice());
        }
        return barn.getSize() - barn.animalsAmount;
    }

    /**
     * @return повертає feedAmount
     */
    public boolean putFeed(Barn barn, int amount, String type, WareHouse wareHouse) {
        if (amount < 0) {
            return false;
        }
        amount = Math.min(amount, (barn.animalsAmount + 1) * 32 - barn.feedAmount);
        int feed = wareHouseService.getFood(wareHouse, type, amount);
        barn.feedAmount += feed;
        return true;
    }

    public int canPutFeed(Barn barn, String type, WareHouse wareHouse) {

        if (barn.animalsAmount * 32 - barn.feedAmount >= Math.min(wareHouseService.checkIfIsFood(wareHouse, type), 0)) {
            return Math.min(wareHouseService.checkIfIsFood(wareHouse, type), 50);
        }
        if (barn.animalsAmount * 32 - barn.feedAmount < 0) {
            barn.feedAmount = 0;
        }
        return barn.animalsAmount * 32 - barn.feedAmount;
    }

    public boolean getProduction(Barn barn, int amount, WareHouse wareHouse) {
        if (amount <= barn.productionAmount
                && wareHouseService.getFreeSpace(wareHouse) >= amount
                        * animalService.getAnimal(barn.getType()).getProducts().length) {
            barn.productionAmount -= amount;
            for (String typeName : animalService.getAnimal(barn.getType()).getProducts()) {
                wareHouseService.putFood(wareHouse, typeName, amount);
            }
            return true;
        }
        return false;
    }

    public int canGetProduction(Barn barn, WareHouse wareHouse) {
        int canGetProduct = barn.getProductionAmount();
        int dabler = animalService.getAnimal(barn.getType()).getProducts().length;
        int wareHouseFreeSpace = wareHouseService.getFreeSpace(wareHouse);
        int product = canGetProduct;
        if (wareHouseFreeSpace < dabler * canGetProduct) {
            product = (int) (wareHouseFreeSpace / dabler);
        }
        return product;
    }

    public void process(Barn barn) {
        new Processes(barn, animalService);
    }
}
