package model;

import org.json.JSONObject;
import org.json.JSONArray;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Iterator;

//Represents a list of instances of the Saving class
public class ListOfSavings implements Iterable<Saving>, Writable {

    private ArrayList<Saving> listOfSavings;

    //EFFECTS: Creates an instance of the ListOfSaving class
    public ListOfSavings() {
        listOfSavings = new ArrayList<>();
    }


    //REQUIRES: Saving should not be the same name as an existing member of the list
    //MODIFIES: this
    //EFFECTS: adds a saving to the list of savings
    public void addSaving(Saving saving) {
        listOfSavings.add(saving);
        EventLog.getInstance().logEvent(new Event("Added new saving:" + saving.getName()));

    }

    //REQUIRES: Saving should exist in list
    //MODIFIES: this
    //EFFECTS: removes saving from list
    public void removeSaving(String saving) {
        Saving target = null;
        for (Saving s: listOfSavings) {
            if (saving.equals(s.getName())) {
                target = s;
            }
        }
        listOfSavings.remove(target);
        EventLog.getInstance().logEvent(new Event("Removed " + saving + " from list of savings"));
    }

    public boolean containsSaving(String savingName) {
        for (Saving s : listOfSavings) {
            if (s.getName().equals(savingName)) {
                EventLog.getInstance().logEvent(new Event("Found " + savingName + " in list of savings"));
                return true; // Found a saving with the given name
            }
        }
        EventLog.getInstance().logEvent(new Event("Did not find " + savingName + " in list of savings"));
        return false; // No saving found with the given name
    }

    //EFFECTS: returns the total amount of money from every saving in the list
    public int getTotalAmount() {
        int total = 0;
        for (Saving s: listOfSavings) {
            total += s.getAmount();
        }
        EventLog.getInstance().logEvent(new Event("Got total amount of money in savings"));
        return total;
    }

    //EFFECTS: returns total money from saving after x years
    public int getFutureTotalAmount(int years) {
        int total = 0;
        for (Saving s : listOfSavings) {
            if (s.getIsCompounding()) {
                total += s.getFutureValue(years);
            } else {
                total += s.getAmount();
            }
        }
        EventLog.getInstance().logEvent(new Event("Returned total amount of saving in the future after "
                + years + " years"));
        return total;
    }

    //REQUIRES: Saving should exist within the list of savings
    //EFFECTS: Returns the details of a saving from the list
    public String getSavingDetails(String saving) {
        for (Saving s : listOfSavings) {
            if (saving.equals(s.getName())) {
                EventLog.getInstance().logEvent(new Event("Got details for " + saving));
                return s.displaySaving();
            }
        }
        EventLog.getInstance().logEvent(new Event("Did not find " + saving + " in list of savings"));
        return "Saving not found!";
    }



    public int getLength() {
        return listOfSavings.size();
    }

    @Override
    public Iterator<Saving> iterator() {
        return listOfSavings.iterator();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray savingsArray = new JSONArray();
        for (Saving savings : this.listOfSavings) { // Assuming iterable over Savings objects
            savingsArray.put(savings.toJson()); // Assuming Savings has a toJson method
        }
        json.put("listOfSavings", savingsArray);
        return json;
    }
}
