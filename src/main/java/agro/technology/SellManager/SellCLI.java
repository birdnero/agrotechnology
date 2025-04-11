package agro.technology.SellManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import agro.technology.Product.Item.ItemCLI;
import agro.technology.Product.Item.ItemsService;
import agro.technology.Product.Product;
import agro.technology.Product.ProductService;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class SellCLI {


    private final RequestHandler requestHandler;

    private final ItemsService itemsService;

    private final ItemCLI itemCLI;

    private final ProductService productService;
    private final CLI terminal;

    public SellCLI(CLI cli, ProductService productService, ItemCLI itemCLI, ItemsService itemsService,
            RequestHandler requestHandler) {
        this.terminal = cli;
        this.productService = productService;
        this.itemCLI = itemCLI;
        this.itemsService = itemsService;
        this.requestHandler = requestHandler;
    }

    public void sellActions() {
        while (true) {
            List<String> options = new ArrayList<>();

            options.add("review prices");
            options.add("find customers");
            if (requestHandler.isRequest())
                options.add(terminal.colorize("! request actions !", Colors.BACKGROUND_YELLOW, true));

            int selected = terminal.initOptions(options.toArray(), null,
                    () -> terminal.print(terminal.optionsLabel("Sell bar")));

            if (selected == -1)
                return;
            if (selected == 0)
                priceReview();
            if (selected == 1)
                searhCustomers();
            if (selected == 2) {
                requestHandler.doRequest();
            }

        }

    }

    public void priceReview() {
        List<Product> products = productService.getAllProducts();

        List<String> names = products
                .stream()
                .map(el -> el.getName())
                .collect(Collectors.toList());

        while (true) {
            List<String[]> amountsMatrix = products
                    .stream()
                    .map(el -> new String[] { el.getAmount() + " x " + el.getPrice() + "$" })
                    .collect(Collectors.toList());

            int[] selected = terminal.initOptions(names.toArray(String[]::new), amountsMatrix.toArray(String[][]::new),
                    null,
                    () -> terminal.print(terminal.optionsLabel("change price")));

            if (selected[0] == -1)
                return;

            itemCLI.changePrice(products.get(selected[0]));
            itemsService.updatePrice(itemsService.searchItem(products.get(selected[0]).getName()),
                    old -> products.get(selected[0]).getPrice());

        }
    }

    public void searhCustomers() {

    }

}
