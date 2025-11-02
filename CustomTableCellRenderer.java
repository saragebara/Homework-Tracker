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
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

//Creating class CustomTableCellRenderer which extents DefaultTableCellRenderer
//DefaultTableCellRenderer is a class in the Swing library used to render cells in a JTable
//Its functionalities include manipulating visual aspects, such as font color
public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private Color fontColor; //Color variable to store the custom font color

    //Constructor that takes a color fontColor as a parameter
    public CustomTableCellRenderer(Color fontColor) {
        this.fontColor = fontColor; //Sets font color based on parameter passed of user's choice
    }

    //Override the getTableCellRendererComponent method from DefaultTableCellRenderer so that this code is run
    @Override
    //Renders the contents of a cell in a table
    //Parameters:
    //table: the JTable asking the renderer to render
    //value: the value of the cell being rendered
    //isSelected: whether or not the cell is selected
    //hasFocus: whether or not the cell has focus
    //row: index of row being rendered
    //column: index of column being rendered
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Calls the superclass to use its implementation of method getTableCellRendererComponent, passing this method's parameters to that method
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //Set the font color of the cell to the fontColor passed as a parameter
        setForeground(fontColor);
        
        //Return the configured renderer component
        return this;
    }
}
