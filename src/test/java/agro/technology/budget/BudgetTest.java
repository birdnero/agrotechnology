package agro.technology.budget;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import agro.technology.Budget.BudgetService;

@SpringBootTest(args = "--test")
public class BudgetTest {

    @Autowired
    BudgetService budgetService;

    private int actualBudget;

    @BeforeEach
    void preSetUp() {
        this.actualBudget = budgetService.getBudget();
    }

    @AfterEach
    void postSetUp() {
        int diff = actualBudget - budgetService.getBudget();
        budgetService.updateBudget(diff);
    }

    @Test
    void budget01() {
        assertEquals(budgetService.updateBudget(100), budgetService.getBudget());
    }

    @Test
    void budget02() {
        int budgetBefore = budgetService.getBudget();

        assertEquals(budgetService.updateBudget(100), budgetBefore + 100);
    }

    @Test
    void budget03() {
        int budgetBefore = budgetService.getBudget();

        assertEquals(budgetService.updateBudget(-budgetBefore / 2),
                (budgetBefore % 2 == 1 ? (budgetBefore / 2) + 1 : budgetBefore / 2));
    }

    @Test
    void budget04() {
        int budgetBefore = budgetService.getBudget();

        assertEquals(budgetService.updateBudget(-budgetBefore), 0);
    }

    @Test
    void budget05() {
        int budgetBefore = budgetService.getBudget();
        budgetService.updateBudget(-(budgetBefore + 1));
        assertEquals(budgetService.getBudget(), budgetBefore);
    }

    @Test
    void budget06() {
        int budgetBefore = budgetService.getBudget();

        assertEquals(budgetService.updateBudget(-(budgetBefore + 1)), -1);
    }

}
