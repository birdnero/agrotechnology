package org.agrotechnology.FarmProperty.Barn.Animals;

public class Chiken implements Animal{
    private static final int price = 6;
    private static final String[] Products = { "ChikenMeat" };
    private static final String[] canEat = { "Corn", "Wheat", "Millet", "Rye" };
    // кукурудза, пшениця, просо, жито

    public int getPrice() {
        return price;
    }

    public String getName() {
        return Pig.class.getSimpleName();
    }

    public String[] getProducts() {
        return Products;
    }

    public String[] canEat() {
        return canEat;
    }
}
