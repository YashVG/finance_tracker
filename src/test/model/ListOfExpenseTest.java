package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListOfExpenseTest {
    // delete or rename this class!
    private ListOfExpenses testList;
    private Expense expense1;
    private Expense expense2;
    private Expense expense3;

    @BeforeEach
    void runBefore() {
        testList = new ListOfExpenses();
        expense1 = new Expense("light", 100, false);
        expense2 = new Expense("credit card", 200, true);
        expense3 = new Expense("food", 150, true);
    }

    @Test
    void testAddAndRemoveExpenses() {
        assertEquals(0, testList.getLength());
        testList.addExpense(expense1);
        testList.addExpense(expense2);
        testList.addExpense(expense3);
        assertEquals(3, testList.getLength());
        testList.removeExpense("light");
        assertEquals(2, testList.getLength());
    }

    @Test
    void testGettingExpenses() {
        testList.addExpense(expense1);
        testList.addExpense(expense2);
        testList.addExpense(expense3);
        assertEquals(testList.getExpenseDetails("food"),
                "Expense name: "  + "food" + "\n "
                        + "Expense amount: " + 150 + "\n ");
        assertEquals(testList.getExpenseDetails("electricity"), "Expense not found!");
    }

    @Test
    void testGetTotalAmount() {
        testList.addExpense(expense1);
        testList.addExpense(expense2);
        testList.addExpense(expense3);
        assertEquals(testList.getTotalAmount(), 450);
        assertEquals(testList.getRecurringExpenseAmount(), 350);
    }

    @Test
    void testContains() {
        testList.addExpense(expense1);
        testList.addExpense(expense2);
        assertTrue(testList.containsExpense("light"));
        assertFalse(testList.containsExpense("testts"));
    }

    @Test
    void testIterator() {
        testList.addExpense(expense1);
        testList.addExpense(expense2);
        testList.addExpense(expense3);
        assertNotNull(testList.iterator());
    }

}