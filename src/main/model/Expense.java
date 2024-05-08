package model;

import org.json.JSONObject;
import persistence.Writable;
//import java.time.LocalDateTime;

//Creates an instance of an expense
public class Expense implements Writable {

    private int amount;
    //private LocalDateTime date;
    private String name;
    private boolean isRecurring;

    //EFFECTS: Creates an expense instance
    public Expense(String name, int amount, boolean isRecurring) {
        this.name = name;
        this.amount = amount;
        //this.date = LocalDateTime.now();
        this.isRecurring = isRecurring;
    }


    //EFFECTS: Displays the expense's details
    public String displayExpense() {
        String details = "Expense name: "  + name + "\n "
                + "Expense amount: " + amount + "\n ";
                //+ "Expense date: " + date.toString();

        return details;
    }

    //REQUIRES: new amount has to be greater than 0
    //MODIFIES: this
    //EFFECTS: changes amount of expense if it's a recurring expense
//    public void updateAmount(int amount) {
//        if (isRecurring) {
//            this.amount = amount;
//        }
//    }

    //MODIFIES: this
    //EFFECTS: Updates the expense's name
//    public void updateName(String name) {
//        if (name.length() > 5) {
//            this.name = name;
//        }
//    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean getIsRecurring() {
        return this.isRecurring;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("amount", this.amount);
        json.put("isRecurring", this.isRecurring);
        // If you decide to include the date field in the future, you would serialize it here as well.
        return json;
    }
}
