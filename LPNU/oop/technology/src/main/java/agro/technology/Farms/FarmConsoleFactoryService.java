package agro.technology.Farms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import agro.technology.Farms.Farm.Farm;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

/**
 * <h3>працює отак:</h3>
 * Spring автоматично викликає конструктор і інжектить
 * у нього всі знайдені класи помічені як боби (або компонент)
 * далі я засуваю їх у хеш-мап де вони за їхнім типом зберігаються
 * <p/>
 * метод створити ферму має отримати тип
 * далі за ключем найти потрібну фаюрику і викликати створення
 */
@Service
public class FarmConsoleFactoryService {

    private final CLI terminal;
    private final Map<String, IFarmConsoleFactory> farms;

    public FarmConsoleFactoryService(List<IFarmConsoleFactory> farms, CLI terminal) {
        HashMap<String, IFarmConsoleFactory> farmMap = new HashMap<>();

        for (IFarmConsoleFactory farm : farms)
            farmMap.put(farm.getType(), farm);
        this.farms = farmMap;
        this.terminal = terminal;
    }

    public Farm createFarm(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {
        IFarmConsoleFactory farmFactory = farms.get(type);
        if (farmFactory == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined type", Colors.RED);
        Farm farm = farmFactory.create(type, location, name, wareHouse, workers);
        farm.initProcess();
        return farm;
    }

    public void specializationAction(Farm farm){
        IFarmConsoleFactory farmFactory = farms.get(farm.getType());
        farmFactory.specializationAction(farm);
    }

    /**
     * повертає ліст з типів ферм
     * @see IFarmConsoleFactory інтерфейс генерації типів
     */
    public Set<String> getFarmsList(){
        return farms.keySet();
    }
}
