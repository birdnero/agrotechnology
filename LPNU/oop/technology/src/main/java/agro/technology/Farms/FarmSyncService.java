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

import agro.technology.Farms.AnimaFarm.AnimalFarm;
import agro.technology.Farms.AnimaFarm.Barn.Barn;
import agro.technology.Farms.Farm.Farm;
import agro.technology.Farms.PlantFarm.PlantFarm;
import agro.technology.Farms.PlantFarm.Field.Field;
import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.WareHouses.WareHouseWithFridge.WareHouseWithFridge;
import agro.technology.Worker.Worker;
import agro.technology.utils.terminal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

@Service
public final class FarmSyncService {
    private Gson gson;
    private static final Path farmsPath = Paths.get("src/main/java/agro/technology/Farms/Farms.json"); 
    private final terminal terminal;
    private static List<Farm> farms;

    public FarmSyncService(terminal terminal) {
        this.gson = new GsonBuilder() // ? ця штука дозволяє десеріалізувати з розтипізацією на plantFarm & AnimalFarm
                                      // з Farm
                .registerTypeAdapter(Farm.class, new AdvancedFarmDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        this.terminal = terminal;
        loadFarms();
        // фігня щоб перед виходом (коректним) все збереглося
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			syncFarms();
		}));
    }

    //? старий десеріалізатор (не враховує WareHouseWithFridge)
    // // ? дозволяє десеріалізувати ферму з правильною типізацією
    // class FarmDeserializer implements JsonDeserializer<Farm> {
    //     /**
    //      * json - містить дані
    //      * typeOfT - фігня
    //      * context - ще одна дивна фігня
    //      */
    //     @Override
    //     public Farm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    //             throws JsonParseException {
    //         JsonObject jsonObject = json.getAsJsonObject(); // ? просто перетворює у пари ключ значення
    //         String type = jsonObject.get("type").getAsString(); // ? логічно

    //         if (type.equals(AnimalFarm.class.getSimpleName())) {
    //             return gson.fromJson(json, AnimalFarm.class); // ? перетворює в об'єкт потрібного класу
    //         } else if (type.equals(PlantFarm.class.getSimpleName())) {
    //             return gson.fromJson(json, PlantFarm.class);// ? знову
    //         }

    //         terminal.errorExit("Unknow type of farm");
    //         return null;
    //     }

    // }

    // ? дозволяє десеріалізувати ферму з правильною типізацією
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
            String name = jsonObject.get("name").getAsString();
            String location = jsonObject.get("location").getAsString();
            
            
            JsonObject wareHouseObject = jsonObject.getAsJsonObject("wareHouse");
            String wareHouseType = wareHouseObject.get("type").getAsString(); 
            
            WareHouse wareHouse = null;
            if(wareHouseType.equals(WareHouse.class.getSimpleName())){
                wareHouse = context.deserialize(wareHouseObject, WareHouse.class);
            } else if(wareHouseType.equals(WareHouseWithFridge.class.getSimpleName())){
                wareHouse = context.deserialize(wareHouseObject, WareHouseWithFridge.class);
            }
            
            
            JsonArray workersArray = jsonObject.getAsJsonArray("workers");
            ArrayList<Worker> workers = new ArrayList<>();
            for (JsonElement workerElement : workersArray) {
                Worker worker = context.deserialize(workerElement, Worker.class);
                workers.add(worker);
            }
            
            String type = jsonObject.get("type").getAsString(); 
            if (type.equals(AnimalFarm.class.getSimpleName())) {
                Barn barn = context.deserialize(jsonObject.get("barn"), Barn.class);


                return new AnimalFarm(name, location, wareHouse, workers, barn);
            } else if (type.equals(PlantFarm.class.getSimpleName())) {
                Field field = context.deserialize(jsonObject.get("field"), Field.class);


                return new PlantFarm(type,name, location, wareHouse, workers, field);
            }

            terminal.errorExit("Unknow type of farm");
            return null;
        }

    }

    private void loadFarms() {

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
            for (Farm farm : farms) {
                farm.initProcess();
            }

        } catch (IOException e) {
            terminal.errorExit("DataManager Farms error " + e);
            System.exit(0);
        }
    }

    public void syncFarms() {
        try (FileWriter file = new FileWriter(farmsPath.toFile())) {
            gson.toJson(farms, file);
        } catch (IOException e) {
            terminal.errorExit("sync farms error");
        }
    }

    public List<Farm> getFarms(){
        return farms;
    }

}
