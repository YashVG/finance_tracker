package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Collections;

public class Fin implements Writable {

    private String name;
    private ListOfSavings listOfSavings;
    private ListOfExpenses listOfExpenses;

    //EFFECTS: Constructs Fin with a name and empty list of savings and expenses
    public Fin(String name) {
        this.name = name;
        listOfSavings = new ListOfSavings();
        listOfExpenses = new ListOfExpenses();
    }

    // MODIFIES: this
    // EFFECTS: adds updates list of savings to Fin
    public void newListOfSavings(ListOfSavings los) {
        listOfSavings = los;
    }


    public String getName() {
        return name;
    }

    public ListOfSavings getListOfSavings() {
        return listOfSavings;
    }

    public ListOfExpenses getListOfExpenses() {
        return listOfExpenses;
    }

    // MODIFIES: this
    // EFFECTS: updates list of expenses to new list los
    public void newListOfExpenses(ListOfExpenses loe) {
        listOfExpenses = loe;
    }


    // EFFECTS: creates a new json object and adds both los and loe to it
    //          as json arrays
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("listOfSavings", listOfSavings.toJson());
        json.put("listOfExpenses", listOfExpenses.toJson());
        return json;
    }
}
