package agro.technology.Product;

import agro.technology.Farms.Farm;
import agro.technology.Farms.FarmService;
import agro.technology.Product.Item.ItemsService;
import agro.technology.utils.CLI;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final FarmService farmService;

    private final ItemsService itemsService;
    private final CLI terminal;

    public ProductService(CLI cli, ItemsService itemsService, @Lazy FarmService farmService) {
        this.terminal = cli;
        this.itemsService = itemsService;
        this.farmService = farmService;
    }

    public Product create(String name, int amount) {
        if (name == null || name.equals("")) return null;
        Product product = new Product(name, Math.max(0, amount), 0);
        return product;
    }

    public void add(Product product, int amount) {
        if (amount > 0 && product != null)
            product.amount += amount;
    }

    public boolean substract(Product product, int amount) {
        if (product == null || amount > product.getAmount() || amount < 0)
            return false;
        product.amount -= amount;
        return true;
    }

    public boolean updateAmount(Product product, Function<Integer, Integer> updater) {
        if (product == null) return false;
        Integer value = updater.apply(product.getAmount());
        if (value == null || value < 0)
            return false;
        product.amount = value;
        return true;
    }

    // розбиває продукт на два лишаючи в першому product.amount - amount
    public Product divideProduct(Product product, int amount) {
        if(product == null) return null;
        if (substract(product, amount))
            return create(product.getName(), amount);
        return create(product.getName(), 0);
    }

    public String report(Product product) {
        if (product == null) return null;
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName("product: " + product.getName()));
        str.append(terminal.formatDataValue("amount", product.getAmount()));
        return str.toString();
    }

    public Product load(JsonObject json) {
        String typeOfProduct = json.get("name").getAsString();
        int amount = json.get("amount").getAsInt();

        Product product = create(typeOfProduct, amount);
        return product;
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (Farm farm : farmService.getFarms())
            for (Product product : farm.getWareHouse().getStorage()) {
                boolean acc = true;
                for (Product product2 : products)
                    if (product.getName().equals(product2.getName())) {
                        acc = false;
                        product2.amount += product.amount;
                    }

                if (acc)
                    products.add(create(product.getName(), product.getAmount()));
            }

        for (Product product : products) {
            itemsService.updatePrice(product, p -> itemsService.getPriceForItem(product));
        }

        return products;
    }
}
