package agro.technology.Farms.PlantFarm.Field;

import agro.technology.Farms.PlantFarm.Field.Plants.PlantService;
import agro.technology.utils.CLI;
import org.springframework.stereotype.Service;

@Service
public class FieldCLI {

    private final PlantService plantService;

    private final FieldService fieldService;

    private final CLI terminal;



    public FieldCLI(CLI cli, FieldService fieldService, PlantService plantService){
        this.terminal = cli;
        this.fieldService = fieldService;
        this.plantService = plantService;
    }

    public Field create() {
        int fieldSize = terminal.inputNumber(()->terminal.print(terminal.optionsLabel("Field size (m²)")));

        int fieldType = terminal.initOptions(plantService.getPlantTypes().toArray(), () -> {
        }, () -> terminal.print(terminal.optionsLabel("select type of plant")));

        if(fieldType == -1){
            terminal.print("plant types are not implemented yet.. \nwe working on it.. .. ..");
            while (!terminal.keyAction(127)) {
                
            }
            return null;
        }

        String type = plantService.getPlantTypes().get(fieldType);

        Field field = fieldService.create(type, fieldSize);

        return field;
    }


    public String report(Field field) {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName("Field of " + plantService.getPlant(field.getType()).getName()));
        str.append(terminal.formatDataValue("size", field.getSize() + " m²"));
        str.append(terminal.formatDataValue("sown", field.sown));
        str.append(terminal.formatDataValue("has ripened", field.ripened));
        str.append("\n");

        return str.toString();
    }
    
}
