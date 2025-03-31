package agro.technology.Farms;

import java.util.ArrayList;

import agro.technology.Farms.Farm.Farm;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;

/**
 * інтерфейс для консольних фабрик ферми
 */
public interface IFarmConsoleFactory {
    public String getType();

    public Farm create( String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers);


    public void specializationAction(Farm farm);
}
