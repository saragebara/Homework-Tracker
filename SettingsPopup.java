/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.homeworktracker;

/**
 *
 * @author Sara Gebara
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Creating class SettingsPopup which extends JDialog and represents a dialog box for the application's settings
public class SettingsPopup extends JDialog {
    //Creating a dropdown box for selecting the theme of the application
    private JComboBox<String> themeComboBox;

    //Constructor for SettingsPopup
    //Takes the parent JFrame as a parameter, which triggers the settings popup
    public SettingsPopup(JFrame parent) {
        //Calling the constructor of the super class (JDialog)
        //The "parent" is the JFrame HomeworkTracker, which owns this JDialog
        //"Settings" is the title of this dialog box
        //The modal is set to "true", meaning that HomeworkTracker can not be interacted with while the settings popup is open
        super(parent, "Settings", true);
        //Setting the size of the dialog box
        setSize(300, 200);
        //Setting its location relative to the HomeworkTracker
        setLocationRelativeTo(parent);

        //Creating a new panel with a grid layout with 3 rows, 2 columns, and setting the gaps in pixels
        JPanel settingsPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        //Creating a label and dropdown box including the themes available
        JLabel themeLabel = new JLabel("Select Theme:");
        themeComboBox = new JComboBox<>(new String[]{"Default","Pink", "Green", "Blue"});
        //Adding the label and dropdown box to the settings panel
        settingsPanel.add(themeLabel);
        settingsPanel.add(themeComboBox);

        //Creating a button used to apply changes. If clicked, the applySettings() method is called
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            //Overriding in order to run the code in this method rather than the parent method
            @Override
            public void actionPerformed(ActionEvent e) {
                applySettings();
            }
        });

        //Creating a button used to cancel changes. If clicked, the settings popup is closed
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            //Overriding in order to run the code in this method rather than the parent method
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //Creating a button panel to put the apply and cancel buttons in
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        //Setting the layout as a border layout and adding all components to the content pane
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(settingsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    //Method to apply the selected theme when the apply button is clicked
    private void applySettings() {
    String selectedTheme = themeComboBox.getSelectedItem().toString(); //Get the selected theme
    
    //Get the parent HomeworkTracker instance. This allows SettingsPopup to interact/modify the HomeworkTracker instance
    HomeworkTracker homeworkTracker = (HomeworkTracker) getParent();
    
    /*Based on the selected theme, background colors and font colors are assigned
      After being assigned, the updateAssignmentsListColor() method from homeworkTracker is called, 
      taking these colors as parameters and applying the changes in theme*/
    if (selectedTheme.equals("Default")){
        //RGB values are used for each color
        Color bgColor = new Color(255,255,255); //White
        Color fontColor = new Color(0,0,0); //Black
        homeworkTracker.updateAssignmentsListColor(bgColor, fontColor);
    }
    else if (selectedTheme.equals("Pink")) {
        Color bgColor = new Color(252,204,231); //Light pink
        Color fontColor = new Color(0,0,0); //Black
        homeworkTracker.updateAssignmentsListColor(bgColor,fontColor);
    }
    else if (selectedTheme.equals("Green")){
        Color bgColor = new Color(196, 230, 181); //Light green
        Color fontColor = new Color(120, 107, 96); //Brown
        homeworkTracker.updateAssignmentsListColor(bgColor,fontColor);
    }
    else if (selectedTheme.equals("Blue")){
        Color bgColor = new Color(206, 239, 242); //Light blue
        Color fontColor = new Color(0,0,0); //Black
        homeworkTracker.updateAssignmentsListColor(bgColor,fontColor);
    }
    //Close the settings popup
    dispose();
}
}