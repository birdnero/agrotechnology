package agro.technology.Farms;

import java.util.ArrayList;


import com.google.gson.annotations.Expose;

import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public abstract class Farm {

    @Expose
    protected String type;
    @Expose
    protected String location;
    @Expose
    protected String name;
    @Expose
    protected WareHouse wareHouse;
    @Expose
    protected ArrayList<Worker> workers;

}
