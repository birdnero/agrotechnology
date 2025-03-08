package org.agrotechnology.Farm;

import java.util.ArrayList;

import org.agrotechnology.FarmAdditional.Equipment;
import org.agrotechnology.FarmProperty.Field.Field;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;

public class PlantFarm extends Farm {
    protected ArrayList<Equipment> equipments;
    private Field field;

    /**
     * Ферма рослин, має поле і спорядження 🍅
     * 
     * @param name       - ім'я ферми
     * @param location   - локація
     * @param size       - розмір (m²)
     * @param wareHouse  - склад (створити і передати)
     * @param workers    - працівники (передати власно створений ArrayList або
     *                   WorkerUtil.workerArrGenerator)
     * @param equipments - спорядження //!
     * @param field      - //!
     */
    public PlantFarm(
            String name,
            String location,
            int size,
            WareHouse wareHouse,
            ///////////////////////
            ArrayList<Worker> workers,
            ArrayList<Equipment> equipments,
            Field field) {

        super(name, location, size, wareHouse, workers);
        this.field = field;
        this.equipments = equipments;
    }

    protected void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    @Override
    protected String useReportHook() {
        StringBuilder str = new StringBuilder();
        for (Equipment equipment : equipments) {
            str.append(equipment.report());
        }
        str.append(this.field.report());
        str.append(finalReportHook());
        return str.toString();
    }

    protected String finalReportHook() {
        return "";
    }
}
