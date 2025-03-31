package agro.technology.Farms;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;

/**
 * інтерфейс для консольних фабрик ферми
 */
public interface IFarmService {
    public String getFarmType();

    public String specializationActionLabel();

    public void specializationAction(Farm farm);

    public Farm createViaCLI(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers);

    public void initProcesess(Farm farm);

    public Farm load(String type,
    String location,
    String name,
    WareHouse wareHouse,
    ArrayList<Worker> workers,
    JsonObject jsonData);

    public String report(Farm farm);
}
