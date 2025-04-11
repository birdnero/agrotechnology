package agro.technology.WareHouses;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.Expose;

import agro.technology.Product.Product;

// #lab використано інтерфейс ну і ще це хороший приклад абстракції
@AllArgsConstructor
@Getter
@Setter
public class WareHouse {
    
    @Expose
    private String type;
    @Expose
    private String location;
    @Expose
    private int size;
    @Expose
    protected int capacity;
    @Expose
    protected double freshness;
    @Expose
    protected ArrayList<Product> storage = new ArrayList<Product>();
    
}
