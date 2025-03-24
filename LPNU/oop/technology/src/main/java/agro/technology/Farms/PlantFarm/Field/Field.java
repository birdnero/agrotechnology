package agro.technology.Farms.PlantFarm.Field;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import agro.technology.Budget.BudgetService;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.utils.HasReport;
import agro.technology.utils.terminal;
import lombok.Getter;

import com.google.gson.annotations.Expose;

@Getter
@Component
public class Field implements HasReport {

    
    // ? to sow - сіяти
    // ? ripened - достигло
    @Expose
    protected String type;
    @Expose
    private int size;
    @Expose
    protected int sown;
    @Expose
    protected int ripened;
    @Expose
    protected double waterLevel;
    
    private PlantService plantService;
    private terminal terminal;
    private BudgetService budgetService;
    
    @Autowired
    public Field(terminal terminal, PlantService plantService, BudgetService budgetService){
        this.terminal = terminal;
        this.plantService = plantService;
        this.budgetService = budgetService;
    }

    /**
     * @param type - тільки назви з PlantUtils.getTypes
     * @param size - максимальна кількість росллин що може там знаходитися
     */
    public Field(String type, int size) {
        this.size = size;
        this.type = type;
        this.ripened = 0;
        this.waterLevel = 1;
        new GrowProcess(this);
    }


    /**
     * посіяти ще
     * 
     * @return
     */
    public boolean toSow(int amount) {
        if (budgetService.getBudget() - plantService.getPlant(type).getBuyPrice() * amount >= 0 && this.size - this.sown >= amount) {
            budgetService.updateBudget(-plantService.getPlant(type).getBuyPrice() * amount);
            this.sown += amount;
            return true;
        }
        return false;
    }

    public int canSow() {
        int canToSow = this.size - this.sown;
        int canBuy = (int) (budgetService.getBudget() / plantService.getPlant(type).getBuyPrice());
        if (canToSow >= canBuy) {
            return canBuy;
        }
        return canToSow;
    }

    public boolean waterField() {
        if (budgetService.getBudget() - 150 >= 0) {
            budgetService.updateBudget(-150);
            this.waterLevel = 1;
            return true;
        }
        return false;
    }

    /**
     * 
     * @return зібрати врожай
     */
    public boolean takeRipened(int amount, WareHouse wareHouse) {
        int freeSpace = wareHouse.isSpaceFor(amount);
        if (freeSpace < 0) {
            return false;
        }
        if (ripened >= amount && freeSpace >= amount) {
            ripened -= amount;
            wareHouse.putFood(plantService.getPlant(type).getName(), amount);
            return true;
        }
        return false;
    }

    public int canTakeRipened(WareHouse wareHouse) {
        int freeSpace = wareHouse.getFreeSpace();
        if (freeSpace >= this.ripened) {
            return this.ripened;
        }
        return freeSpace;
    }

    public void process() {
        new GrowProcess(this);
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName("Field of " + plantService.getPlant(type).getName()));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue("sown", this.sown));
        str.append(terminal.formatDataValue("has ripened", this.ripened));
        str.append("\n");

        return str.toString();
    }


}
