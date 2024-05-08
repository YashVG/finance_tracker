package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;



import org.json.*;

// Represents a reader that reads the finance tracker from JSON data stored in file
public class JsonReader {
    private String source;

    //EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads data from json source, throws exception if error encountered
    public Fin read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        EventLog.getInstance().logEvent(new Event("Loaded data from JSON file"));
        return parseFin(jsonObject); // Now returns a fully parsed Fin object
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Fin from JSON object and returns it
    private Fin parseFin(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Fin fin = new Fin(name);
        addListOfExpenses(fin, jsonObject); // Parses and adds expenses
        addListOfSavings(fin, jsonObject); // You will implement this method similarly to addListOfExpenses
        return fin;
    }

    // MODIFIES: fin
    // EFFECTS: parses through list of expenses and adds them to fin
    private void addListOfExpenses(Fin fin, JSONObject jsonObject) {
        JSONObject initialRef = jsonObject.getJSONObject("listOfExpenses");
        JSONArray jsonArrayOfExpenses = initialRef.getJSONArray("listOfExpenses");
        ListOfExpenses listOfExpenses = new ListOfExpenses();
        EventLog.getInstance().logEvent(new Event("ADDING LIST OF EXPENSES FROM LOADED FILE"));
        for (Object jsonExpense : jsonArrayOfExpenses) {
            JSONObject expenseJson = (JSONObject) jsonExpense;
            Expense expense = getExpense(expenseJson);
            listOfExpenses.addExpense(expense);
        }
        EventLog.getInstance().logEvent(new Event("FINISHED ADDING LIST OF EXPENSES FROM LOADED FILE"));
        fin.newListOfExpenses(listOfExpenses);
    }

    //EFFECTS: gets expense and a json object and reconstructs into
    //         an Expense object
    private static Expense getExpense(JSONObject expenseJson) {
        String name = expenseJson.getString("name");
        int amount = expenseJson.getInt("amount");
        boolean isRecurring = expenseJson.getBoolean("isRecurring");
        // Construct a new Expense instance
        Expense expense = new Expense(name, amount, isRecurring);
        return expense;
    }

    // MODIFIES : fin
    // EFFECTS : parses through list of savings and adds them to fin
    private void addListOfSavings(Fin fin, JSONObject jsonObject) {
        JSONObject initialRef = jsonObject.getJSONObject("listOfSavings");
        JSONArray jsonArrayOfSavings = initialRef.getJSONArray("listOfSavings");
        ListOfSavings listOfSavings = new ListOfSavings();
        EventLog.getInstance().logEvent(new Event("ADDING LIST OF SAVINGS FROM LOADED FILE"));
        for (Object jsonSaving : jsonArrayOfSavings) {
            JSONObject savingJson = (JSONObject) jsonSaving;
            Saving saving = getSaving(savingJson);
            listOfSavings.addSaving(saving);
        }
        EventLog.getInstance().logEvent(new Event("FINISHED ADDING LIST OF SAVINGS FROM LOADED FILE"));
        fin.newListOfSavings(listOfSavings);
    }

    // MODIFIES: fin
    // EFFECTS: parses through list of savings and adds them to fin
    private static Saving getSaving(JSONObject savingJson) {
        String name = savingJson.getString("name");
        int amount = savingJson.getInt("amount");
        double compoundRate = savingJson.getDouble("compoundRate");
        boolean isCompounding = savingJson.getBoolean("isCompounding");
        Saving saving = new Saving(name, amount, compoundRate, isCompounding);
        return saving;
    }

}
