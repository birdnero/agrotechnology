package org.agrotechnology.FarmProperty.Barn;

import java.util.ArrayList;

import org.agrotechnology.Farm.AnimalFarm;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.utils.HasReport;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

public class Barn implements HasReport {

    @Expose
    private Animal type;
    @Expose
    private int size;
    @Expose
    protected int animalsAmount;
    @Expose
    protected int feedAmount;
    @Expose
    protected int productionAmount;

    /**
     * 
     * @param type - тільки назви класів тварин інтерфейсу Animal.defineType
     * @param size - максимальна кількість тварин що може там знаходитися
     */
    public Barn(String type, int size) {
        this.type = Animal.defineType(type);
        this.size = size;
        this.animalsAmount = 0;
        this.feedAmount = 0;
        this.productionAmount = 0;

        new Processes(this);
    }

    public boolean addAnimal(int amount) {
        if (amount < 0) {
            return false;
        }
        if (Farm.getBudget() - this.type.getPrice() * amount >= 0 && this.size - this.animalsAmount >= amount) {
            Farm.updateBudget(-this.type.getPrice() * amount);
            this.animalsAmount += amount;
            return true;
        }
        return false;
    }

    public int canAddAnimals() {
        int budget = Farm.getBUDGET();
        if (this.size - this.animalsAmount >= (int) (budget / this.type.getPrice())) {
            return (int) (budget / this.type.getPrice());
        }
        return this.size - this.animalsAmount;
    }

    /**
     * @return повертає feedAmount
     */
    public boolean putFeed(int amount, String type, WareHouse wareHouse) {
        if (amount < 0) {
            return false;
        }
        amount = Math.min(amount, (this.animalsAmount + 1) * 32 - this.feedAmount);
        int feed = wareHouse.getFood(type, amount);
        this.feedAmount += feed;
        return true;
    }

    public int canPutFeed(String type, WareHouse wareHouse) {
        
        if (this.animalsAmount * 32 - this.feedAmount >= Math.min(wareHouse.checkIfIsFood(type), 0)) {
            return Math.min(wareHouse.checkIfIsFood(type), 50);
        }
        if (animalsAmount * 32 - this.feedAmount < 0) {
            this.feedAmount = 0;
        }
        return this.animalsAmount * 32 - this.feedAmount;
    }

    public boolean getProduction(int amount, WareHouse wareHouse) {
        if (amount <= productionAmount && wareHouse.getFreeSpace() >= amount * this.type.getProducts().length) {
            productionAmount -= amount;
            for (String typeName : this.type.getProducts()) {
                wareHouse.putFood(typeName, amount);
            }
            return true;
        }
        return false;
    }

    public int canGetProduction(WareHouse wareHouse) {
        int canGetProduct = this.getProductionAmount();
        int dabler = this.type.getProducts().length;
        int wareHouseFreeSpace = wareHouse.getFreeSpace();
        int product = canGetProduct;
        if (wareHouseFreeSpace < dabler * canGetProduct) {
            product = (int) (wareHouseFreeSpace / dabler);
        }
        return product;
    }

    public void process() {
        new Processes(this);
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(this.type.getName() + " barn"));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue(this.type.getName() + "s amount", this.animalsAmount));
        str.append(terminal.formatDataValue("production amount", productionAmount));
        str.append(terminal.formatDataValue("feed", feedAmount));

        str.append("\n");

        return str.toString();
    }

    public Animal getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getAnimalsAmount() {
        return animalsAmount;
    }

    public int getFeedAmount() {
        return feedAmount;
    }

    public int getProductionAmount() {
        return productionAmount;
    }

    // ? </-- CONSOLE METHODS --/>

    public static Barn consoleCreateBarn() {
        int barnType = terminal.initOptions(Animal.getTypes(), () -> {
        }, () -> {
            terminal.print(terminal.colorize("\tSelect type of animals\n", 0, true));
        });

        int maxAmount = terminal.inputNumber("Barn max animal amount: ");

        return new Barn(Animal.getTypes()[barnType], maxAmount);
    }

    public static void consoleActions(AnimalFarm farm) {
        Barn barn = farm.getBarn();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("add " + barn.getType().getName());
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
                    consolePutFood(farm);
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

    private static void consolePutFood(AnimalFarm farm) {
        Barn barn = farm.getBarn();
        while (true) {

            String[] foodTypes = barn.getType().getCanEat();
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
                terminal.statusMessage(barn.putFeed(putted[1] + 1, foodTypes[putted[0]], farm.getWareHouse()), "putted");
            }

        }

    }

}
