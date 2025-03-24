package agro.technology.WareHouses;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.utils.terminal;

@Service
public class WareHousesFactoryService {

    private terminal terminal;
    private Map<String, IWareHouseFactory> wareHouses;
    private Map<String, ISpecificationAction> actions;

    public WareHousesFactoryService(List<IWareHouseFactory> wareHouses, terminal terminal,
            List<ISpecificationAction> specificationActions) {

        this.wareHouses = new java.util.HashMap<>();
        this.actions = new java.util.HashMap<>();
        for (IWareHouseFactory wareHouse : wareHouses)
            this.wareHouses.put(wareHouse.getType(), wareHouse);
        for (ISpecificationAction action : specificationActions)
            actions.put(action.getType(), action);
        this.terminal = terminal;

    }

    public WareHouse createWareHouse(String type, String location, int size) {
        IWareHouseFactory wareHouseFactory = wareHouses.get(type);
        if (wareHouseFactory == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined type", 1);
        return wareHouseFactory.create(type, location, size);
    }

    /**
     * повертає ліст з типів складів
     * 
     * @see IWareHouseFactory інтерфейс генерації типів
     */
    public List<String> getWareHousesList() {
        return wareHouses.keySet().stream().collect(Collectors.toList());
    }

    public List<String> getActions(String type) {
        ISpecificationAction action = actions.get(type);
        if (action == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined type", 1);
        return action.getActionsList();
    }

    public void takeAction(WareHouse wareHouse, String type) {
        ISpecificationAction action = actions.get(type);
        if (action == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined type", 1);
        action.action(wareHouse, type);
        ;
    }

}
