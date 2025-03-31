package agro.technology.Farms.AnimaFarm.Barn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.utils.CLI;

@Service
public class BarnConsoleService {
    private final AnimalService animalService;
    private final CLI terminal;

    @Autowired
    private ApplicationContext context;

    public BarnConsoleService(AnimalService animalService, CLI terminal) {
        this.animalService = animalService;
        this.terminal = terminal;
    }

    public Barn create() {
        int barnType = terminal.initOptions(animalService.getAnimalTypes().toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("Select type of animals")));

        int maxAmount = terminal.inputNumber(() -> terminal.print(terminal.optionsLabel("Barn max animal amount: ")));

        Barn barn = context.getBean(Barn.class);
        barn.constructor(animalService.getAnimalTypes().get(barnType), maxAmount);
        return barn;
    }
}

/*
 * 
 * 
 * private static final Animal[] AnimalList = new Animal[] {
 * new Animal("Chiken", 6, 0.25, new String[] { "ChikenMeat" },
 * new String[] { "Corn", "Wheat", "Millet", "Rye" }, 3),
 * 
 * new Animal("Pig", 45, 0.4, new String[] { "Pork" },
 * new String[] { "Corn", "Tomato", "Wheat", "Millet", "Rye", "Buckwheat",
 * "Potato" }, 5),
 * 
 * new Animal("Sheep", 60, 0.47, new String[] { "Wool", "Mutton" }, new String[]
 * { "Grass", "Herb", "Hay" }, 5),
 * 
 * new Animal("Cow", 150, 0.8, new String[] { "Milk", "Beef" }, new String[] {
 * "Grass", "Herb", "Hay" }, 3),
 * 
 * new Animal("Антон", 13, 0.04, new String[] { "slave meat" }, new String[] {
 * "Wheat", "Grass" }, 2),
 * };
 * 
 * 
 */