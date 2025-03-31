package agro.technology.Farms.AnimaFarm.Barn.Animals;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import agro.technology.utils.CLI;
import agro.technology.utils.ConsoleEditable;
import agro.technology.utils.CLI.Colors;

@Service
public class AnimalConsoleService implements ConsoleEditable {

        private final CLI terminal;

        private final AnimalService animalService;

        public AnimalConsoleService(AnimalService animalService, CLI CLI) {
                this.animalService = animalService;
                this.terminal = CLI;
        }

        public void createAnimal() {
                terminal.cursorOnOff(false);
                terminal.CtrlC(true);
                String name = terminal.input(() -> terminal.print(terminal.optionsLabel("enter animal type")));

                // !change over time!!
                String[] canEat = terminal
                                .input(() -> terminal
                                                .print(terminal.optionsLabel("enter what can eat (Wheat, Meat, ...)")))
                                .split(", ");

                // !change over time!!
                String[] products = terminal
                                .input(() -> terminal
                                                .print(terminal.optionsLabel(
                                                                "enter production from this animal (Pork, Meat, ...)")))
                                .split(", ");

                List<Double> lifeArr = DoubleStream
                                .iterate(0, x -> x + 0.05)
                                .limit((int) (1.0 / 0.05) + 1)
                                .boxed()
                                .toList();
                DecimalFormat format = new DecimalFormat("#.##");

                List<Integer> priceArr = IntStream
                                .iterate(0, x -> x + 1)
                                .limit(51)
                                .boxed()
                                .toList();

                List<Integer> selected = terminal.multiOptions(
                                new String[] { "life (weeks)", "buy price", "sell price" },
                                new String[][] {
                                                lifeArr.stream().map(el -> format.format(el * 100))
                                                                .toArray(String[]::new),
                                                priceArr.stream().map(el -> el.toString()).toArray(String[]::new),
                                                priceArr.stream().map(el -> el.toString()).toArray(String[]::new),
                                },
                                null, null);

                terminal.cursorOnOff(false);
                terminal.CtrlC(true);
                if (selected.get(0) == -1)
                        return;

                animalService.addAnimal(
                                name,
                                priceArr.get(selected.get(1)),
                                lifeArr.get(selected.get(0)),
                                products,
                                canEat,
                                priceArr.get(selected.get(1)));
                terminal.previewing("succefully added!", Colors.GREEN);
        }

        public void editAnimal(Animal animal) {
                while (true) {
                        String oldName = animal.getName();
                        int selected = terminal.initOptions(
                                        new String[] {
                                                        "name",
                                                        "can eat",
                                                        "can product",
                                                        "life",
                                                        "buy price",
                                                        "sell price"
                                        },
                                        null,
                                        () -> terminal.print(terminal.optionsLabel(animal.getName())));

                        List<Integer> priceArr = IntStream
                                        .iterate(0, x -> x + 1)
                                        .limit(51)
                                        .boxed()
                                        .toList();

                        switch (selected) {
                                case -1 -> {
                                        return;
                                }
                                case 0 -> {
                                        String name = terminal.input(() -> terminal
                                                        .print(terminal.optionsLabel("enter animal type")));
                                        animal.setName(name);
                                }
                                case 1 -> {
                                        // !change over time!!
                                        String[] canEat = terminal
                                                        .input(() -> terminal
                                                                        .print(terminal.optionsLabel(
                                                                                        "enter what can eat (Wheat, Meat, ...)")))
                                                        .split(", ");
                                        animal.setCanEat(canEat);
                                }
                                case 2 -> {
                                        // !change over time!!
                                        String[] products = terminal
                                                        .input(() -> terminal
                                                                        .print(terminal.optionsLabel(
                                                                                        "enter production from this animal (Pork, Meat, ...)")))
                                                        .split(", ");

                                        animal.setProducts(products);
                                }
                                case 3 -> {
                                        List<Double> lifeArr = DoubleStream
                                                        .iterate(0, x -> x + 0.05)
                                                        .limit((int) (1.0 / 0.05) + 1)
                                                        .boxed()
                                                        .toList();
                                        DecimalFormat format = new DecimalFormat("#.##");

                                        int[] lifeSelected = terminal.initOptions(
                                                        new String[] { "life (weeks)" },
                                                        lifeArr
                                                                        .stream()
                                                                        .map(el -> format.format(el * 100))
                                                                        .toArray(String[]::new),
                                                        new int[] { 0 },
                                                        null, () -> terminal.print("\n"));
                                        if (lifeSelected[0] == -1)
                                                break;
                                        animal.setLife(lifeArr.get(lifeSelected[1]));
                                }
                                case 4 -> {

                                        int[] priceSelected = terminal.initOptions(
                                                        new String[] { "buy price" },
                                                        priceArr.stream().map(el -> el.toString())
                                                                        .toArray(String[]::new),
                                                        new int[] { 0 },
                                                        null, () -> terminal.print("\n"));
                                        if (priceSelected[0] == -1)
                                                break;
                                        animal.setPrice(priceArr.get(priceSelected[1]));
                                }
                                case 5 -> {
                                        int[] priceSelected = terminal.initOptions(
                                                        new String[] { "sell price" },
                                                        priceArr.stream().map(el -> el.toString())
                                                                        .toArray(String[]::new),
                                                        new int[] { 0 },
                                                        null, () -> terminal.print("\n"));
                                        if (priceSelected[0] == -1)
                                                break;
                                        animal.setSellPrice(priceArr.get(priceSelected[1]));
                                }

                                default -> {
                                }
                        }

                        animalService.setAnimal(animal, oldName);
                }
        }

        @Override
        public String getLabel() {
                return "edit animals";
        }

        @Override
        public void consoleEditAction() {
                while (true) {

                        List<String> options1 = animalService
                                        .getAnimals()
                                        .stream()
                                        .map(animal -> animal.getName())
                                        .collect(Collectors.toList());
                        options1.addFirst("add");

                        int[] usedTo = IntStream.range(1, options1.size()).toArray();

                        String[] opt2 = { "view", "edit", "delete" };

                        int[] selected = terminal.initOptions(
                                        options1.toArray(),
                                        opt2,
                                        usedTo,
                                        null, () -> terminal.print(terminal.optionsLabel(getLabel())));

                        if (selected[0] == -1)
                                return;
                        else if (selected[0] == 0) {
                                createAnimal();
                        } else {

                                Animal animal = animalService.getAnimal(options1.get(selected[0]));

                                switch (selected[1]) {
                                        case 0 -> {
                                                terminal.print(animal.report());
                                                while (!terminal.keyAction(127)) {
                                                }
                                        }
                                        case 1 -> {
                                                editAnimal(animal);
                                        }
                                        case 2 -> {

                                                animalService.deleteAnimal(options1.get(selected[0]));
                                        }
                                        default -> {
                                        }
                                }

                        }
                }

        }
}
