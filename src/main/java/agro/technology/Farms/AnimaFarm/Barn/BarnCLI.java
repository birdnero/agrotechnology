package agro.technology.Farms.AnimaFarm.Barn;

import org.springframework.stereotype.Service;

import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;
import agro.technology.utils.CLI;

@Service
public class BarnCLI {

    private final BarnService barnService;

    private final AnimalService animalService;
    private final CLI terminal;


    public BarnCLI(AnimalService animalService, CLI terminal, BarnService barnService) {
        this.animalService = animalService;
        this.terminal = terminal;
        this.barnService = barnService;
    }
    

    public Barn create(){
        int barnType = terminal.initOptions(animalService.getAnimalTypes().toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("Select type of animals")));

        if(barnType == -1){
            terminal.print("animal types are not implemented yet.. \nwe working on it.. .. ..");
            while (!terminal.keyAction(127)) {
                
            }
            return null;
        }  

        int maxAmount = terminal.inputNumber(() -> terminal.print(terminal.optionsLabel("Barn max animal amount: ")));

        Barn barn = barnService.create(animalService.getAnimalTypes().get(barnType), maxAmount);

        return barn;
    }

    public String report(Barn barn) {
        
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(animalService.getAnimal(barn.getType()).getName() + " barn"));
        str.append(terminal.formatDataValue("size", barn.getSize() + " m²"));
        str.append(terminal.formatDataValue(animalService.getAnimal(barn.getType()).getName() + "s amount", barn.animalsAmount));
        str.append(terminal.formatDataValue("production amount", barn.productionAmount));
        str.append(terminal.formatDataValue("feed", barn.getFeedAmount()));

        str.append("\n");

        return str.toString();
    }
}
