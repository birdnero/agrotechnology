package agro.technology.Farms.AnimaFarm.Barn;

import org.springframework.stereotype.Component;

import agro.technology.Budget.BudgetService;
import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.utils.HasReport;
import agro.technology.utils.CLI;
import lombok.Getter;

import com.google.gson.annotations.Expose;

@Component
@Getter
public class Barn implements HasReport {

    @Expose
    private String type;
    @Expose
    private int size;
    @Expose
    protected int animalsAmount;
    @Expose
    protected int feedAmount;
    @Expose
    protected int productionAmount;

    private CLI terminal;
    private AnimalService animalService;
    private BudgetService budgetService;

    public class BarnException extends RuntimeException {
        public BarnException() {
            super("barn not intialized!");
        }
    }

    private void checkInitialization() {
        if (this.type == null)
            throw new BarnException();
    }

    /**
     * DON'T FORGET TO INFLICT constructor()
     */
    public Barn(CLI terminal, AnimalService animalService, BudgetService budgetService) {
        this.terminal = terminal;
        this.animalService = animalService;
        this.budgetService = budgetService;
        this.type = null;
        this.size = -1;
    }

    /**
     * initialization method 
     */
    public void constructor(String type, int size) {
        this.type = type;
        this.size = size;
        this.animalsAmount = 0;
        this.feedAmount = 0;
        this.productionAmount = 0;

        new Processes(this);
    }

    public boolean addAnimal(int amount) {
        checkInitialization();
        if (amount < 0) {
            return false;
        }
        if (budgetService.getBudget() - animalService.getAnimal(type).getPrice() * amount >= 0
                && this.size - this.animalsAmount >= amount) {
            budgetService.updateBudget(-animalService.getAnimal(type).getPrice() * amount);
            this.animalsAmount += amount;
            return true;
        }
        return false;
    }

    public int canAddAnimals() {
        checkInitialization();
        int budget = budgetService.getBudget();
        if (this.size - this.animalsAmount >= (int) (budget / animalService.getAnimal(type).getPrice())) {
            return (int) (budget / animalService.getAnimal(type).getPrice());
        }
        return this.size - this.animalsAmount;
    }

    /**
     * @return повертає feedAmount
     */
    public boolean putFeed(int amount, String type, WareHouse wareHouse) {
        checkInitialization();
        if (amount < 0) {
            return false;
        }
        amount = Math.min(amount, (this.animalsAmount + 1) * 32 - this.feedAmount);
        int feed = wareHouse.getFood(type, amount);
        this.feedAmount += feed;
        return true;
    }

    public int canPutFeed(String type, WareHouse wareHouse) {
        checkInitialization();

        if (this.animalsAmount * 32 - this.feedAmount >= Math.min(wareHouse.checkIfIsFood(type), 0)) {
            return Math.min(wareHouse.checkIfIsFood(type), 50);
        }
        if (animalsAmount * 32 - this.feedAmount < 0) {
            this.feedAmount = 0;
        }
        return this.animalsAmount * 32 - this.feedAmount;
    }

    public boolean getProduction(int amount, WareHouse wareHouse) {
        checkInitialization();
        if (amount <= productionAmount
                && wareHouse.getFreeSpace() >= amount * animalService.getAnimal(type).getProducts().length) {
            productionAmount -= amount;
            for (String typeName : animalService.getAnimal(type).getProducts()) {
                wareHouse.putFood(typeName, amount);
            }
            return true;
        }
        return false;
    }

    public int canGetProduction(WareHouse wareHouse) {
        checkInitialization();
        int canGetProduct = this.getProductionAmount();
        int dabler = animalService.getAnimal(type).getProducts().length;
        int wareHouseFreeSpace = wareHouse.getFreeSpace();
        int product = canGetProduct;
        if (wareHouseFreeSpace < dabler * canGetProduct) {
            product = (int) (wareHouseFreeSpace / dabler);
        }
        return product;
    }

    public void process() {
        checkInitialization();
        new Processes(this);
    }

    @Override
    public String report() {
        checkInitialization();
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(animalService.getAnimal(type).getName() + " barn"));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue(animalService.getAnimal(type).getName() + "s amount", this.animalsAmount));
        str.append(terminal.formatDataValue("production amount", productionAmount));
        str.append(terminal.formatDataValue("feed", feedAmount));

        str.append("\n");

        return str.toString();
    }

}
