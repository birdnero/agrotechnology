package agro.technology.Farms.AnimaFarm.Barn.Animals;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
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
}
