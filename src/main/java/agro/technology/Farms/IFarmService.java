package agro.technology.Farms;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import agro.technology.WareHouses.WareHouse;
import agro.technology.Worker.Worker;

/**
 * інтерфейс для консольних фабрик ферми
 */
public interface IFarmService {
    public String getFarmType();// ? визначення типу фабрики

    public String specializationActionLabel();

    public void specializationAction(Farm farm);

    public Farm createViaCLI(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers);// ? метод рівноцінний create()|get() з патерну Abstract Factory

    public void initProcesess(Farm farm);

    public Farm load(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers,
            JsonObject jsonData);// ? ще один підваріант того як працює фабрика, але менш стандартний

    public String report(Farm farm);
}

// ? класи plantFarm & AnimalFarm реалізують цей інтерфейс,
// ? а отже вони є фабриками
