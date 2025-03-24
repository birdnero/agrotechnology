package agro.technology.Farms.AnimaFarm;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import agro.technology.Farms.AnimaFarm.Barn.Barn;
import agro.technology.Farms.Farm.Farm;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;

/**
 * <h3>Ферма тварин, містить додаткове поле barn</h3>
 */
// #lab використано наслідування
public class AnimalFarm extends Farm {
    @Expose
    private Barn barn;

    /**
     * @param name      - ім'я ферми
     * @param location  - локація
     * @param wareHouse - склад (створити і передати)
     * @param workers   - працівники (передати власно створений ArrayList або
     *                  WorkerUtil.workerArrGenerator)
     * @param barn      - приміщення для тварин (створити і передати)
     */
    public AnimalFarm(
            String name,
            String location,
            WareHouse wareHouse,
            ArrayList<Worker> workers,
            /////////////////////////
            Barn barn) { // ? Barn - place for animals

        super(AnimalFarm.class.getSimpleName(), location, name, wareHouse, workers, barn.getClass().getSimpleName());
        this.barn = barn;
    }

    /**
     * @param type - поле для визначення підкласу ( для правильної (де)серіалізації )
     * @param name      - ім'я ферми
     * @param location  - локація
     * @param wareHouse - склад (створити і передати)
     * @param workers   - працівники (передати власно створений ArrayList або
     *                  WorkerUtil.workerArrGenerator)
     * @param barn      - приміщення для тварин (створити і передати)
     */
    public AnimalFarm(String type, String name, String location, WareHouse wareHouse, ArrayList<Worker> workers,
            Barn barn) {
        super(type, location, name, wareHouse, workers, barn.getClass().getSimpleName());
        this.barn = barn;
    }

    // #lab використано поліморфізм (насправді він тут буквально всюди це як маленький приклад)
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
