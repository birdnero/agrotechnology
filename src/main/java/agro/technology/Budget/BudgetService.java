package agro.technology.Budget;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class BudgetService {

    private final CLI terminal;
    private int budget = 0;

    BudgetService(CLI terminal) {
        this.terminal = terminal;
        loadBudget();
        // фігня щоб перед виходом (коректним) все збереглося
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            syncBudget();
        }));
        
        terminal.topInfoHook("budget", () -> printBudget());
    }

    /**
     * 
     * @param value - сума яка додастся або відніметься
     * @return - повертає оновлений бюджет або -1 якщо грошей недостатньо
     */
    public int updateBudget(int value) {
        if (budget + value >= 0) {
            budget += value;
            syncBudget();
            return budget;
        }
        return -1;
    }

    public int getBudget() {
        return budget;
    }

    private void loadBudget() {
        try {

            String budget = Files.readString(Path.of("src/main/java/agro/technology/Budget/BUDGET.txt"));
            updateBudget(Integer.parseInt(budget));

        } catch (IOException e) {
            terminal.errorExit("budget reading error");
        }
    }

    public void syncBudget() {
        try (FileWriter file = new FileWriter(Path.of("src/main/java/agro/technology/Budget/BUDGET.txt").toFile())) {
            file.write((getBudget() + ""));
        } catch (IOException e) {
            terminal.errorExit("budget sync error");
        }
    }

    /**
     * виводить бюджет бюджет
     */
    private String printBudget() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.colorize("BUDGET", Colors.BlUE_NEON, true));
        str.append(": \t\t\t");
        str.append(terminal.colorize(budget + "$", Colors.YELLOW, true));
        str.append("\n");
        return str.toString();
    }

}