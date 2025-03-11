package org.agrotechnology.FarmProperty.Field;

import com.google.gson.annotations.Expose;

public class Plant {
    @Expose
    private String name;
    @Expose
    private int buyPrice;
    @Expose
    private int selPrice;
    @Expose
    private double dabler;

    public Plant(String name, int buyPrice, int selPrice, double dabler) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.selPrice = selPrice;
        this.dabler = dabler;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public String getName() {
        return name;
    }

    public double getDoubler() {
        return dabler;
    }

    public int getSellPrice() {
        return selPrice;
    }

    public int getSelPrice() {
        return selPrice;
    }

    public double getDabler() {
        return dabler;
    }

    // ? </-- UTILS --/>

    private static final Plant[] plantList = new Plant[] {
            new Plant("Corn", 4, 3, 2),
            new Plant("Tomato", 11, 5, 10),
            new Plant("Wheat", 2, 1, 2.5),
            new Plant("Millet", 2, 2, 1.5), // просо
            new Plant("Rye", 1, 1, 2), // жито
            new Plant("Buckwheat", 9, 10, 1), // гречка
            new Plant("Potato", 3, 3, 4),
            new Plant("Cucumber", 7, 4, 3),
            new Plant("Salad", 1, 3, 6),
            new Plant("Strawberry", 9, 10, 1.7),
            new Plant("Rice", 3, 1, 9),
            new Plant("pumpkin", 7, 2, 0.1),
            new Plant("watermelon", 4, 15, 1),
    };

    public static Plant[] getList() {
        return plantList;
    }

    public static Plant defineType(String typeName) {
        for (Plant plant : plantList) {
            if (plant.getName() == typeName) {
                return plant;
            }
        }
        return null;
    }

    public static String[] getTypes() {
        String[] types = new String[plantList.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = plantList[i].getName();
        }
        return types;
    }
}
