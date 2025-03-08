package org.agrotechnology.Farm;

import java.util.ArrayList;

import org.agrotechnology.FarmAdditional.Kennel;
import org.agrotechnology.FarmProperty.Barn.Barn;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;

public class AnimalFarm extends Farm {

    protected Kennel petHouse;// ? Kennel - place for dog
    private Barn barn;

    /**
     * Ферма тварин, потребує додатково домашнього улюбленця😊
     * 
     * @param name      - ім'я ферми
     * @param location  - локація
     * @param size      - розмір (m²)
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
            int size,
            WareHouse wareHouse,
            ArrayList<Worker> workers,
            /////////////////////////
            Kennel petHouse,
            Barn barn) { // ? Barn - place for animals

        super(name, location, size, wareHouse, workers);
        this.barn = barn;
        this.petHouse = petHouse;
    }

    @Override
    protected String useReportHook() {
        return petHouse.report() + barn.report();
    }

}
