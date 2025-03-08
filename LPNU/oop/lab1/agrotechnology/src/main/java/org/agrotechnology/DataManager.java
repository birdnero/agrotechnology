package org.agrotechnology;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.agrotechnology.Farm.AnimalFarm;
import org.agrotechnology.Farm.Farm;
import org.agrotechnology.Farm.PlantFarm;
import org.agrotechnology.utils.terminal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public final class DataManager {
    private Gson gson;

    public DataManager() {
        this.gson = new GsonBuilder() //? ця штука дозволяє десеріалізувати з розтипізацією на plantFarm & AnimalFarm з Farm
                .registerTypeAdapter(Farm.class, new FarmDeserializer())
                .create();
    }

    // ? дозволяє десеріалізувати ферму з правильною типізацією
    class FarmDeserializer implements JsonDeserializer<Farm> {
        /**
         * json - містить дані
         * typeOfT - фігня
         * context - ще одна дивна фігня
         */
        @Override
        public Farm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject(); // ? просто перетворює у пари ключ значення
            String type = jsonObject.get("type").getAsString(); // ? логічно

            if (type.equals("animals")) {
                return gson.fromJson(json, AnimalFarm.class); // ? перетворює в об'єкт потрібного класу
            } else if (type.equals("plant")) {
                return gson.fromJson(json, PlantFarm.class);// ? знову
            }

            terminal.errorExit("Unknow type of farm");
            return null;
        }

    }

    public List<Farm> loadFarms() {
        Path path = Paths.get("./data/Farms.json"); // ? шлях через Path, бо це шлях незалежний від файлової системи

        try {
            if (Files.notExists(path)) { // ? якщо файлу не існує
                Files.createDirectories(path.getParent());// ? створити потрібну папку
                Files.createFile(path);// ? створити потрібний файл
                Files.writeString(path, "[]");// ? і засунути туди файл
            }
        } catch (IOException e) {
            terminal.errorExit("Smth wrong with loadFarms file creation");
        }

        // ? конструкція яка автоматично закриє файл і тд. короче корисно
        try (FileReader file = new FileReader(path.toFile())) {

            Type typeForFarms = new TypeToken<List<Farm>>() {
            }.getType();// ? дивна штука яка правильно (за версією chatGPT) типізує json файл

            ArrayList<Farm> farms = this.gson.fromJson(file, typeForFarms);
            return farms;

        } catch (IOException e) {
            terminal.errorExit("DataManager Farms error");
        }
        return null;
    }

    public void syncFarms(List<Farm> farms) {
        Path path = Paths.get("./data/Farms.json");
        try (FileWriter file = new FileWriter(path.toFile())) {
            gson.toJson(farms, file);
        } catch (IOException e) {
            terminal.errorExit("sync farms error");
        }
    }

}
