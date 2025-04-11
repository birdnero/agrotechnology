package agro.technology.Farms.AnimaFarm.Barn.Animals;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Animal {
    @Expose
    protected String name;
    @Expose
    protected int buyPrice;
    @Expose
    protected String[] Products;
    @Expose
    protected String[] canEat;
    @Expose
    protected double life;



    public Animal(String name, int sellPrice, int buyPrice, String[] products, String[] canEat, double life) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.Products = products;
        this.canEat = canEat;
        this.life = life;
    }

}
