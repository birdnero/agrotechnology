package agro.technology.Farms.PlantFarm.Field.Plants;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class PlantService {
    private Path plantJson = Path.of("src/main/java/agro/technology/Farms/PlantFarm/Field/Plants/Plants.json");
    private HashMap<String, Plant> plants;
    private Gson gson;
    private final CLI terminal;

    public PlantService(CLI terminal) {
        this.terminal = terminal;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try (FileReader file = new FileReader(plantJson.toFile())) {

            Type typeToken = new TypeToken<List<Plant>>() {
            }.getType();
            List<Plant> plants = gson.fromJson(file, typeToken);
            HashMap<String, Plant> plantsMap = new HashMap<>();
            for (Plant plant : plants)
                plantsMap.put(plant.getName(), plant);
            this.plants = plantsMap;

        } catch (IOException e) {
            terminal.errorExit(this.getClass().getSimpleName() + ": error reading " + plantJson.getFileName());
        }
    }

    public List<Plant> getPlants() {
        return plants.values().stream().collect(Collectors.toList());
    }

    public List<String> getPlantTypes() {
        return plants.keySet().stream().collect(Collectors.toList());
    }

    public void addPlant(String name, int buyPrice, double dabler) {
        plants.put(name, new Plant(name, buyPrice, dabler));
        sync();
    }

    public void addPlant(Plant plant) {
        plants.put(plant.getName(), plant);
        sync();
    }

    public Plant getPlant(String name) {
        Plant plant = plants.get(name);
        if (plant == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined plant", Colors.RED);
        return plant;
    }

    private void sync() {
        try (FileWriter file = new FileWriter(plantJson.toFile())) {
            gson.toJson(plants.values(), file);
        } catch (Exception e) {
            terminal.previewing(this.getClass().getSimpleName() + ": sync plants error", Colors.RED);
        }
    }
}

/*
 * 
 * 
 * private static final Plant[] plantList = new Plant[] {
 * new Plant("Corn", 4, 3, 2),
 * new Plant("Tomato", 11, 5, 10),
 * new Plant("Wheat", 2, 1, 2.5),
 * new Plant("Millet", 2, 2, 1.5), // просо
 * new Plant("Rye", 1, 1, 2), // жито
 * new Plant("Buckwheat", 9, 10, 1), // гречка
 * new Plant("Potato", 3, 3, 4),
 * new Plant("Cucumber", 7, 4, 3),
 * new Plant("Salad", 1, 3, 6),
 * new Plant("Strawberry", 9, 10, 1.7),
 * new Plant("Rice", 3, 1, 9),
 * new Plant("pumpkin", 7, 2, 0.1),
 * new Plant("watermelon", 4, 15, 1),
 * };
 * 
 * 
 */