package org.agrotechnology.WareHouse;

import java.util.ArrayList;
import java.util.List;

import org.agrotechnology.Main;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.FarmProperty.Barn.Animal;
import org.agrotechnology.FarmProperty.Field.Plant;
import org.agrotechnology.utils.HasReport;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

// #lab використано інтерфейс ну і ще це хороший приклад абстракції
public class WareHouse implements HasReport {

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
    public WareHouse(String location, int size) {
        this.location = location;
        this.size = size;
        this.freshness = 1;
        this.capacity = 0;
        this.type = WareHouse.class.getSimpleName();

    }

    public WareHouse(String type, String location, int size) {
        this.location = location;
        this.size = size;
        this.freshness = 1;
        this.capacity = 0;
        this.type = type;

    }

    // #lab використано внутрішній статичний клас
    public static class StorageCell {
        @Expose
        String type;
        @Expose
        int amount;

        public StorageCell(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public int getAmount() {
            return amount;
        }
    }

    /**
     * за 200 оновлює свіжість до 1
     */
    public boolean fullyCleanWareHouse() {

        if (Farm.updateBudget(-200) >= 0) {
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

        for (int i = 0; i < Animal.getList().length; i++) {
            for (int j = 0; j < Animal.getList()[i].getProducts().length; j++) {
                if (Animal.getList()[i].getProducts()[j].equals(type)) {
                    price = Animal.getList()[i].getSellPrice();
                    break;
                }
            }
        }
        if (price > 0) {
            Farm.updateBudget(price * food);
            return true;
        }

        for (int i = 0; i < Plant.getList().length; i++) {
            if (Plant.getList()[i].getName().equals(type)) {
                price = Plant.getList()[i].getSelPrice();
            }
        }
        if (price > 0) {
            Farm.updateBudget(price * food);
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

    public double getFreshness() {
        return freshness;
    }

    public String getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<StorageCell> getStorage() {
        return storage;
    }

    // ? </-- CONSOLE METHODS --/>

    public static WareHouse consoleCreateWareHouse() {
        int wareHouseSize = terminal.inputNumber("WareHouse size (m²): ");
        String location = terminal.input("WareHouse location: ");

        /////////////////////////////////////////////////////
        int[] useFridge = terminal.initOptions(new String[] { "use fridge in wareHouse?" },
                new String[] { "yes", "no" },
                new int[] { 0 }, () -> {
                }, null);

        WareHouse wareHouse = new WareHouse(location, wareHouseSize);
        if (useFridge[1] == 0) {
            wareHouse = new WareHouseWithFridge(location, wareHouseSize);
        }
        return wareHouse;
    }

    public static void consoleActions(Farm farm) {
        WareHouse wareHouse = farm.getWareHouse();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("clean");

            options.add("transfer");

            if (wareHouse instanceof WareHouseWithFridge) {
                options.add("charge fridge");
            }

            int selected = terminal.initOptions(
                    options.toArray(),
                    null,
                    () -> terminal.print(terminal.colorize("\tWAREHOUSE:\n", 0, true)));

            switch (selected) {
                case -1:
                    return;

                case 0:
                    if (wareHouse.fullyCleanWareHouse()) {
                        terminal.previewing("seccefully cleaned", 2);
                    } else {
                        terminal.previewing("no unough money :(", 1);
                    }
                    break;
                case 2:
                    if (((WareHouseWithFridge) wareHouse).chargeFridge()) {
                        terminal.previewing("seccefully charged", 2);
                    } else {
                        terminal.previewing("no unough money :(", 1);
                    }
                    break;

                case 1:
                    consoleTransfer(farm);

                default:
                    break;
            }

        }
    }

    private static void consoleTransfer(Farm farm) {
        List<Farm> farms = Main.getFarms();
        while (true) {
            ArrayList<Farm> farmsLocal = new ArrayList<>();

            for (int i = 0; i < farms.size(); i++) {
                if (!farms.get(i).getName().equals(farm.getName())) {
                    farmsLocal.add(farms.get(i));
                }
            }
            String[] farmsNames = new String[farmsLocal.size()];

            for (int i = 0; i < farmsLocal.size(); i++) {
                farmsNames[i] = farmsLocal.get(i).getName();
            }

            String[] products = farm.getWareHouse().getListOfStorage();
            int[] usedTo = new int[farmsNames.length];
            for (int i = 0; i < usedTo.length; i++) {
                usedTo[i] = i;
            }

            if (products.length == 0) {
                terminal.previewing("no products:(", 1);
                return;
            }

            int[] selected = terminal.initOptions(farmsNames, products, usedTo, null,
                    () -> terminal.print(terminal.colorize("\tSelect WareHouse :\n", 0, true)));

            if (selected[0] == -1) {
                return;
            }

            if (selected[1] == -1) {
                terminal.previewing("no products:(", 1);
            } else {
                if (farm.getWareHouse().transferFood(products[selected[1]],
                        farmsLocal.get(selected[0]).getWareHouse())) {
                    terminal.statusMessage(true, "transfered");
                } else {
                    terminal.previewing("no free space :(", 1);
                }

            }

        }
    }

}
