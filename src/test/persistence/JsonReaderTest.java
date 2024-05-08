package persistence;

import model.Fin;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Fin fin = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFin() {
        JsonReader reader = new JsonReader("./data/testEmptyFin.json");
        try {
            Fin fin = reader.read();
            assertEquals("Yash's Fin Tracker", fin.getName());
            assertEquals(0, fin.getListOfSavings().getLength());
            assertEquals(0, fin.getListOfExpenses().getLength());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testGeneralFin() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFin.json");
        try {
            Fin fin = reader.read();
            assertEquals("Yash's Fin Tracker", fin.getName());
            assertEquals(fin.getListOfSavings().getLength(), 2);
            assertEquals(fin.getListOfExpenses().getLength(), 1);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
