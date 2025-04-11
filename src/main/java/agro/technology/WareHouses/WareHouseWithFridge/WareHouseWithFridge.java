package agro.technology.WareHouses.WareHouseWithFridge;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import agro.technology.Product.Product;
import agro.technology.WareHouses.WareHouse;
import lombok.Getter;

@Getter
public class WareHouseWithFridge extends WareHouse {
    @Expose
    protected double fridgeWithAccumulator;

    /**
     * склад з спеціальним холодильником
     */
    public WareHouseWithFridge(String type, String location, int size, int capacity, double freshness, ArrayList<Product> storage, double fridgeWithAccumulator) {
        super(type, location, size, capacity, freshness, storage);
        this.fridgeWithAccumulator = fridgeWithAccumulator;
    }

    
}