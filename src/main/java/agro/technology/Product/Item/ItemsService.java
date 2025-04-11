package agro.technology.Product.Item;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class ItemsService {
    private final Path pathToFile = Path.of("src/main/java/agro/technology/Product/Item/Items.json");
    private List<Item> itemsIndex;
    private Gson gson;
    private final CLI terminal;

    public ItemsService(CLI cli) {
        this.terminal = cli;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        loadItems();
    }

    public Item searchItem(String name) {
        for (Item it : itemsIndex)
            if (it.getName().equals(name))
                return it;
        return null;
    }

    public int getPriceForItem(Item item0) {
        Item it = searchItem(item0.getName());
        if (it != null)
            return it.price;
        Item item = new Item(item0.getName(), item0.getPrice());
        itemsIndex.add(item);
        sync();
        return item.getPrice();
    }

    public void loadItems() {
        try (FileReader file = new FileReader(pathToFile.toFile())) {
            Type typeToken = new TypeToken<List<Item>>() {
            }.getType();

            List<Item> items = gson.fromJson(file, typeToken);
            this.itemsIndex = items;
        } catch (IOException e) {
            terminal.errorExit(this.getClass().getSimpleName() + ": error reading " + pathToFile.getFileName());
        }
    }

    protected void sync() {
        try (FileWriter file = new FileWriter(pathToFile.toFile())) {
            gson.toJson(itemsIndex, file);
        } catch (Exception e) {
            terminal.previewing(this.getClass().getSimpleName() + ": sync items error", Colors.RED);
        }
    }

    public boolean updatePrice(Item item, Function<Integer, Integer> updater) {
        int value = updater.apply(item.getPrice());
        if (value < 0)
            return false;
        item.price = value;
        sync();
        return true;
    }

    public List<Item> getAvaibleItems(){
        return itemsIndex;
    }
}
