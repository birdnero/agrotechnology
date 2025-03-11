package org.agrotechnology.Farm;

import java.util.ArrayList;

import org.agrotechnology.FarmProperty.Barn.Barn;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;

import com.google.gson.annotations.Expose;

public class AnimalFarm extends Farm {
    @Expose
    private Barn barn;

    /**
     * Ферма тварин, потребує додатково домашнього улюбленця😊
     * 
     * @param name      - ім'я ферми
     * @param location  - локація
     * @param wareHouse - склад (створити і передати)
     * @param workers   - працівники (передати власно створений ArrayList або
     *                  WorkerUtil.workerArrGenerator)
     * @param petHouse  - //!
     * @param barnType  - //!
     * @param barnSize  - //!
     */
    public AnimalFarm(
            String name,
            String location,
            WareHouse wareHouse,
            ArrayList<Worker> workers,
            /////////////////////////
            Barn barn) { // ? Barn - place for animals

        super(AnimalFarm.class.getSimpleName(), location, name, wareHouse, workers);
        this.barn = barn;
    }

    public AnimalFarm(String type, String name, String location, WareHouse wareHouse, ArrayList<Worker> workers,
            Barn barn) {
        super(type, location, name, wareHouse, workers);
        this.barn = barn;
    }

    @Override
    protected void initProcessHook() {
        barn.process();
        
    }

    @Override
    protected String useReportHook() {
        return barn.report();
    }

    public Barn getBarn() {
        return barn;
    }

}
