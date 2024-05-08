package persistence;

import model.Event;
import model.EventLog;
import model.Fin;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of fin to file
    public void write(Fin fin) {
        JSONObject json = fin.toJson();
        saveToFile(json.toString(TAB));
        EventLog.getInstance().logEvent(new Event("Saved data to JSON file"));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
