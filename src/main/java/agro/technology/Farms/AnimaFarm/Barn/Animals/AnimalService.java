package agro.technology.Farms.AnimaFarm.Barn.Animals;

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
public class AnimalService {
    private Path pathToFile = Path.of("src/main/java/agro/technology/Farms/AnimaFarm/Barn/Animals/Animal.json");
    private HashMap<String, Animal> animals;
    private Gson gson;

    private CLI terminal;

    public AnimalService(CLI terminal) {
        this.terminal = terminal;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        
        loadAnimals();
    }

    public void loadAnimals() {
        try (FileReader file = new FileReader(pathToFile.toFile())) {
            Type typeToken = new TypeToken<List<Animal>>() {
            }.getType();

            List<Animal> animals = gson.fromJson(file, typeToken);
            HashMap<String, Animal> animalsMap = new HashMap<>();
            for (Animal animal : animals) {
                animalsMap.put(animal.getName(), animal);
            }
            this.animals = animalsMap;

        } catch (IOException e) {
            terminal.errorExit(this.getClass().getSimpleName() + ": error reading " + pathToFile.getFileName());
        }
    }

    public List<String> getAnimalTypes() {
        return animals.keySet().stream().collect(Collectors.toList());
    }

    public List<Animal> getAnimals() {
        return animals.values().stream().collect(Collectors.toList());
    }

    public void addAnimal(String name, int price, double life, String[] products, String[] canEat, int sellPrice) {
        animals.put(name, new Animal(name, sellPrice, price, products, canEat, life));
        sync();
    }

    public void addAnimal(Animal animal) {
        animals.put(animal.getName(), animal);
        sync();
    }

    public Animal getAnimal(String name) {
        Animal animal = animals.get(name);
        if (animal == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined animal", Colors.RED);
        return animal;
    }

    public void setAnimal(Animal animal, String oldName) {
        animals.remove(oldName);
        animals.put(animal.getName(), animal);
        sync();
    }

    public void deleteAnimal(String name) {
        Animal animal = animals.get(name);
        if (animal == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined animal", Colors.RED);
        else
        animals.remove(animal.getName());
        sync();
    }

    private void sync() {
        try (FileWriter file = new FileWriter(pathToFile.toFile())) {
            gson.toJson(animals.values(), file);
            // loadAnimals();
        } catch (Exception e) {
            terminal.previewing(this.getClass().getSimpleName() + ": sync animals error", Colors.RED);
        }
    }
}
