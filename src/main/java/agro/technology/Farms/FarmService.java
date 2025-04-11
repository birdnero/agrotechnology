package agro.technology.Farms;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import agro.technology.WareHouses.WareHouse;
import agro.technology.WareHouses.WareHouseService;
import agro.technology.Worker.Worker;
import agro.technology.Worker.WorkerFactory;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class FarmService {

    private final WorkerFactory workerFactory;

    private final WareHouseService wareHouseService;

    private final CLI terminal;
    private final List<IFarmService> farmServices;
    private Gson gson;
    private static final Path farmsPath = Paths.get("src/main/java/agro/technology/Farms/Farms.json");

    private static List<Farm> farms;

    public FarmService(List<IFarmService> farmServices, CLI terminal, WareHouseService wareHouseService,
            WorkerFactory workerFactory) {
        this.farmServices = farmServices;
        this.wareHouseService = wareHouseService;
        this.terminal = terminal;
        this.workerFactory = workerFactory;
        this.gson = new GsonBuilder() // ? ця штука дозволяє десеріалізувати з розтипізацією на plantFarm & AnimalFarm
                                      // з Farm
                .registerTypeAdapter(Farm.class, new AdvancedFarmDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    public IFarmService searchService(String type) {
        for (IFarmService farmService : farmServices) {
            if (farmService.getFarmType().equals(type))
                return farmService;
        }
        terminal.previewing("can't find type " + type, Colors.RED);
        sync();
        return null;
    }

    // ? Abstract Factory: IFarmService farmFactory - отримує потрібну фабрику і
    // ? якщо така існує, викликає метод .createViaCLI(...) для створення 
    // ? потрібного екземпляру
    public Farm createFarm(String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {

        IFarmService farmFactory = searchService(type); //? перейди у IFarmService
        if (farmFactory == null)
            terminal.previewing(this.getClass().getSimpleName() + ": undefined type", Colors.RED);
        Farm farm = farmFactory.createViaCLI(type, location, name, wareHouse, workers);
        if (farm == null) {
            terminal.previewing("something went wrong, when creating a farm", Colors.RED);
            return null;
        }
        initProcess(farm);
        return farm;
    }

    public void initProcess(Farm farm) {
        searchService(farm.getType()).initProcesess(farm);
    }

    class AdvancedFarmDeserializer implements JsonDeserializer<Farm> {
        /**
         * json - містить дані
         * typeOfT - фігня
         * context - ще одна дивна фігня
         */
        @Override
        public Farm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject(); // ? просто перетворює у пари ключ значення

            String type = jsonObject.get("type").getAsString();
            String name = jsonObject.get("name").getAsString();
            String location = jsonObject.get("location").getAsString();

            JsonObject wareHouseObject = jsonObject.getAsJsonObject("wareHouse");
            WareHouse wareHouse = wareHouseService.load(wareHouseObject);

            JsonArray workersArray = jsonObject.getAsJsonArray("workers");
            ArrayList<Worker> workers = workerFactory.load(workersArray);

            return searchService(type).load(type, location, name, wareHouse, workers, jsonObject);
        }

    }

    public void load() {

        try {
            if (Files.notExists(farmsPath)) { // ? якщо файлу не існує
                Files.createDirectories(farmsPath.getParent());// ? створити потрібну папку
                Files.createFile(farmsPath);// ? створити потрібний файл
                Files.writeString(farmsPath, "[]");// ? і засунути туди файл
            }
        } catch (IOException e) {
            terminal.errorExit("Smth wrong with loadFarms file creation " + e);
        }

        // ? конструкція яка автоматично закриє файл і тд. короче корисно
        try (FileReader file = new FileReader(farmsPath.toFile())) {

            Type typeForFarms = new TypeToken<List<Farm>>() {
            }.getType();// ? дивна штука яка правильно (за версією chatGPT) типізує json файл

            ArrayList<Farm> farmsArr = this.gson.fromJson(file, typeForFarms);
            farms = farmsArr;
            for (Farm farm : farms)
                searchService(farm.getType()).initProcesess(farm);

        } catch (IOException e) {
            terminal.errorExit("DataManager Farms error " + e);
            System.exit(0);
        }
    }

    public void sync() {
        try (FileWriter file = new FileWriter(farmsPath.toFile())) {
            gson.toJson(farms, file);
        } catch (IOException e) {
            terminal.errorExit("sync farms error");
        }
    }

    public List<Farm> getFarms() {
        return farms;
    }

    public List<IFarmService> getFarmServices() {
        return farmServices;
    }

}
