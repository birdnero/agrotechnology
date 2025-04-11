package agro.technology.Product.Item;

import org.springframework.stereotype.Service;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class ItemCLI {

    private final ItemsService itemsService;
    private final CLI terminal;

    public ItemCLI(CLI cli, ItemsService itemsService) {
        this.terminal = cli;
        this.itemsService = itemsService;
    }

    public boolean resolvePrice(Item item1) {
        Item item2 = itemsService.searchItem(item1.getName());
        if (item2 == null)
            return false;
        if (item1.price.equals(item2.price))
            return true;
        int selected = terminal.initOptions(new String[] {
                "first one (" + item1.getPrice() + "$)",
                "second (" + item2.getPrice() + "$)",
                "new price"
        }, null, () -> terminal.print(terminal.optionsLabel("price issue for " + item1.getName())));

        switch (selected) {
            case -1 -> {
                return false;
            }
            case 0 -> {
                item2.price = item1.price;
            }
            case 1 -> {
                item1.price = item2.price;
            }
            case 2 -> {
                int price = terminal.inputNumber(() -> terminal.print(terminal.optionsLabel("enter new price")));
                while (price < 0)
                    price = terminal.inputNumber(() -> terminal.print(terminal.optionsLabel("enter new price")));
                item1.price = price;
                item2.price = price;
            }
            default -> {
            }
        }
        itemsService.sync();
        return true;
    }

    public void changePrice(Item item) {
        int price = terminal
                .inputNumber(() -> terminal.print(terminal.optionsLabel("set sell price for " + item.getName())));
        while (price < 0)
            price = terminal
                    .inputNumber(() -> terminal.print(terminal.optionsLabel("set sell price for " + item.getName())));
        item.price = price;
        itemsService.sync();
        terminal.previewing("seccessfully changed", Colors.GREEN);
    }

}
