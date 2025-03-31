package agro.technology.Farms.PlantFarm.Field;

import org.springframework.stereotype.Service;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.utils.CLI;

@Service
public class FieldConsoleService {

    private final PlantService plantService;
    private final CLI terminal;

    public FieldConsoleService(PlantService plantService, CLI terminal){
        this.plantService = plantService;
        this.terminal = terminal;
    }
    
    public Field create(){
        int fieldSize = terminal.inputNumber(()->terminal.print(terminal.optionsLabel("Field size (m²)")));

        int fieldType = terminal.initOptions(plantService.getPlantTypes().toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("select type of plant")));

        return new Field(plantService.getPlantTypes().get(fieldType), fieldSize);
    }
}
