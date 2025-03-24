package agro.technology.Farms.PlantFarm;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import agro.technology.Farms.Farm.Farm;
import agro.technology.Farms.PlantFarm.Field.Field;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;

public class PlantFarm extends Farm {
    @Expose
    private Field field;

    /**
     * Ферма рослин, має поле і спорядження 🍅
     * 
     * @param name       - ім'я ферми
     * @param location   - локація
     * @param wareHouse  - склад (створити і передати)
     * @param workers    - працівники (передати власно створений ArrayList або
     *                   WorkerUtil.workerArrGenerator)
     * @param field      - //!
     */
    public PlantFarm(
            String name,
            String location,
            WareHouse wareHouse,
            ///////////////////////
            ArrayList<Worker> workers,
            Field field) {

        super(PlantFarm.class.getSimpleName(), location, name, wareHouse, workers, field.getClass().getSimpleName());
        this.field = field;
    }

    

    public PlantFarm(String type, String name, String location, WareHouse wareHouse, ArrayList<Worker> workers,
            Field field) {
        super(type, location, name, wareHouse, workers, field.getClass().getSimpleName());
        this.field = field;
    }



    @Override
    protected void initProcessHook() {
        field.process();
    }

    @Override
    protected String useReportHook() {
        StringBuilder str = new StringBuilder("");
        str.append(this.field.report());
        return str.toString();
    }

    public Field getField() {
        return field;
    }

}
