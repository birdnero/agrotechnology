package agro.technology.WareHouses.WareHouse;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import agro.technology.Budget.BudgetService;
import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.utils.HasReport;
import agro.technology.utils.CLI;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.annotations.Expose;

// #lab використано інтерфейс ну і ще це хороший приклад абстракції
@Getter
public class WareHouse implements HasReport {

    private AnimalService animalService;
    private PlantService plantService;
    private CLI terminal;
    private BudgetService budgetService;
    
    @Autowired
    public WareHouse(PlantService plantService, AnimalService animalService, CLI terminal, BudgetService budgetService){
        this.plantService = plantService;
        this.animalService = animalService;
        this.terminal = terminal;
        this.budgetService = budgetService;
    }

    @Expose
    protected String location;
    @Expose
    protected int size;
    @Expose
    protected int capacity;
    @Expose
    protected double freshness;
    @Expose
    protected ArrayList<StorageCell> storage = new ArrayList<StorageCell>();
    @Expose
    protected String type;

    /**
     * стандартний склад
     * 
     * @param location - те саме що й у ферми
     * @param size
     * @param name     - назва/тип складу
     */

    public WareHouse(String type, String location, int size) {
        this.location = location;
        this.size = size;
        this.freshness = 1;
        this.capacity = 0;
        this.type = type;

    }

    // #lab використано внутрішній статичний клас
    @Getter
    @AllArgsConstructor
    public static class StorageCell {
        @Expose
        String type;
        @Expose
        int amount;
    }

    /**
     * за 200 оновлює свіжість до 1
     */
    public boolean fullyCleanWareHouse() {

        if (budgetService.updateBudget(-200) >= 0) {
            this.freshness = 1;
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
    public int isSpaceFor(int amount) {
        if (this.capacity + amount < this.size) {
            return amount;
        } else if (this.capacity < this.size) {
            return this.size - this.capacity;
        }
        return -1;
    }

    public int getFreeSpace() {
        return this.size - this.capacity;
    }

    public String[] getListOfStorage() {
        ArrayList<String> prod = new ArrayList<>();
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getAmount() > 0) {
                prod.add(storage.get(i).getType());
            }
        }

        String[] products = new String[prod.size()];
        for (int i = 0; i < prod.size(); i++) {
            products[i] = prod.get(i);
        }
        return products;
    }

    public boolean putFood(String type, int amount) {
        if (this.size >= this.capacity + amount) {
            this.capacity += amount;
            for (StorageCell cell : storage) {
                if (cell.type.equals(type)) {
                    cell.amount += amount;
                    return true;
                }
            }
            storage.add(new StorageCell(type, amount));
        }
        return false;
    }

    /**
     * дістати щось зі складу
     * 
     * @return повертає всю їжу що є якщо на складі не стає
     */
    public int getFood(String type, int amount) {
        if (amount <= 0) {
            return 0;
        }
        for (StorageCell cell : storage) {
            if (cell.type.equals(type)) {
                if (cell.amount >= amount) {
                    this.capacity -= amount;
                    cell.amount -= amount;
                    return amount;
                } else {
                    amount = cell.amount;
                    this.capacity -= amount;
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
    public int checkIfIsFood(String type) {
        for (StorageCell cell : storage) {
            if (cell.type.equals(type)) {
                return cell.getAmount();
            }
        }
        return 0;
    }

    public boolean transferFood(String type, WareHouse wareHouse) {
        int isAvaible = Math.min(this.checkIfIsFood(type), 50);
        int freeSpace = wareHouse.isSpaceFor(isAvaible);
        if (freeSpace > 0) {
            wareHouse.putFood(type, this.getFood(type, freeSpace));
            return true;
        }
        return false;
    }

    public boolean sellFood(String type) {
        int food = getFood(type, size);
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

    public void process() {
        // #lab використано анонімний клас
        new Process(this) {
            @Override
            public void process(WareHouse wareHouse) {
                wareHouse.freshness = Math.max(0, wareHouse.freshness - 0.03);

                if (wareHouse.freshness <= 0 && !wareHouse.storage.isEmpty()) {
                    wareHouse.storage.removeFirst();
                }
            }
        };
        processHook();
    }

    public void processHook() {
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName("WareHouse"));
        str.append(terminal.formatDataValue("located", location));
        str.append(terminal.formatDataValue("size", size + " m²"));
        if (!storage.isEmpty()) {
            str.append(terminal.formatName("storage items"));
            for (StorageCell cell : storage) {
                str.append(terminal.formatDataValue(cell.type, cell.amount));
            }

        }
        return str.toString();
    }

}
