package agro.technology.Farms.PlantFarm.Field;

import org.springframework.stereotype.Service;
import agro.technology.WareHouses.WareHouseService;
import com.google.gson.JsonObject;

import agro.technology.Budget.BudgetService;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.WareHouses.WareHouse;

@Service
public class FieldService {

    private final WareHouseService wareHouseService;

    private PlantService plantService;
    private BudgetService budgetService;

    public FieldService(PlantService plantService, BudgetService budgetService, WareHouseService wareHouseService) {
        this.plantService = plantService;
        this.budgetService = budgetService;
        this.wareHouseService = wareHouseService;
    }

    public Field create(String type, int size) {
        Field field = new Field(type, size, 0, 0, 1);

        return field;
    }

    public void process(Field field) {
        new Procesess(field, plantService);
    }

    public Field load(JsonObject json) {
        String type = json.get("type").getAsString();
        int size = json.get("size").getAsInt();
        int sown = json.get("sown").getAsInt();
        int ripened = json.get("ripened").getAsInt();
        double waterLevel = json.get("waterLevel").getAsDouble();

        Field field = new Field(type, size, sown, ripened, waterLevel);

        return field;
    }

    /**
     * посіяти ще
     */
    public boolean toSow(Field field, int amount) {
        if (budgetService.getBudget() - plantService.getPlant(field.getType()).getBuyPrice() * amount >= 0
                && field.getSize() - field.sown >= amount) {
            budgetService.updateBudget(-plantService.getPlant(field.getType()).getBuyPrice() * amount);
            field.sown += amount;
            return true;
        }
        return false;
    }

    public int canSow(Field field) {
        int canToSow = field.getSize() - field.sown;
        int price = plantService.getPlant(field.getType()).getBuyPrice();
        if (price == 0)
            return canToSow;
        int canBuy = (int) (budgetService.getBudget() / plantService.getPlant(field.getType()).getBuyPrice());
        if (canToSow >= canBuy) {
            return canBuy;
        }
        return canToSow;
    }

    public boolean waterField(Field field) {
        if (budgetService.getBudget() - 150 >= 0) {
            budgetService.updateBudget(-150);
            field.waterLevel = 1;
            return true;
        }
        return false;
    }

    /**
     * 
     * @return зібрати врожай
     */
    public boolean takeRipened(Field field, int amount, WareHouse wareHouse) {
        int freeSpace = wareHouseService.isSpaceFor(wareHouse, amount);
        if (freeSpace < 0) {
            return false;
        }
        if (field.ripened >= amount && freeSpace >= amount) {
            field.ripened -= amount;
            wareHouseService.putFood(wareHouse, plantService.getPlant(field.getType()).getName(), amount);
            return true;
        }
        return false;
    }

    public int canTakeRipened(Field field, WareHouse wareHouse) {
        int freeSpace = wareHouseService.getFreeSpace(wareHouse);
        if (freeSpace >= field.ripened) {
            return field.ripened;
        }
        return freeSpace;
    }

}
