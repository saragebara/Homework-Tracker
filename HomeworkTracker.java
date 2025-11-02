/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.homeworktracker;
//Imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sara Gebara
 */

//Creating class "HomeworkTracker" as the main GUI application
//It extends JFrame, which provides the resources needed to create a window
public class HomeworkTracker extends JFrame {
    private DefaultTableModel tableModel; //Creating a table model to manage the data
    private JTable table; //Creating a table to display homework assignments

    //Constructor for the "HomeworkTracker" class
    public HomeworkTracker() {
        //Initializing GUI components
        setTitle("Homework Tracker"); //Setting the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //When the user closes the window, the program stops
        setSize(630, 600); //Setting the size of the window
        setLocationRelativeTo(null); //Centers the window on the screen
        
        //Initializing the table model as a "DefaultTableModel" to store and manage data
        tableModel = new DefaultTableModel(new Object[]{"Due Date", "Name", "Description", "Class", "Time Taken (hours)"}, 0);
        //Initializing the table as a "JTable", a component which displays a grid of cells
        table = new JTable(tableModel);
        
        //Using Swing component "JScrollPane" to make the table scrollable in the case that the list can not fit in the GUI view
        JScrollPane scrollPane = new JScrollPane(table);
        //The scroll pane containing the table is added to the center of the GUI
        add(scrollPane, BorderLayout.CENTER);
        
        //Creating buttons for different actions
        JButton addButton = new JButton("Add Assignment");
        JButton removeButton = new JButton("Remove Assignment");
        JButton editButton = new JButton("Edit Assignment");
        JButton searchButton = new JButton("Search by Name");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load from File");
        JButton fileAddButton = new JButton("Add from File");
        JButton settingsButton = new JButton("Settings");
        
        //Making a dropdown menu for different sorting options for the list
        //Includes sorting by due date, sorting by class, and sorting by estimated time taken
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Due Date", "Class", "Time Taken"});
        JButton sortButton = new JButton("✓");
        //When the checkmark button is clicked, the list is sorted based on the selected sorting option
        sortButton.addActionListener(e -> sortSelectedOption((String) sortComboBox.getSelectedItem()));
        //Creating a panel which includes the label "Sort by:" along with sorting options and the checkmark button
        //A flow layout is used to ensure that when the window is resized, the components wrap around accordingly
        JPanel sortPanel = new JPanel(new FlowLayout());
        sortPanel.add(new JLabel("Sort by: "));
        sortPanel.add(sortComboBox);
        sortPanel.add(sortButton);

        //Adding action listeners to all buttons to call their corresponding methods
        addButton.addActionListener(e -> showAddAssignmentDialog());
        removeButton.addActionListener(e -> removeSelectedAssignment());
        editButton.addActionListener(e -> showEditAssignmentDialog());
        searchButton.addActionListener(e -> showSearchByNameDialog());
        saveButton.addActionListener(e -> saveToFile());
        loadButton.addActionListener(e -> loadFromFile());
        fileAddButton.addActionListener(e -> addFromFile());
        settingsButton.addActionListener(e -> openSettings());
        
        //Adding all the buttons and the sorting panel to a button panel
        //A grid layout is used to ensure readability when the window is resized
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(sortPanel);  
        buttonPanel.add(searchButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(fileAddButton);
        buttonPanel.add(settingsButton);
        //Adding the button panel to the bottom of the GUI
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //Private method to show a dialog box when adding an assignment to the list
    //This method and subsequent private utility methods are private since they are only relevant within this class and do not need to be accessed by outside classes
    private void showAddAssignmentDialog() {
        //Creating text fields for user input for each category of an assignment
        JTextField dueDateField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField classField = new JTextField();
        JTextField timeTakenField = new JTextField();
        
        //Creating a panel with a grid layout to add the text fields and labels for each field
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Due Date (mm/dd/yyyy):"));
        panel.add(dueDateField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Class:"));
        panel.add(classField);
        panel.add(new JLabel("Estimated Time Taken (hours):"));
        panel.add(timeTakenField);
        
        //Displaying the dialog box called "Add Assignment" with "OK" and "CANCEL" options
        //What the user clicks (OK, CANCEL, or exiting the dialog box) is stored as an int "result"
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Assignment", JOptionPane.OK_CANCEL_OPTION);

        //If the user clicked okay, the texts inside each textfield are stored as strings
        if (result == JOptionPane.OK_OPTION) {
            String dueDateString = dueDateField.getText();
            String name = nameField.getText();
            String description = descriptionField.getText();
            String className = classField.getText();
            String timeTakenStr = timeTakenField.getText();

            //If the user entered the due date in the proper month/day/year format, and all fields are filled, the assignment is added to the list
            if ((isValidDate(dueDateString, "MM/dd/yyyy"))&&isValidAssignmentInput(dueDateString, name, description, className, timeTakenStr)) {
                tableModel.addRow(new Object[]{dueDateString, name, description, className, timeTakenStr});
            } 
            //Otherwise, an error message dialog pops up and the assignment is not added to the list
            //The error message specifies if it was an issue in date format or filling in all fields
            else if (!isValidAssignmentInput(dueDateString, name, description, className, timeTakenStr)){
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use mm/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Method to check whether all fields are filled when creating/editing an assignment
    private boolean isValidAssignmentInput(String dueDate, String name, String description, String className, String timeTaken) {
        //If all fields are not empty, return true. Otherwise, return false
        return !dueDate.isEmpty() && !name.isEmpty() && !description.isEmpty() && !className.isEmpty() && !timeTaken.isEmpty();
    }
    
    //Method to check whether or not a date entered is formatted properly. Returns true or false
    //Takes the date as a string and a string of the formatting as parameters
    private boolean isValidDate(String dateStr, String format) {
        //Using class "SimpleDateFormat" that was imported in order to create a set format and parse dates
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //Setting leniency to false. This means that while parsing, the date entered must strictly match the formatting set
        sdf.setLenient(false);

        //Parsing the date string into a "Date" object. This means that it interprets and analyzes the string according to the format specified
        //Parsing can throw a "ParseException" if the formatting doesn't match up. If this exception doesn't occur, then "true" is returned
        //If an exception is thrown, then false is returned
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    //Method to remove a selected assignment
    private void removeSelectedAssignment() {
        //Getting the index of the selected row in the table
        int selectedRow = table.getSelectedRow();
        //If this index is NOT -1 (-1 means nothing is selected), then the row selected is removed
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } 
        //If no row is selected, an error dialog box pops up telling the user to select an assignment first
        else {
            JOptionPane.showMessageDialog(this, "Select an assignment to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Method to show a dialog box when editing an assignment
    private void showEditAssignmentDialog() {
        //Getting the index of the selected row in the table
        int selectedRow = table.getSelectedRow();
        //If a row is selected, create text fields containing the existing values of the row
        //The method "getNonNullValue" is used in order to ensure that any null values are converted into an empty string. 
        if (selectedRow != -1) {
            JTextField dueDateField = new JTextField(getNonNullValue(tableModel.getValueAt(selectedRow, 0)));
            JTextField nameField = new JTextField(getNonNullValue(tableModel.getValueAt(selectedRow, 1)));
            JTextField descriptionField = new JTextField(getNonNullValue(tableModel.getValueAt(selectedRow, 2)));
            JTextField classField = new JTextField(getNonNullValue(tableModel.getValueAt(selectedRow, 3)));
            JTextField timeTakenField = new JTextField(getNonNullValue(tableModel.getValueAt(selectedRow, 4)));

            //Creates a panel with a grid layout like the addAssignment panel, but with existing values within each text field
            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Due Date (mm/dd/yyyy):"));
            panel.add(dueDateField);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Class:"));
            panel.add(classField);
            panel.add(new JLabel("Estimated Time Taken (hours):"));
            panel.add(timeTakenField);
            
            //Show the "OK" and "CANCEL" options in this editing panel
            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Assignment", JOptionPane.OK_CANCEL_OPTION);

            //If the user clicks "OK", the values for each category of the assignment are stored
            if (result == JOptionPane.OK_OPTION) {
                String dueDateString = dueDateField.getText();
                String name = nameField.getText();
                String description = descriptionField.getText();
                String className = classField.getText();
                String timeTakenStr = timeTakenField.getText();

                //If the due date inputted is valid, then the values are applied and the table is updated
                if ((isValidDate(dueDateString, "MM/dd/yyyy"))&&isValidAssignmentInput(dueDateString, name, description, className, timeTakenStr)) {                    tableModel.setValueAt(dueDateString, selectedRow, 0);
                    tableModel.setValueAt(name, selectedRow, 1);
                    tableModel.setValueAt(description, selectedRow, 2);
                    tableModel.setValueAt(className, selectedRow, 3);
                    tableModel.setValueAt(timeTakenStr, selectedRow, 4);
                } 
            //Otherwise, an error message dialog pops up and the assignment is not edited
            //The error message specifies if it was an issue in date format or filling in all fields
            else if (!isValidAssignmentInput(dueDateString, name, description, className, timeTakenStr)){
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use mm/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            }
        } else {
            //If the user does not have an assignment selected and attempts to edit an assignment, an error is shown telling them to first select an assignment
            JOptionPane.showMessageDialog(this, "Select an assignment to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Method to convert null values to empty strings
    //Takes an "Object" as a parameter (values from the table model), returns a String
    private String getNonNullValue(Object value) {
        if (value != null) {
            //If the value is not null, it is converted to a string and returned
            return value.toString();
        } else {
            //If the value is null, it returns an empty string
            return "";
        }
    }
   
    //Method to sort all assignments in the table based on the option chosen
    //Takes a string representing the selected option as a parameter
    private void sortSelectedOption(String selectedOption) {
        switch (selectedOption) {
            case "Due Date":
                sortByDueDate();
                break;
            case "Class":
                sortByClass();
                break;
            case "Time Taken":
                sortByTimeTaken();
                break;
        }
    }
    
    //Method to sort by due date in ascending order
    private void sortByDueDate() {
        //Gets the list of assignments from the table using the getAssignmentList() method
        //<Object[]> means that it's a generic array. The angled brackets denote the use of generics
        //Using generics allows any object to be assigned to "Object", as different types of data are being stored in the same array in this assignment list
        ArrayList<Object[]> assignments = getAssignmentList();
        int n = assignments.size(); //getting length of the assignments array

        //Bubble sort algorithm to sort by ascending due dates
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                //Convert dates from adjacent rows to strings in order to compare them
                String date1 = assignments.get(j)[0].toString();
                String date2 = assignments.get(j + 1)[0].toString();

                //If the first date is greater than the second, the order of the assignments are switched
                if (compareDueDates(date1, date2) > 0) {
                    Object[] temp = assignments.get(j);
                    assignments.set(j, assignments.get(j + 1));
                    assignments.set(j + 1, temp);
                }
            }
        }

        //Updates table with sorted assignments
        updateTable(assignments);
    }

    //Method to compare due dates from two assignments
    private int compareDueDates(String date1, String date2) {
        //Using class "SimpleDateFormat" that was imported in order to create a set format and to parse dates
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            //Parsing the due dates into "Date" objects using SimpleDateFormat
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            //Using the Date library's method "compareTo" in order to compare the dates. 
            //A value greater than 1 indicates that d1 is greater than d2, -1 = less than, 0 = equal
            return d1.compareTo(d2);
        } catch (ParseException e) {
            //If there's an error in parsing, prints the error in the console
            //Although the user has to enter the date in the correct format no matter what, including this ensures extensibility in case code is ever changed in the future
            //Returns 0 so that the program continues
            e.printStackTrace();
            return 0;
        }
    }

    //Method to sort by ascending class names (alphabetically)
    private void sortByClass() {
        ArrayList<Object[]> assignments = getAssignmentList(); //Get assignments list
        int n = assignments.size(); //Get the length of the list

        //Bubble sorting alphabetically
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                //If the String of the class of the assignment on top is greater than the one below it, the assignments are swapped
                if (compareClass(assignments.get(j), assignments.get(j + 1)) > 0) {
                    Object[] temp = assignments.get(j);
                    assignments.set(j, assignments.get(j + 1));
                    assignments.set(j + 1, temp);
                }
            }
        }
        //Updates table with sorted assignments
        updateTable(assignments);
    }

    //Method to compare class strings from two assignments
    private int compareClass(Object[] assignment1, Object[] assignment2) {
        //Class strings are in the fourth column, hence the index of 3 used
        String class1 = assignment1[3].toString(); 
        String class2 = assignment2[3].toString();

        //Compare class names and return the result (-1= assignment1 is less than assignment2, 1 = greater, 0 = equal)
        return class1.compareTo(class2);
    }

    //Method to sort by time taken in ascending order
    private void sortByTimeTaken() {
        ArrayList<Object[]> assignments = getAssignmentList(); //Get list of assignments
        int n = assignments.size(); //Get the length of the list

        //Bubble sort in ascending order
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                //If the time taken by the first assignment is greater than the one below it, they are swapped
                if (compareTimeTaken(assignments.get(j), assignments.get(j + 1)) > 0) {
                    Object[] temp = assignments.get(j);
                    assignments.set(j, assignments.get(j + 1));
                    assignments.set(j + 1, temp);
                }
            }
        }
        //Updates table with sorted assignments
        updateTable(assignments);
    }
    
    //Method to compare the time taken by two assignments
    private int compareTimeTaken(Object[] assignment1, Object[] assignment2) {
        //Parsing the time taken from strings into doubles in order to be compared
        //Time taken values are in the fifth column, hence why the assignment index is 4 
        double timeTaken1 = Double.parseDouble(assignment1[4].toString());
        double timeTaken2 = Double.parseDouble(assignment2[4].toString());

        //Comparing the time taken values from both assignments
        if (timeTaken1 < timeTaken2) {
            return -1; //assignment1 takes less time than assignment2
        } else if (timeTaken1 > timeTaken2) {
            return 1;  //assignment1 takes more time than assignment2
        } else {
            return 0;  //time taken values are equal
        }
    }

    //Method to show a dialog box prompting the user to search for an assignment and then outputting search results
    private void showSearchByNameDialog() {
        //Uses an input dialog from the JOptionPane class to prompt the user to enter the assignment name to search for
        String nameToSearch = JOptionPane.showInputDialog(this, "Enter the name to search:");
        //If the user inputs something, this code runs
        if (nameToSearch != null) {
            ArrayList<Object[]> searchResults = new ArrayList<>(); //Initializing an array list of generic objects to store search results
            ArrayList<Object[]> assignments = getAssignmentList();//Getting the list of assignments
            
            //Iterates through the assignments array
            for (int i = 0; i < assignments.size(); i++) {
                Object[] assignment = assignments.get(i); //Sets the object array "assignment" to be the assignment at index i
                
                //If name of the assignment at index i matches the name searched, this assignment is added to the searchResults array
                if (assignment[1].toString().contains(nameToSearch)) { //[1] represents the second column in which names are stored
                    searchResults.add(assignment);
                }
            }
            
            //If there are search results, show the search results found
            if (!searchResults.isEmpty()) {
                showSearchResultsDialog(searchResults);
            } 
            //Otherwise, show a message dialog which tells the user that no matching assignments were found
            else {
                JOptionPane.showMessageDialog(this, "No matching assignments found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //Method to create a dialog box to display search results
    //Takes the generic object array of search results as a parameter
    private void showSearchResultsDialog(ArrayList<Object[]> searchResults) {
        //Creates a panel with a grid layout. The amount of rows is dependent on the size of the searchResults array, there is 1 column, and gap lengths are set
        JPanel panel = new JPanel(new GridLayout(searchResults.size(), 1, 10, 5));
        
        //Iterates through the searchResults array
        for (int i = 0; i < searchResults.size(); i++) {
            Object[] assignment = searchResults.get(i); //Gets the assignment at index i\
            //Iterates through the assignment array
            for (int j = 0; j < assignment.length; j++) {
                Object field = assignment[j];//Gets the assignment at index j
                panel.add(new JLabel(field.toString())); //Adds a label for the assignment in the panel
            }
        }

        //Creates a scroll pane with the panel inside of it so that the user can scroll through search results if there are many
        JScrollPane scrollPane = new JScrollPane(panel);
        //Sets the size of the scroll pane
        scrollPane.setPreferredSize(new Dimension(630, 300)); 

        //Shows a dialog with the scroll pane containing the search results
        JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
    }

    //Method for saving the assignment list to a file
    private void saveToFile() {
        //Creates a file chooser dialog which allows the user to select which file to save the list to
        JFileChooser fileChooser = new JFileChooser();
        //Shows the file chooser dialog and stores what the user does in int result
        int result = fileChooser.showSaveDialog(this);
        //If the user chose a file, this code runs
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile(); //Gets the chosen file
            //Creates a "PrintWriter" object which is used to write characters to a file
            //Creates a FileWriter taking the chosen file as a parameter. This class is used for writing character files
            //If the file selected exists, its contents are emptied. If the file does not exist, it is created
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                //Gets assignment list
                ArrayList<Object[]> assignmentList = getAssignmentList();
                
                //Iterates over assignment list
                for (int i = 0; i < assignmentList.size(); i++) {
                    Object[] assignment = assignmentList.get(i); //Gets assignment at index i 
                    
                    //Iterates through each column of an assignment
                    for (int col = 0; col < assignment.length; col++) {
                        writer.print(assignment[col]); //Writes the contents of the assignment in this column to the file
                        if (col < assignment.length - 1) { //If it's not the last column, add a comma and a space afterwards
                            writer.print(", ");
                        }
                    }
                    writer.println(); //Moves to the next line for the next assignment
                }
                
                //When using "PrintWriter", data is buffered (stored) in memory for efficiency
                //The flush() method forces the data in the buffer to be written to the file immediately
                writer.flush();
                //Show a message dialog stating that the list was successfully saved to file
                JOptionPane.showMessageDialog(this, "List saved to file.");
            } 
            //Handles IOException, such as a file not being found
            catch (IOException e) {
                //Prints a stack trace to the console for debugging purposes
                e.printStackTrace();
                //Shows a message dialog telling the user that there was an error in saving to file
                JOptionPane.showMessageDialog(this, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   //Method for loading an assignment list from a file
    private void loadFromFile() {
        //Creates a file chooser dialog which allows the user to select which file to load
        JFileChooser fileChooser = new JFileChooser();
        //Show the open file dialog and store what the user does in int result
        int result = fileChooser.showOpenDialog(this);
        //If the user selected a file, this code runs
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile(); //Get the chosen file
            //Create a buffered reader object to read the contents of a file
            //Takes a new file reader object as parameter, which takes the chosen file as a parameter
            //The FileReader object is a class used for reading the contents of a file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                ArrayList<Object[]> loadedAssignments = new ArrayList<>(); //Creates a list to store the loaded assignments

                //Reading the lines in the file. While the line has content, the loop continues
                String line;
                while ((line = reader.readLine()) != null) {
                    //Splitting the line into a string array. Splitting where ", " is used as the delimeter
                    String[] data = line.split(", ");
                    //Adding the array to the loaded assignment list
                    loadedAssignments.add(data);
                }
                
                //Updating the table with the loaded assignments. This replaces anything previously in the table.
                updateTable(loadedAssignments);
                //Show a message dialog that states that the assignments were loaded successfully
                JOptionPane.showMessageDialog(this, "Assignments loaded successfully.");
            } 
            //If an IOException occurs, print a stack trace and inform the user of an error in loading assignments
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading assignments from file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //
    private void addFromFile() {
        //Creates a file chooser dialog which allows the user to select which file to load
        JFileChooser fileChooser = new JFileChooser();
        //Show the open file dialog and store what the user does in int result
        int result = fileChooser.showOpenDialog(this);
        //If the user selected a file, this code runs
        if (result == JFileChooser.APPROVE_OPTION) {
            //Gets the chosen file
            File file = fileChooser.getSelectedFile();
            //Creates a buffered reader and file reader to read the contents of the chosen file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                //Reads each line of the chosen file
                String line;
                while ((line = reader.readLine()) != null) {
                    //Splitting data into a string array using delimeter ","
                    String[] data = line.split(", ");
                    //Adding a new row to the table model with the data from the file
                    tableModel.addRow(data);
                }
                //Show a message dialog telling the user that assignments were successfully uploaded
                JOptionPane.showMessageDialog(this, "Assignments uploaded successfully.");
            } 
            //If an IOException occurs, prints a stack trace and informs the user of an error in uploading assignmennts
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error uploading assignments from file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Method to open a settings window
    public void openSettings() {
        //Passing the current JFrame as parameter so that they can interact with each other
        SettingsPopup settingsPopup = new SettingsPopup(this);
        settingsPopup.setVisible(true);
    }

    //Method to update the assignments table whenever changes are made
    //Takes an array of generic objects representing assignments as a parameter
    private void updateTable(ArrayList<Object[]> assignments) {
        //Resets the rows of the table model
        tableModel.setRowCount(0);
        //Iterates over the provided list of assignments
        for (int i = 0; i < assignments.size(); i++) {
            //Gets the assignment at index i
            Object[] assignment = assignments.get(i);
            //Adds the assignment to the list in a new row
            tableModel.addRow(assignment);
        }
    }

    //Method to get the current assignments from the table model
    //Returns a list of arrays of generic objects representing assignments
    private ArrayList<Object[]> getAssignmentList() {
        ArrayList<Object[]> assignments = new ArrayList<>();//Creates an array list to store assignments
        
        //Iterates through each row in the table model
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            //Creates an array of objects representing an assignment of length 5 (representing the five columns)
            Object[] assignment = new Object[5];
            //Iterates through each column in the table model
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                //Gets the value at the current row and column in the table model
                assignment[col] = tableModel.getValueAt(row, col);
            }
            //Adds the assignment array to the list of assignments
            assignments.add(assignment);
        }
        //Returns the list of assignments
        return assignments;
    }

    //Method to update the visual appearance of assignments in the table
    //Takes colors (background color and font color) as parameters
    public void updateAssignmentsListColor(Color bgColor, Color fontColor) {
        //Iterates through each row of the table model
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            //Iterates through each column of the table model
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                //The table.getColumnModel() method returns the TableColumnModel associated with this table which contains all column information for the table
                //The .getColumn() method retrieves the TableColumn at a specific index which is taken as a parameter, allowing for the properties of the chosen column to be customized
                //The setCellRenderer() method sets a custom cell renderer for this specified column. A cell renderer is used to paint (change the color) of cells in a table
                //The setCellRenderer() takes a new CustomTableCellRenderer(fontColor) as a parameter, which is a custom renderer extending the DefaultTableCellRenderer and taking fontColor as a parameter
                //This allows for the font color of text in all cells to be changed accordingly
                table.getColumnModel().getColumn(col).setCellRenderer(new CustomTableCellRenderer(fontColor));
            }
        }

        //Sets the background color of the entire table
        table.setBackground(bgColor);
        //fireTableDataChanged method notifies the table model that data changed which updates the table visually 
        //based on the data in the table model
        tableModel.fireTableDataChanged();

        //Repaints the GUI. Though not necessary since the table has already been notified and updated, 
        //it is good practice to call repaint() anyway to ensure that the GUI is updated
        repaint();
    }

    //Main method
    public static void main(String[] args) {
        //The invokeLater method comes from the SwingUtilities class and ensures that GUI-related activities are performed on the "event dispatch thread (EDT)"
        //The EDT is a special thread used in Swing applicaitons. It handles events an updates the GUI components to avoid synchronization issues
        //Using a lambda expression, creates an instance of the HomeworkTracker class and makes it visible
        SwingUtilities.invokeLater(() -> new HomeworkTracker().setVisible(true));
    }
}


