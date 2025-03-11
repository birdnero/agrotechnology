package org.agrotechnology.FarmProperty.Field;

import java.util.ArrayList;

import org.agrotechnology.Farm.Farm;
import org.agrotechnology.Farm.PlantFarm;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.utils.HasReport;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

public class Field implements HasReport {
    // ? to sow - сіяти
    // ? ripened - достигло
    @Expose
    protected Plant type;
    @Expose
    private int size;
    @Expose
    protected int sown;
    @Expose
    protected int ripened;
    @Expose
    protected double waterLevel;

    /**
     * @param type - тільки назви з PlantUtils.getTypes
     * @param size - максимальна кількість росллин що може там знаходитися
     */
    public Field(String type, int size) {
        this.size = size;
        this.type = Plant.defineType(type);
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
        if (Farm.getBudget() - this.type.getBuyPrice() * amount >= 0 && this.size - this.sown >= amount) {
            Farm.updateBudget(-this.type.getBuyPrice() * amount);
            this.sown += amount;
            return true;
        }
        return false;
    }

    public int canSow() {
        int canToSow = this.size - this.sown;
        int canBuy = (int) (Farm.getBUDGET() / this.type.getBuyPrice());
        if (canToSow >= canBuy) {
            return canBuy;
        }
        return canToSow;
    }

    public boolean waterField() {
        if (Farm.getBudget() - 150 >= 0) {
            Farm.updateBudget(-150);
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
            wareHouse.putFood(this.type.getName(), amount);
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

        str.append(terminal.formatName("Field of " + type.getName()));
        str.append(terminal.formatDataValue("size", this.size + " m²"));
        str.append(terminal.formatDataValue("sown", this.sown));
        str.append(terminal.formatDataValue("has ripened", this.ripened));
        str.append("\n");

        return str.toString();
    }

    public int getSize() {
        return size;
    }

    public Plant getType() {
        return type;
    }

    public int getSown() {
        return sown;
    }

    public double getRipened() {
        return ripened;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    // ? </-- CONSOLE METHODS --/>

    public static Field consoleCreateField() {
        int fieldSize = terminal.inputNumber("Field size (m²): ");

        int fieldType = terminal.initOptions(Plant.getTypes(), () -> {
        }, () -> {
            terminal.print(terminal.colorize("\tSelect type of plant\n", 0, true));
        });

        return new Field(Plant.getTypes()[fieldType], fieldSize);
    }

    public static void consoleActions(PlantFarm farm) {
        Field field = farm.getField();
        while (true) {

            ArrayList<String> options = new ArrayList<>();
            options.add("to sow " + field.type.getName());
            options.add("to water");
            options.add("harvest");

            int canSow = field.canSow();
            String[] sowArr = new String[canSow];
            for (int i = 0; i < canSow; i++) {
                sowArr[i] = (i + 1) + "";
            }

            String[][] secondOption = {
                    sowArr,
                    null,
                    null
            };

            int[] selected = terminal.initOptions(
                    options.toArray(),
                    secondOption,
                    null,
                    () -> terminal
                            .print(terminal.colorize("\t" + field.type.getName().toUpperCase() + " FIELD:\n", 0,
                                    true)));

            if (selected[0] == -1) {
                return;
            }
            switch (selected[0]) {
                case 0:

                    if (selected[1] == -1) {
                        terminal.previewing("no unough money or place :(", 1);
                    } else {
                        terminal.statusMessage(field.toSow(selected[1] + 1), "sowed");
                    }

                    break;

                case 1:
                    if (field.waterField()) {
                        terminal.statusMessage(true, "watered");
                    } else {
                        terminal.previewing("no unough money :(", 1);
                    }
                    break;

                case 2:
                int amount = field.canTakeRipened(farm.getWareHouse());
                    if (amount <= 0) {
                        terminal.previewing("no production yet :(", 1);
                    } else {
                        terminal.statusMessage(field.takeRipened(amount, farm.getWareHouse()), "taken");
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
