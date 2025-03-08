package org.agrotechnology.FarmProperty.Barn.Animals;

public class Pig implements Animal {
    private static final int price = 45;
    private static final String[] Products = {"Pork"};
    private static final String[] canEat = {"Corn", "Tomato", "Wheat", "Millet", "Rye", "Buckwheat", "Potato"};
    //кукурудза, помідори, пшениця, просо, жито, гречка, картопля

    public int getPrice(){
        return price;
    }

    public String getName(){
        return Pig.class.getSimpleName();
    }

    public String[] getProducts(){
        return Products;
    }

    public String[] canEat(){
        return canEat;
    }
}
