package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import agro.technology.Product.Product;

public interface IWareHouseService {
    public String getWareHouseType();

    public void initProcesess(WareHouse wareHouse);

    public WareHouse create(String type, String location, int size, int capacity, double freshness,
            ArrayList<Product> storage);

    public WareHouse load(String type, String location, int size, int capacity, double freshness,
            ArrayList<Product> storage, JsonObject json);
    
    public List<String> getActionsList();

    public void doAction(WareHouse wareHouse, String actionType);
}
