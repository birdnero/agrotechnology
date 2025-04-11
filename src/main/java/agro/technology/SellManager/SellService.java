package agro.technology.SellManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import agro.technology.Product.Product;
import agro.technology.Product.ProductService;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/shop")
public class SellService {

    private final PauseService pauseService;

    private final ProductService productService;

    private final RequestHandler requestHandler;

    private final CLI terminal;
    private Gson gson;
    private List<Product> productAnswer = new ArrayList<>();

    public SellService(CLI cli, RequestHandler requestHandler, ProductService productService,
            PauseService pauseService) {
        this.terminal = cli;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        this.requestHandler = requestHandler;
        this.productService = productService;
        this.pauseService = pauseService;
    }

    public static class RequestProduct {
        public String type;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ask")
    public String askAboutProduction(@RequestBody RequestProduct request) throws InterruptedException {

        requestHandler.hotMessageHook("new purchase request");

        Supplier<Boolean> requestFn = () -> {
            while (true) {

                int selected = terminal.initOptions(new String[] {
                        "respond",
                        "review prices ",
                        "deny"
                }, null, () -> terminal.print(terminal.optionsLabel("someone asks about products")));

                if (selected == -1)
                    return false;
                if (selected == 0) {
                    productAnswer = productService.getAllProducts();
                    return true;
                }

            }
        };

        requestHandler.requestHook(requestFn);

        pauseService.waiting();

        terminal.print(terminal.colorize("request handled", Colors.BACKGROUND_GREEN, false));

        return gson.toJson(this.productAnswer);
    }

    public List<String> getConsumers() {

        return null;
    };

    public record dt() {
    }

}
