package agro.technology.Farms.PlantFarm.Field.Plants;

import com.google.gson.annotations.Expose;

import lombok.Getter;


@Getter
public class Plant {
    @Expose
    private String name;
    @Expose
    private int buyPrice;
    @Expose
    private double dabler;

    public Plant(String name, int buyPrice, double dabler){
        this.name = name;
        this.buyPrice = buyPrice;
        this.dabler = dabler;
    }
}
