package agro.technology.Farms.AnimaFarm.Barn.Animals;

import com.google.gson.annotations.Expose;

import agro.technology.utils.CLI;
import agro.technology.utils.HasReport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Animal implements HasReport {
    @Expose
    private int price;
    @Expose
    private String[] Products;
    @Expose
    private String[] canEat;
    @Expose
    private String name;
    @Expose
    private double life;
    @Expose
    private int sellPrice;

    private CLI terminal;

    public void setTerminal(CLI terminal) {
        this.terminal = terminal;
    }

    public Animal(int price, String[] products, String[] canEat, String name, double life, int sellPrice, CLI cli) {
        this.price = price;
        Products = products;
        this.canEat = canEat;
        this.name = name;
        this.life = life;
        this.sellPrice = sellPrice;
        this.terminal = cli;
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(name));
        str.append(terminal.formatDataValue("life", life * 100 + " weeks"));
        str.append(terminal.formatDataValue("buy price", price));
        str.append(terminal.formatDataValue("sell price", sellPrice));
        str.append(terminal.formatDataValue("can eat: ", String.join(", ", canEat)));
        str.append(terminal.formatDataValue("production from it: ", String.join(", ", Products)));
        return str.toString();
    }
}
