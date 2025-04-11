package agro.technology.WareHouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import agro.technology.Product.Product;

@Service
class WareHousePrimaryService implements IWareHouseService {

    public WareHousePrimaryService(){
        
    }

    @Override
    public WareHouse create(String type, String location, int size, int capacity, double freshness,
            ArrayList<Product> storage) {
        WareHouse wareHouse = new WareHouse(getWareHouseType(), location, size, capacity, freshness, storage);

        initProcesess(wareHouse);

        return wareHouse;
    }

    @Override
    public void doAction(WareHouse wareHouse, String actionType) {
        return;
    }

    @Override
    public List<String> getActionsList() {
        return new ArrayList<>();
    }

    @Override
    public String getWareHouseType() {
        return "WareHouse";
    }

    @Override
    public void initProcesess(WareHouse wareHouse) {
        return;
    }

    @Override
    public WareHouse load(String type, String location, int size, int capacity, double freshness,
            ArrayList<Product> storage, JsonObject json) {
        return create(type, location, size, capacity, freshness, storage);
    }

}
