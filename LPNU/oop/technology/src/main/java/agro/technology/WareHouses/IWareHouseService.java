package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import agro.technology.WareHouses.WareHouse.StorageCell;

public interface IWareHouseService {
    public String getWareHouseType();

    public void initProcesess(WareHouse wareHouse);

    public WareHouse create(String type, String location, int size, int capacity, double freshness,
            ArrayList<StorageCell> storage);

    public WareHouse load(String type, String location, int size, int capacity, double freshness,
            ArrayList<StorageCell> storage, JsonObject json);
    
    public List<String> getActionsList();

    public void doAction(WareHouse wareHouse, String actionType);
}
