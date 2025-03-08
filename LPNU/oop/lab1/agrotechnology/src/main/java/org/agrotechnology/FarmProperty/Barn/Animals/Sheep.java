package org.agrotechnology.FarmProperty.Barn.Animals;

public class Sheep implements Animal {
    private static final int price = 60;
    private static final String name = Sheep.class.getSimpleName();
    private static final String[] Products = {"Wool", "Mutton"};
    private static final String[] canEat = {"grass", "herb", "hay"};

    public int getPrice(){
        return price;
    }

    public String getName() {
        return name;
    }

    public String[] getProducts(){
        return Products;
    }

    public String[] canEat(){
        return canEat;
    }
}
