package agro.technology.Farms.AnimaFarm.Barn.Animals;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import agro.technology.utils.terminal;

@Service
public class AnimalService {
    private Path animalJson = Path.of("/src/main/java/agro/technology/Farms/AnimaFarm/Barn/Animals/Animals.json");
    private HashMap<String, Animal> animals;
    private Gson gson;

    private terminal terminal;

    @Autowired
    public AnimalService(terminal terminal){
        this.terminal = terminal;
    }

    public AnimalService() {
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try (FileReader file = new FileReader(animalJson.toFile())) {
            Type typeToken = new TypeToken<List<Animal>>() {
            }.getType();

            List<Animal> animals = gson.fromJson(file, typeToken);
            HashMap<String, Animal> animalsMap = new HashMap<>();
            for (Animal animal : animals)
                animalsMap.put(animal.getName(), animal);
            this.animals = animalsMap;

        } catch (IOException e) {
            terminal.errorExit(this.getClass().getSimpleName() + ": error reading " + animalJson.getFileName());
        }
    }

    public List<String> getAnimalTypes() {
        return animals.keySet().stream().collect(Collectors.toList());
    }

    public List<Animal> getAnimals() {
        return animals.values().stream().collect(Collectors.toList());
    }

    public void addAnimal(String name, int price, double life, String[] products, String[] canEat, int sellPrice) {
        animals.put(name, new Animal(price, products, canEat, name, life, sellPrice));
        sync();
    }

    public void addAnimal(Animal animal) {
        animals.put(animal.getName(), animal);
        sync();
    }

    public Animal getAnimal(String name) {
        Animal animal = animals.get(name);
        if (animal == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined animal", 1);
        return animal;
    }


    private void sync(){
         try (FileWriter file = new FileWriter(animalJson.toFile())) {
            gson.toJson(animals.values(), file);
        } catch (Exception e) {
            terminal.previewing(this.getClass().getSimpleName() + ": sync animals error", 1);
        }
    }
}
