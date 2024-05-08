package ui;

import model.Expense;
import model.ListOfExpenses;
import model.ListOfSavings;
import model.Saving;
import model.Fin;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FinApp {

    private static final String JSON_STORE = "./data/fin.json";
    private Fin fin;
    private ListOfExpenses listOfExpenses;
    private ListOfSavings listOfSavings;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    public FinApp() {
        runFinApp();
    }

    private void init() {
        fin = new Fin("Yash's Fin Tracker");
        listOfExpenses = new ListOfExpenses();
        listOfSavings = new ListOfSavings();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    private void runFinApp() {
        boolean keepGoing = true;
        String command = null;
        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ts -> Savings");
        System.out.println("\te -> Expenses");
        System.out.println("\tl -> Load data");
        System.out.println("\tz -> Save data");
        System.out.println("\tq -> Quit");
    }

    // EFFECTS : Processes right function according to input
    private void processCommand(String command) {
        if (command.equals("s")) {
            doSavings();
        } else if (command.equals("e")) {
            doExpenses();
        } else if (command.equals("l")) {
            loadFin();
        } else if (command.equals("z")) {
            saveFin();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS : Processes savings commands in savings menu
    private void doSavings() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            displaySavingsMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("e")) {
                keepGoing = false;
            } else {
                processSavingsCommand(command);
            }
        }

    }

    // EFFECTS : Processes expenses commands in savings menu
    private void doExpenses() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            displayExpensesMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("e")) {
                keepGoing = false;
            } else {
                processExpensesCommand(command);
            }
        }
    }

    // EFFECTS : Redirects function depending on input
    private void processSavingsCommand(String command) {
        if (command.equals("a")) {
            addSaving();
        } else if (command.equals("r")) {
            removeSaving();
        } else if (command.equals("g")) {
            getTotalSavings();
        } else if (command.equals("f")) {
            getTotalFutureSavings();
        } else if (command.equals("s")) {
            displaySavings();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: fin
    // EFFECTS : adds a saving to list of savings and updates fin
    private void addSaving() {
        double compound = 0;
        boolean isCompounded = false;
        System.out.println("Enter saving's name: ");
        String name = input.next();
        System.out.println("Enter amount in saving: ");
        int amount = input.nextInt();
        System.out.println("Is this amount compounded: (y/n)");
        String bool = input.next();
        if (bool.equals("y")) {
            System.out.println("Enter the compound rate as a decimal: ");
            compound = input.nextDouble();
            isCompounded = true;
        }
        Saving savingToAdd = new Saving(name, amount, compound, isCompounded);
        listOfSavings.addSaving(savingToAdd);
        //Extremely important line of code: it adds list of savings to fin tracker
        fin.newListOfSavings(listOfSavings);
        System.out.println("Successfully added " + savingToAdd.getName() + " to your savings!");
    }

    // MODIFIES: fin
    // EFFECTS : removes a saving from list of savings and updates fin
    private void removeSaving() {
        System.out.println("Enter saving's name to remove: ");
        String name = input.next();
        listOfSavings.removeSaving(name);
        fin.newListOfSavings(listOfSavings);
        System.out.println("Successfully removed " + name + " from your savings");
    }

    // EFFECTS : Gets total amount of savings
    private void getTotalSavings() {
        System.out.println("The total amount of your current savings is " + listOfSavings.getTotalAmount());
    }

    // REQUIRES : years >= 0
    // EFFECTS : gets future amount of all savings
    private void getTotalFutureSavings() {
        System.out.println("How many years in the future do you want to calculate: ");
        int years = input.nextInt();
        double earnings = listOfSavings.getFutureTotalAmount(years);
        System.out.println("The amount of savings that you get in " + years + " years is " + earnings);
    }

    private void displaySavings() {
        for (Saving s : listOfSavings) {
            System.out.println(s.displaySaving());
        }
    }

    private void displaySavingsMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add to savings");
        System.out.println("\tr -> Remove from savings");
        System.out.println("\tg -> Get total savings");
        System.out.println("\tf -> Get total savings in future");
        System.out.println("\ts -> Display list of savings");
        System.out.println("\te -> Exit");
    }

    private void processExpensesCommand(String command) {
        if (command.equals("a")) {
            addExpense();
        } else if (command.equals("r")) {
            removeExpense();
        } else if (command.equals("g")) {
            getTotalExpenses();
        } else if (command.equals("f")) {
            getTotalRecurringExpenses();
        } else if (command.equals("s")) {
            displayExpenses();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: fin
    // EFFECTS : adds an expense to list of expenses and updates fin
    private void addExpense() {
        boolean isRecurring = false;
        System.out.println("Enter expense's name: ");
        String name = input.next();
        System.out.println("Enter expense amount: ");
        int amount = input.nextInt();
        System.out.println("Is this a recurring expense (y/n)?");
        String bool = input.next();
        if (bool.equals("y")) {
            isRecurring = true;
        }
        Expense expenseToAdd = new Expense(name, amount, isRecurring);
        listOfExpenses.addExpense(expenseToAdd);
        //
        //
        //Important line of code
        fin.newListOfExpenses(listOfExpenses);
        System.out.println("Successfully added " + name + " to your expenses");
    }

    // MODIFIES: fin
    // EFFECTS : removes an expense from list of expenses and updates fin
    private void removeExpense() {
        System.out.println("Enter expense's name to remove: ");
        String name = input.next();
        listOfExpenses.removeExpense(name);
        //
        //
        //Important line of code
        fin.newListOfExpenses(listOfExpenses);
        System.out.println("Successfully removed " + name + " from your expenses");
    }

    // EFFECTS: Prints total monetary value of all expenses in a list
    private void getTotalExpenses() {
        System.out.println("The total amount of your current expenses is " + listOfExpenses.getTotalAmount());
    }

    // EFFECTS : Gets amount of recurring expenses
    private void getTotalRecurringExpenses() {
        System.out.println("The total amount of your current recurring expenses is "
                + listOfExpenses.getRecurringExpenseAmount());
    }

    // EFFECTS : displays list of expenses
    private void displayExpenses() {
        for (Expense e : listOfExpenses) {
            System.out.println(e.displayExpense());
        }
    }

    private void displayExpensesMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add to expenses");
        System.out.println("\tr -> Remove from expenses");
        System.out.println("\tg -> Get total expenses");
        System.out.println("\tf -> Get total recurring expenses");
        System.out.println("\ts -> Show list of expenses");
        System.out.println("\te -> Exit");
    }

    // EFFECTS: saves fin to file
    private void saveFin() {
        try {
            jsonWriter.open();
            jsonWriter.write(fin);
            jsonWriter.close();
            System.out.println("Saved " + fin.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads fin into file
    private void loadFin() {
        try {
            fin = jsonReader.read();
            listOfExpenses = fin.getListOfExpenses();
            listOfSavings = fin.getListOfSavings();
            System.out.println("Loaded " + fin.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
