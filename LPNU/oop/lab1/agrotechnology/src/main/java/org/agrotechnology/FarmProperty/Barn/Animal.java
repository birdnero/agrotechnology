package org.agrotechnology.FarmProperty.Barn;

import com.google.gson.annotations.Expose;

public class Animal {
    @Expose
    private int price;
    @Expose
    private String[] Products;
    @Expose
    private String[] canEat;
    @Expose
    private String name;
    @Expose
    private double life;
    @Expose 
    private int sellPrice;

    public int getSellPrice() {
        return sellPrice;
    }

    /**
     * avaible Animal create
     * 
     * @param products - what can give animal
     * @param canEat   - what can eat animal
     */
    public Animal(String name, int price, double life, String[] products, String[] canEat, int sellPrice) {
        this.name = name;
        this.price = price;
        this.life = life;
        this.Products = products;
        this.canEat = canEat;
        this.sellPrice = sellPrice;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return this.name;
    }

    public String[] getProducts() {
        return Products;
    }

    public String[] getCanEat() {
        return canEat;
    }

    public double getLife() {
        return life;
    }

    // ? </-- UTILS --/>

    private static final Animal[] AnimalList = new Animal[] {
            new Animal("Chiken", 6, 0.25, new String[] { "ChikenMeat" },
                    new String[] { "Corn", "Wheat", "Millet", "Rye" }, 3),

            new Animal("Pig", 45, 0.4, new String[] { "Pork" },
                    new String[] { "Corn", "Tomato", "Wheat", "Millet", "Rye", "Buckwheat", "Potato" }, 5),

            new Animal("Sheep", 60, 0.47, new String[] { "Wool", "Mutton" }, new String[] { "Grass", "Herb", "Hay" }, 5),

            new Animal("Cow", 150, 0.8, new String[] { "Milk", "Beef" }, new String[] { "Grass", "Herb", "Hay" }, 3),

            new Animal("Student SHI", 13, 0.04, new String[] { "slave meat" }, new String[] { "Wheat", "Grass" }, 2),
    };

    public static Animal[] getList() {
        return AnimalList;
    }

    public static Animal defineType(String typeName) {
        for (Animal Animal : AnimalList) {
            if (Animal.getName() == typeName) {
                return Animal;
            }
        }
        return null;
    }

    public static String[] getTypes() {
        String[] types = new String[AnimalList.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = AnimalList[i].getName();
        }
        return types;
    }
}
