package org.agrotechnology.FarmProperty.Barn;

import java.util.ArrayList;

import org.agrotechnology.HasReport;
import org.agrotechnology.Farm.AnimalFarm;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.WareHouse.WareHouse;
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
        if(amount < 0){
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
        if(amount < 0){
            return false;
        }
        if (amount + this.feedAmount > this.animalsAmount * 32) {
            amount = this.animalsAmount * 32;
        }
        int feed = wareHouse.getFood(type, amount);
        this.feedAmount += feed;
        return true;
    }

    public int canPutFeed(String type, WareHouse wareHouse){
        if(this.animalsAmount * 32 - this.feedAmount >= wareHouse.getFood(type, 50)){
            return wareHouse.getFood(type, 50);
        }
        if(animalsAmount*32 - this.feedAmount < 0){
            this.feedAmount = 0;
        }
        return this.animalsAmount * 32 - this.feedAmount;
    }

    public boolean getProduction(int amount, WareHouse wareHouse){
        if(amount <= productionAmount && wareHouse.getFreeSpace() >= amount * this.type.getProducts().length){
            productionAmount -= amount;
            for(String typeName: this.type.getProducts()){
                wareHouse.putFood(typeName, amount);
            }
            return true;
        } 
        return false;
    }

    public void process(){
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

    public int getProductionAmount(){
        return productionAmount;
    }

    //?           </-- CONSOLE METHODS --/> 

    public static Barn consoleCreateBarn(){
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

            int canGetProduct = barn.getProductionAmount();
            int dabler = barn.type.getProducts().length;
            int wareHouseFreeSpace = farm.getWareHouse().getFreeSpace();
            int arrSize = canGetProduct;
            if(wareHouseFreeSpace < dabler * canGetProduct){
                arrSize = (int)(wareHouseFreeSpace/dabler);
            }
            String[] getProductArr = new String[arrSize];
            for (int i = 0; i < arrSize; i++) {
                getProductArr[i] = (i+1) + "";
            }

            String[][] secondOption = {
                    addArr,
                    null,
                    getProductArr
            };

            int[] selected = terminal.initOptions(
                    options.toArray(),
                    secondOption,
                    () -> terminal.print(terminal.colorize("\tBARN:\n", 0, true)),
                    null);

            if (selected[0] == -1) {
                return;
            }
            switch (selected[0]) {
                case 0:

                    if (selected[1] == -1) {
                        terminal.previewing("no unough money :(", 1);
                    } else {
                        terminal.buyOperationMessage(barn.addAnimal(selected[1] + 1));
                    }

                    break;

                case 1:
                    consolePutFood(farm);
                    break;

                case 2:
                    if(selected[1] == -1){
                        terminal.previewing("no production yet :(", 1);
                    } else {
                        terminal.buyOperationMessage(barn.getProduction(selected[1] + 1, farm.getWareHouse()));
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
                terminal.buyOperationMessage(barn.putFeed(putted[1] + 1, foodTypes[putted[0]], farm.getWareHouse()));
            }

        }

    }

}
