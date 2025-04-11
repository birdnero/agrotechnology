package agro.technology.Farms.AnimaFarm;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import agro.technology.Farms.Farm;
import agro.technology.Farms.AnimaFarm.Barn.Barn;
import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;
import lombok.Getter;

/**
 * <h3>Ферма тварин, містить додаткове поле barn</h3>
 */
@Getter
public class AnimalFarm extends Farm {
    @Expose
    private Barn barn;

    public static final String type = "Animal Farm";

    public AnimalFarm(
            String name,
            String location,
            WareHouse wareHouse,
            ArrayList<Worker> workers,
            /////////////////////////
            Barn barn) { // ? Barn - place for animals
        super(type, location, name, wareHouse, workers);
        this.barn = barn;
    }

}
