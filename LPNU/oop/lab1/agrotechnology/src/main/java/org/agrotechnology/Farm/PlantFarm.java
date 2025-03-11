package org.agrotechnology.Farm;

import java.util.ArrayList;

import org.agrotechnology.FarmProperty.Field.Field;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;

import com.google.gson.annotations.Expose;

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

        super(PlantFarm.class.getSimpleName(), location, name, wareHouse, workers);
        this.field = field;
    }

    

    public PlantFarm(String type, String name, String location, WareHouse wareHouse, ArrayList<Worker> workers,
            Field field) {
        super(type, location, name, wareHouse, workers);
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
