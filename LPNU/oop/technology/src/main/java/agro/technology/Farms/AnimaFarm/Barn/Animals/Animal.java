package agro.technology.Farms.AnimaFarm.Barn.Animals;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Animal {
    @Expose
    protected int price;
    @Expose
    protected String[] Products;
    @Expose
    protected String[] canEat;
    @Expose
    protected String name;
    @Expose
    protected double life;
    @Expose
    protected int sellPrice;



    public Animal(int price, String[] products, String[] canEat, String name, double life, int sellPrice) {
        this.price = price;
        Products = products;
        this.canEat = canEat;
        this.name = name;
        this.life = life;
        this.sellPrice = sellPrice;
    }

}
