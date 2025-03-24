package agro.technology.Budget;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    private int budget = 0;

    BudgetService() {
        loadBudget();
        // фігня щоб перед виходом (коректним) все збереглося
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            syncBudget();
        }));
    }

    /**
     * 
     * @param value - сума яка додастся або відніметься
     * @return - повертає оновлений бюджет або -1 якщо грошей недостатньо
     */
    public int updateBudget(int value) {
        if (budget + value > 0) {
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
            System.exit(1);
            // terminal.errorExit("budget reading error");
        }
    }

    public void syncBudget() {
        try (FileWriter file = new FileWriter(Path.of("src/main/java/agro/technology/Budget/BUDGET.txt").toFile())) {
            // TODO change ALL pathes in ALL files
            file.write((getBudget() + ""));
        } catch (IOException e) {
            System.exit(1);
            // terminal.errorExit("budget sync error");
        }
    }

}