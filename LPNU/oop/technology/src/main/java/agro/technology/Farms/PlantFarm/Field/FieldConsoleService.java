package agro.technology.Farms.PlantFarm.Field;

import org.springframework.stereotype.Service;
import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.utils.terminal;

@Service
public class FieldConsoleService {

    private final PlantService plantService;
    private final terminal terminal;

    public FieldConsoleService(PlantService plantService, terminal terminal){
        this.plantService = plantService;
        this.terminal = terminal;
    }
    
    public Field create(){
        int fieldSize = terminal.inputNumber(()->terminal.optionsLabel("Field size (m²)"));

        int fieldType = terminal.initOptions(plantService.getPlantTypes().toArray(), () -> {
        }, () -> terminal.optionsLabel("select type of plant"));

        return new Field(plantService.getPlantTypes().get(fieldType), fieldSize);
    }
}
