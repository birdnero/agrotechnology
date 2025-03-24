package agro.technology.WareHouses.WareHouse;

import org.springframework.stereotype.Service;

import agro.technology.WareHouses.IWareHouseFactory;

@Service
public class WareHouseFactoryService implements IWareHouseFactory {

    @Override
    public WareHouse create(String type, String location, int size) {
        return new WareHouse(type, location, size);
    }

    @Override
    public String getType() {
        return "WareHouse";
    }
    
}
