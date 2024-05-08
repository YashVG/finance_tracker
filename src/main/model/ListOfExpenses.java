package model;

import org.json.JSONObject;
import org.json.JSONArray;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Iterator;

// Represents a list of Expense instances
public class ListOfExpenses implements Iterable<Expense>, Writable {

    private ArrayList<Expense> listOfExpenses;

    //EFFECTS: Creates an instance of an ArrayList
    public ListOfExpenses() {
        listOfExpenses = new ArrayList<>();
    }

    //REQUIRES: Expense should not be the same name as an existing member of the list
    //MODIFIES: this
    //EFFECTS: adds an expense to the list of expenses
    public void addExpense(Expense expense) {
        listOfExpenses.add(expense);
        EventLog.getInstance().logEvent(new Event("Added new expense:" + expense.getName()));
    }

    public boolean containsExpense(String expenseName) {
        for (Expense e : listOfExpenses) {
            if (e.getName().equals(expenseName)) {
                EventLog.getInstance().logEvent(new Event("Found " + expenseName + " in list of expenses"));
                return true; // Found an expense with the given name

            }
        }
        EventLog.getInstance().logEvent(new Event("Did not find " + expenseName + " in list of expenses"));
        return false; // No expense found with the given name


    }


    //REQUIRES: Expense should exist in the list
    //MODIFIES: this
    //EFFECTS: removes the expense
    public void removeExpense(String expense) {
        Expense target = null;
        for (Expense e: listOfExpenses) {
            if (expense.equals(e.getName())) {
                target = e;
            }
        }
        listOfExpenses.remove(target);
        EventLog.getInstance().logEvent(new Event("Removed " + expense + " from list of expenses"));
    }

    //REQUIRES: Expense should exist within the list of expenses
    //EFFECTS: Returns the details of an expense from the list
    public String getExpenseDetails(String expense) {
        for (Expense e : listOfExpenses) {
            if (expense.equals(e.getName())) {
                EventLog.getInstance().logEvent(new Event("Got details for " + expense));
                return e.displayExpense();
            }
        }
        EventLog.getInstance().logEvent(new Event("Did not find " + expense + " in list of expenses"));
        return "Expense not found!";
    }

    //EFFECTS: returns the total amount of money from every expense in the list
    public int getTotalAmount() {
        int total = 0;
        for (Expense e: listOfExpenses) {
            total += e.getAmount();
        }
        EventLog.getInstance().logEvent(new Event("Got total amount of money in expenses"));
        return total;
    }

    //EFFECTS: gets total amount of recurring expenses/bills
    public int getRecurringExpenseAmount() {
        int total = 0;
        for (Expense e: listOfExpenses) {
            if (e.getIsRecurring()) {
                total += e.getAmount();
            }
        }
        EventLog.getInstance().logEvent(new Event("Got total amount of money in recurring expenses"));
        return total;
    }

    //EFFECTS: returns length of expenses list
    public int getLength() {
        return listOfExpenses.size();
    }

    @Override
    public Iterator<Expense> iterator() {
        return listOfExpenses.iterator();
    }


    public JSONObject toJson() {
        JSONArray jsonArray = new JSONArray();
        for (Expense expense : listOfExpenses) {
            jsonArray.put(expense.toJson());
        }
        JSONObject json = new JSONObject();
        json.put("listOfExpenses", jsonArray);
        return json;
    }

}
