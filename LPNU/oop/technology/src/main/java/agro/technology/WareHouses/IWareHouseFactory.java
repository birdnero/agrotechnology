package agro.technology.WareHouses;

import agro.technology.WareHouses.WareHouse.WareHouse;

public interface IWareHouseFactory {
    public String getType();
    public WareHouse create(String type, String location, int size);
}
