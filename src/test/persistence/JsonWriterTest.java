package persistence;

import model.*;
import org.junit.jupiter.api.Test;



import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Fin wr = new Fin("Test Financial Tracker");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriteEmptyFin() {
        try {
            Fin fin = new Fin("Test Financial Tracker");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFin.json");
            writer.open();
            writer.write(fin);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFin.json");
            fin = reader.read();
            assertEquals(fin.getName(), "Test Financial Tracker");
            assertEquals(fin.getListOfExpenses().getLength(), 0);
            assertEquals(fin.getListOfSavings().getLength(), 0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralFin() {
        try {
            Fin fin = new Fin("Test Fin");
            ListOfSavings los = new ListOfSavings();
            Saving saving1 = new Saving("college", 1200, 0, false);
            Saving saving2 = new Saving("mutual", 1100, 0.09, true);
            los.addSaving(saving1);
            los.addSaving(saving2);

            ListOfExpenses loe = new ListOfExpenses();
            Expense expense1 = new Expense("light", 100, false);
            Expense expense2 = new Expense("credit card", 200, true);
            Expense expense3 = new Expense("food", 150, true);
            loe.addExpense(expense1);
            loe.addExpense(expense2);
            loe.addExpense(expense3);

            fin.newListOfExpenses(loe);
            fin.newListOfSavings(los);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralFin.json");
            writer.open();
            writer.write(fin);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralFin.json");
            fin = reader.read();
            assertEquals(fin.getName(), "Test Fin");
            assertEquals(fin.getListOfExpenses().getLength(), 3);
            assertEquals(fin.getListOfSavings().getLength(), 2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
