package agro.technology.Farms.PlantFarm.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.annotations.Expose;

@Getter
@AllArgsConstructor
public class Field {

    
    // ? to sow - сіяти
    // ? ripened - достигло
    @Expose
    private String type;
    @Expose
    private int size;
    @Expose
    protected int sown;
    @Expose
    protected int ripened;
    @Expose
    protected double waterLevel;
    
}
