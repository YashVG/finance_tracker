package model;

import org.json.JSONObject;
import persistence.Writable;

import static java.lang.Math.pow;

//Represents the Saving class where a user can save their money
public class Saving implements Writable {

    private String name;
    private int amount;
    private double compoundRate;
    private boolean isCompounding;

    //EFFECTS: creates an instance of the Saving class
    //         If it isCompounding, set compound rate to whatever is specified,
    //         else, change the compounding rate to 0
    public Saving(String name, int amount, double compoundRate, boolean isCompounding) {
        this.name = name;
        this.amount = amount;
        if (isCompounding) {
            this.isCompounding = true;
            this.compoundRate = compoundRate;
        } else {
            this.isCompounding = false;
            this.compoundRate = 0;
        }
    }

    //REQUIRES: isCompounding == true
    //EFFECTS: Gets the future value of a saving after x years
    public double getFutureValue(int years) {
        return amount * pow((1 + compoundRate), years);
    }

    public String displaySaving() {
        String details = "Saving name: "  + name + "\n "
                + "saving amount: " + amount + "\n ";

        return details;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean getIsCompounding() {
        return this.isCompounding;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("amount", this.amount);
        json.put("compoundRate", this.compoundRate);
        json.put("isCompounding", this.isCompounding);
        return json;
    }
}
