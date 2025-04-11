package agro.technology.Farms.PlantFarm;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import agro.technology.Farms.Farm;
import agro.technology.Farms.PlantFarm.Field.Field;
import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;
import lombok.Getter;

@Getter
public class PlantFarm extends Farm {
    @Expose
    private Field field;

    public static final String type = "Plant Farm";

    public PlantFarm(
            String name,
            String location,
            WareHouse wareHouse,
            ///////////////////////
            ArrayList<Worker> workers,
            Field field) {
        super(type, location, name, wareHouse, workers);
        this.field = field;
    }

}