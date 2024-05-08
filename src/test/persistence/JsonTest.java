package persistence;

import model.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkFin(String name, ListOfExpenses listOfExpenses, ListOfSavings listOfSavings, Fin fin) {
        assertEquals(name, fin.getName());
        assertEquals(listOfExpenses, fin.getListOfExpenses());
        assertEquals(listOfSavings, fin.getListOfSavings());
    }
}
