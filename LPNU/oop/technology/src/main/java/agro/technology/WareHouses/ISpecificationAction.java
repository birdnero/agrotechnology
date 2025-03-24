package agro.technology.WareHouses;

import java.util.List;

import agro.technology.WareHouses.WareHouse.WareHouse;

public interface ISpecificationAction {
    public String getType();
    public void action(WareHouse wareHouse, String actionType);
    public List<String> getActionsList();
}
