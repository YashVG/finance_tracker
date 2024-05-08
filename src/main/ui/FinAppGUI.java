package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class FinAppGUI extends JFrame {

    private JPanel mainPanel; // This will now serve as the "main screen"
    private JPanel currentPanel; // The panel currently being displayed
    private JPanel previousPanel = null; // Track the previous panel to enable "Back" functionality
    private static final String JSON_STORE = "./data/fin.json";
    private Fin fin;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private ListOfExpenses listOfExpenses;
    private ListOfSavings listOfSavings;

    // EFFECTS: initializer method
    private void init() {
        fin = new Fin("Yash's Fin Tracker");
        listOfExpenses = new ListOfExpenses();
        listOfSavings = new ListOfSavings();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    public FinAppGUI() {
        super("Financial Tracker");
        init();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setupMainPanel();
        setupTopPanel();

        currentPanel = mainPanel;
        add(currentPanel, BorderLayout.CENTER);

        pack();
    }

    // MODIFIES: mainPanel
    // EFFECTS: adds back and quit button to top of the panel
    private void setupTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToPreviousPanel());
        topPanel.add(backButton, BorderLayout.WEST);

        JButton quitButton = new JButton("Quit");

        quitButton.addActionListener(e -> {
            printLog(EventLog.getInstance());
            System.exit(0);
        });

        topPanel.add(quitButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    // MODIFIES: mainPanel
    // EFFECTS: sets up initial menu screen and its options
    private void setupMainPanel() {
        mainPanel.removeAll();
        addButton(mainPanel, "Expenses", e -> switchToExpensesMenu());
        addButton(mainPanel, "Savings", e -> switchToSavingsMenu());
        addButton(mainPanel, "Save data", e -> saveFinGUI());
        addButton(mainPanel, "Load data", e -> loadFin());
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // MODIFIES: JPanel panel
    // EFFECTS : adds a button to the button with the declared parameters
    private void addButton(JPanel panel, String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        panel.add(button, new GridBagConstraints());
    }

    // EFFECTS : prints log of all the events occurred in the model classes
    public void printLog(EventLog el) {
        StringBuilder builder = new StringBuilder();
        for (Event next : el) {
            builder.append(next.toString() + "\n\n");
        }
        System.out.println(builder);
    }

    // MODIFIES : mainPanel, currentPanel, previousPanel
    // EFFECTS : switches currentPanel to expenses one, and adds
    //           main panel to previousPanel
    private void switchToExpensesMenu() {
        JPanel expensesPanel = setupMenuPanel();
        expensesPanel.add(createButton("Add to expenses", e -> addExpenseGUI()));
        expensesPanel.add(createButton("Remove from expenses", e -> removeExpenseGUI()));
        expensesPanel.add(createButton("Get total expenses", e -> getTotalExpensesGUI()));
        expensesPanel.add(createButton("Get total recurring expenses", e -> getTotalRecurringExpensesGUI()));
        expensesPanel.add(createButton("Show list of expenses", e -> displayExpensesGUI()));
        switchPanel(expensesPanel);
    }

    // MODIFIES : mainPanel, currentPanel, previousPanel
    // EFFECTS : switches currentPanel to savings one, and adds
    //           main panel to previousPanel
    private void switchToSavingsMenu() {
        JPanel savingsPanel = setupMenuPanel();
        savingsPanel.add(createButton("Add to savings", e -> addSavingGUI()));
        savingsPanel.add(createButton("Remove from savings", e -> removeSavingGUI()));
        savingsPanel.add(createButton("Get total savings", e -> getTotalSavingsGUI()));
        savingsPanel.add(createButton("Predict future savings", e -> getTotalFutureSavingsGUI()));
        savingsPanel.add(createButton("Show list of savings", e -> displaySavingsGUI()));
        switchPanel(savingsPanel);
    }

    // MODIFIES : this
    // EFFECTS : sets up menu panel
    private JPanel setupMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20)); // 3 rows, 2 columns, with padding
        return menuPanel;
    }

    // EFFECTS : creates a new button
    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    // MODIFIES : currentPanel, previousPanel
    // EFFECTS : goes back to previous page
    private void goBackToPreviousPanel() {
        if (previousPanel != null) {
            switchPanel(previousPanel);
            previousPanel = null; // Reset to prevent stacking issues
        } else {
            switchToMainPanel(); // Fallback to main panel if no previous panel
        }
    }

    // MODIFIES : currentPanel, previousPanel
    // EFFECTS : switches currentPanel to new panel
    private void switchPanel(JPanel newPanel) {
        getContentPane().remove(currentPanel);
        previousPanel = currentPanel; // Update the previous panel before switching
        currentPanel = newPanel;
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        getContentPane().validate();
        getContentPane().repaint();
    }

    // MODIFIES : currentPanel, previousPanel
    // EFFECTS : goes back to main menu
    private void switchToMainPanel() {
        if (currentPanel != mainPanel) {
            previousPanel = currentPanel; // Save the current panel as previous
        }
        switchPanel(mainPanel);
        setupMainPanel(); // Re-setup main panel to ensure it's in its initial state
    }

    // MODIFIES: this
    // EFFECTS: loads fin into file
    private void loadFin() {
        try {
            fin = jsonReader.read();
            listOfExpenses = fin.getListOfExpenses();
            listOfSavings = fin.getListOfSavings();

            // Prepare visual feedback
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/photos/loading.png")));
            ImageIcon scaledIcon = scaleImageIcon(icon, 0.5);
            JLabel message = new JLabel("Loaded " + fin.getName() + " from " + JSON_STORE);
            message.setHorizontalTextPosition(JLabel.RIGHT);
            message.setVerticalTextPosition(JLabel.CENTER);

            JOptionPane.showMessageDialog(this, message, "Data Loaded",
                    JOptionPane.INFORMATION_MESSAGE, scaledIcon);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: "
                    + JSON_STORE, "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: saves fin to file
    private void saveFinGUI() {
        try {
            jsonWriter.open();
            jsonWriter.write(fin);
            jsonWriter.close();

            // Load the image for successful save notification
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/photos/save.png")));
            ImageIcon scaledIcon = scaleImageIcon(icon, 0.5);
            JOptionPane.showMessageDialog(this, "Saved " + fin.getName() + " to "
                    + JSON_STORE, "Data Saved", JOptionPane.INFORMATION_MESSAGE, scaledIcon);

            System.out.println("Saved " + fin.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: "
                    + JSON_STORE, "Save Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES : icon
    // EFFECTS : applies scaleFactor to icon
    private ImageIcon scaleImageIcon(ImageIcon icon, double scaleFactor) {
        // Calculate the new dimensions based on the scale factor
        int newWidth = (int) (icon.getIconWidth() * scaleFactor);
        int newHeight = (int) (icon.getIconHeight() * scaleFactor);

        // Use getScaledInstance() to scale the image
        Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Return a new ImageIcon for use
        return new ImageIcon(scaledImage);
    }

    // MODIFIES: fin
    // EFFECTS : adds an expense to list of expenses and updates fin
    private void addExpenseGUI() {
        // Create a panel to hold multiple input fields
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JCheckBox isRecurringBox = new JCheckBox("Recurring Expense?");

        panel.add(new JLabel("Expense's Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Expense Amount:"));
        panel.add(amountField);
        panel.add(isRecurringBox);

        // Show the panel in a dialog
        int result = JOptionPane.showConfirmDialog(null, panel, "Add Expense",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            int amount = Integer.parseInt(amountField.getText()); // Add error handling for non-integer inputs
            boolean isRecurring = isRecurringBox.isSelected();

            Expense expenseToAdd = new Expense(name, amount, isRecurring);
            listOfExpenses.addExpense(expenseToAdd);

            // Assuming fin.newListOfExpenses() updates the fin object with the new list of expenses
            fin.newListOfExpenses(listOfExpenses);

            JOptionPane.showMessageDialog(null, "Successfully added " + name + " to your expenses");
        } else {
            System.out.println("Expense addition cancelled.");
        }
    }

    // MODIFIES: fin
    // EFFECTS : removes an expense from list of expenses and updates fin
    private void removeExpenseGUI() {
        String name = JOptionPane.showInputDialog(this, "Enter expense's name to remove:",
                "Remove Expense", JOptionPane.QUESTION_MESSAGE);

        if (name != null && !name.trim().isEmpty()) {
            // Check if the expense exists before attempting to remove
            boolean exists = listOfExpenses.containsExpense(name);
            if (exists) {
                listOfExpenses.removeExpense(name);
                fin.newListOfExpenses(listOfExpenses); // Update fin with the new list of expenses
                JOptionPane.showMessageDialog(this, "Successfully removed " + name
                        + " from your expenses", "Expense Removed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // If no expense with the given name exists, inform the user
                JOptionPane.showMessageDialog(this, "No expense found with the name: "
                        + name, "Expense Not Found", JOptionPane.ERROR_MESSAGE);
            }
        } else if (name != null) {
            JOptionPane.showMessageDialog(this,
                    "No name entered. Please enter an expense's name to remove.", "No Name Entered",
                    JOptionPane.WARNING_MESSAGE);
        }
        // If the operation was cancelled (name == null), do nothing
    }

    // MODIFIES: fin
    // EFFECTS : adds a saving to list of savings and updates fin
    private void addSavingGUI() {
        AddSavingSetup savingSetup = getAddSavingSetup();

        // Initially hide the compound rate field unless compounded is checked
        savingSetup.compoundRateField.setEnabled(false);
        savingSetup.isCompoundedBox.addActionListener(e ->
                savingSetup.compoundRateField.setEnabled(savingSetup.isCompoundedBox.isSelected()));

        // Show the panel in a dialog
        int result = JOptionPane.showConfirmDialog(null, savingSetup.panel, "Add Saving",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        layoutChooser(savingSetup, result);
    }

    // EFFECTS : chooses layout for adding a saving
    private void layoutChooser(AddSavingSetup savingSetup, int result) {
        if (result == JOptionPane.OK_OPTION) {
            meetsCriteria(savingSetup);
        } else {
            JOptionPane.showMessageDialog(null, "Saving addition cancelled.", "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS : checks if compound rate is > 0 & amount is > 0
    //           for adding a new saving
    private void meetsCriteria(AddSavingSetup savingSetup) {

        String name = savingSetup.nameField.getText();
        double compound = 0.0;

        int amount;
        // Safe parsing with error handling
        try {
            amount = Integer.parseInt(savingSetup.amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (savingSetup.isCompoundedBox.isSelected()) {
            try {
                compound = Double.parseDouble(savingSetup.compoundRateField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid compound rate.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Saving savingToAdd = new Saving(name, amount, compound, savingSetup.isCompoundedBox.isSelected());
        listOfSavings.addSaving(savingToAdd);
        fin.newListOfSavings(listOfSavings); // Important: updates the fin tracker
        JOptionPane.showMessageDialog(null, "Successfully added to your savings!");
    }

    //Helper function
    private static AddSavingSetup getAddSavingSetup() {
        // Panel to hold input fields
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JCheckBox isCompoundedBox = new JCheckBox("Is this amount compounded?");
        JTextField compoundRateField = new JTextField();
        // Add components to the panel
        panel.add(new JLabel("Saving's Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Amount in Saving:"));
        panel.add(amountField);
        panel.add(isCompoundedBox);
        panel.add(new JLabel("Compound Rate (as a decimal):"));
        panel.add(compoundRateField);
        AddSavingSetup savingSetup = new AddSavingSetup(panel, nameField, amountField, isCompoundedBox,
                compoundRateField);
        return savingSetup;
    }

    //Helper function
    private static class AddSavingSetup {
        public final JPanel panel;
        public final JTextField nameField;
        public final JTextField amountField;
        public final JCheckBox isCompoundedBox;
        public final JTextField compoundRateField;

        public AddSavingSetup(JPanel panel, JTextField nameField, JTextField amountField,
                              JCheckBox isCompoundedBox, JTextField compoundRateField) {
            this.panel = panel;
            this.nameField = nameField;
            this.amountField = amountField;
            this.isCompoundedBox = isCompoundedBox;
            this.compoundRateField = compoundRateField;
        }
    }

    // REQUIRES : years >= 0
    // EFFECTS : gets future amount of all savings
    private void getTotalFutureSavingsGUI() {
        // Use a JOptionPane to get the number of years from the user
        String yearsString = JOptionPane.showInputDialog(this,
                "How many years in the future do you want to calculate:",
                "Calculate Future Savings", JOptionPane.QUESTION_MESSAGE);

        // Check if a value was entered
        if (yearsString != null && !yearsString.trim().isEmpty()) {
            try {
                int years = Integer.parseInt(yearsString);
                if (years >= 0) {
                    double earnings = listOfSavings.getFutureTotalAmount(years);
                    JOptionPane.showMessageDialog(this,
                            "The amount of savings that you get in " + years + " years is " + earnings,
                            "Future Savings", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // If the user enters a negative number
                    JOptionPane.showMessageDialog(this,
                            "Please enter a positive number of years.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                // If the input was not a valid integer
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number of years.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
        // If the operation was cancelled (yearsString == null), do nothing
    }

    // MODIFIES: fin
    // EFFECTS : removes a saving from list of savings and updates fin
    private void removeSavingGUI() {
        String name = JOptionPane.showInputDialog(this, "Enter saving's name to remove:",
                "Remove Saving", JOptionPane.QUESTION_MESSAGE);

        if (name != null && !name.trim().isEmpty()) {
            // Check if the saving exists before attempting to remove
            boolean exists = listOfSavings.containsSaving(name);
            if (exists) {
                listOfSavings.removeSaving(name);
                fin.newListOfSavings(listOfSavings); // Update fin with the new list of savings
                JOptionPane.showMessageDialog(this,
                        "Successfully removed " + name + " from your savings",
                        "Saving Removed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // If no saving with the given name exists, inform the user
                JOptionPane.showMessageDialog(this, "No saving found with the name: " + name,
                        "Saving Not Found", JOptionPane.ERROR_MESSAGE);
            }
        } else if (name != null) {
            JOptionPane.showMessageDialog(this,
                    "No name entered. Please enter a saving's name to remove.", "No Name Entered",
                    JOptionPane.WARNING_MESSAGE);
        }
        // If the operation was cancelled (name == null), do nothing
    }

    // EFFECTS : Gets total amount of savings
    private void getTotalSavingsGUI() {
        int totalAmount = listOfSavings.getTotalAmount();
        JOptionPane.showMessageDialog(this, "The total amount of your current savings is "
                + totalAmount, "Total Savings", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: Prints total monetary value of all expenses in a list
    private void getTotalExpensesGUI() {
        int totalAmount = listOfExpenses.getTotalAmount();
        JOptionPane.showMessageDialog(this, "The total amount of your current expenses is "
                + totalAmount, "Total Expenses", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS : Gets amount of recurring expenses
    private void getTotalRecurringExpensesGUI() {
        // Assuming getRecurringExpenseAmount() returns the sum of all recurring expenses
        int recurringExpenseAmount = listOfExpenses.getRecurringExpenseAmount();
        JOptionPane.showMessageDialog(this,
                "The total amount of your current recurring expenses is "
                        + recurringExpenseAmount, "Total Recurring Expenses", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS : displays list of expenses
    private void displayExpensesGUI() {
        // Create a model for the list
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Fill the model with expenses
        for (Expense e : listOfExpenses) {
            listModel.addElement(e.displayExpense()); // Assumes displayExpense() returns a String
        }

        // Check if the list is empty before proceeding
        if (listModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No expenses to display.",
                    "List of Expenses", JOptionPane.INFORMATION_MESSAGE);
            return; // Exit the method early
        }

        // Create the JList and add the model to it
        JList<String> expensesList = new JList<>(listModel);

        // Ensure there's enough vertical space
        expensesList.setVisibleRowCount(10); // Display up to 10 items before scrolling

        // Add the list to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(expensesList);

        // Determine the scroll pane size
        scrollPane.setPreferredSize(new Dimension(250, 150));

        // Display in a dialog
        JOptionPane.showMessageDialog(this, scrollPane, "List of Expenses",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS : displays list of savings
    private void displaySavingsGUI() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Fill the model with savings, assuming Saving has a suitable toString() or a displaySaving() method
        for (Saving s : listOfSavings) {
            listModel.addElement(s.displaySaving()); // Modify as needed for your Saving class representation
        }

        // Check if the list is empty before proceeding
        if (listModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No savings to display.",
                    "List of Savings", JOptionPane.INFORMATION_MESSAGE);
            return; // Exit the method early
        }

        JList<String> savingsList = new JList<>(listModel);
        savingsList.setVisibleRowCount(10); // Set visible row count before scrolling

        JScrollPane scrollPane = new JScrollPane(savingsList);
        scrollPane.setPreferredSize(new Dimension(250, 150)); // Set preferred size for the scroll pane

        JOptionPane.showMessageDialog(this, scrollPane, "List of Savings", JOptionPane.INFORMATION_MESSAGE);
    }

}
