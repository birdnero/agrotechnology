package org.agrotechnology.FarmProperty.Barn.Animals;

public class Cow implements Animal {
    private static final int price = 100;
    private static final String name = Cow.class.getSimpleName();
    private static final String[] Products = {"Milk", "Beef"};
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
