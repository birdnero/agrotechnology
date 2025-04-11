package agro.technology.WareHouses.WareHouseWithFridge;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import agro.technology.Budget.BudgetService;
import agro.technology.Product.Product;
import agro.technology.WareHouses.IWareHouseService;
import agro.technology.WareHouses.Procesess;
import agro.technology.WareHouses.WareHouse;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class WareHouseWithFridgeService implements IWareHouseService {

    private final CLI terminal;

    private final BudgetService budgetService;

    public WareHouseWithFridgeService(BudgetService budgetService, CLI cli) {
        this.terminal = cli;
        this.budgetService = budgetService;
    }

    @Override
    public WareHouse create(String type, String location, int size, int capacity, double freshness,
            ArrayList<Product> storage) {
        WareHouseWithFridge wareHouseWithFridge = new WareHouseWithFridge(type, location, size, capacity, freshness,
                storage, 10);

        return wareHouseWithFridge;
    }

    @Override
    public void doAction(WareHouse wareHouse, String actionType) {
        if (actionType.equals("charge fridge"))
            chargeFridge(wareHouse);

    }

    @Override
    public List<String> getActionsList() {
        return List.of("charge fridge");
    }

    @Override
    public String getWareHouseType() {
        return "WareHouse with fridge";
    }

    @Override
    public void initProcesess(WareHouse wareHouse) {
        new Procesess(wareHouse) {
            @Override
            public void process(WareHouse wareHOuse) {
                if (wareHOuse instanceof WareHouseWithFridge) {
                    WareHouseWithFridge wareHouse = ((WareHouseWithFridge) wareHOuse);
                    wareHouse.setFreshness(wareHouse.getFreshness() + 0.01);
                    if (wareHouse.fridgeWithAccumulator > 0) {
                        wareHouse.fridgeWithAccumulator = Math.max(0, wareHouse.fridgeWithAccumulator - 0.2);
                    }

                    if (wareHouse.fridgeWithAccumulator == 0 && !wareHouse.getStorage().isEmpty()) {
                        wareHouse.getStorage().removeFirst();
                    }
                }
            }
        };
    }

    @Override
    public WareHouse load(String type, String location, int size, int capacity, double freshness,
            ArrayList<Product> storage, JsonObject json) {
        double fridgeWithAccumulator = json.get("fridgeWithAccumulator").getAsDouble();

        WareHouseWithFridge wareHouseWithFridge = new WareHouseWithFridge(type, location, size, capacity, freshness,
                storage, fridgeWithAccumulator);

        return wareHouseWithFridge;
    }

    /**
     * заряджає холодильник якщо є гроші у ферм
     * 
     * @return повертає рівень заряду холодильника
     */
    public void chargeFridge(WareHouse wareHouse) {
        if (wareHouse instanceof WareHouseWithFridge) {
            if (budgetService.updateBudget(-20) >= 0) {
                ((WareHouseWithFridge) wareHouse).fridgeWithAccumulator += 2;
                terminal.previewing("seccefully charged", Colors.GREEN);
            } else
                terminal.previewing("no unough money :(", Colors.RED);
        }
    }

}
