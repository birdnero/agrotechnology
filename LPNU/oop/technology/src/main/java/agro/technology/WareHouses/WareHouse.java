package agro.technology.WareHouses;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.Expose;

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
    protected ArrayList<StorageCell> storage = new ArrayList<StorageCell>();

    @Getter
    @AllArgsConstructor
    public static class StorageCell {
        @Expose
        String type;
        @Expose
        int amount;
    }

    
}
