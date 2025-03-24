package agro.technology.WareHouses.WareHouseWithFridge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import agro.technology.WareHouses.ISpecificationAction;
import agro.technology.WareHouses.IWareHouseFactory;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.utils.terminal;

@Service
public class WithFridgeFactoryService implements IWareHouseFactory, ISpecificationAction {

    private final Map<String, Consumer<WareHouseWithFridge>> actionsList;
    private final terminal terminal;

    public void chargeFridge(WareHouseWithFridge wareHouse) {
        if (wareHouse.chargeFridge()) {
            terminal.previewing("seccefully charged", 2);
        } else {
            terminal.previewing("no unough money :(", 1);
        }
    }

    public WithFridgeFactoryService(terminal terminal) {
        this.terminal = terminal;
        HashMap<String, Consumer<WareHouseWithFridge>> actions = new HashMap<>();

        actions.put("charge fridge", w -> chargeFridge(w));

        this.actionsList = actions;
    }

    @Override
    public WareHouse create(String type, String location, int size) {
        return new WareHouseWithFridge(type, location, size);
    }

    @Override
    public String getType() {
        return "WareHouse with fridge";
    }

    @Override
    public void action(WareHouse wareHouse, String type) {
        Consumer<WareHouseWithFridge> act = this.actionsList.get(type);
        if (act == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined type", 1);
        else
            act.accept(((WareHouseWithFridge) wareHouse));
    }

    @Override
    public List<String> getActionsList() {
        return this.actionsList.keySet().stream().collect(Collectors.toList());
    }

}
