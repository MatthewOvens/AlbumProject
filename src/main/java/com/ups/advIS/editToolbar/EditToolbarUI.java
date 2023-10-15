package com.ups.advIS.editToolbar;

import javax.swing.*;
import java.awt.*;

public class EditToolbarUI extends JToolBar {
    private JButton selectModeButton;
    private JButton drawModeButton;
    private JButton colorButton;

    private Color color = Color.BLACK; //default black as a color

    EditToolbar controller;

    public EditToolbarUI(EditToolbar controller) {

        this.controller = controller;

        this.setBackground(Color.decode("#DEDAD9"));
        this.setFloatable(false);

        //Group of buttons to manage the two "radio buttons"

        JButton selectModeButton = new JButton("Select Mode");
        JButton drawModeButton = new JButton("Draw Mode");
        //JColorChooser colorChooser = new JColorChooser();
        JButton colorButton = new JButton();
        colorButton.setBackground(color);

        selectModeButton.addActionListener(e -> {
            // Add logic to switch to select mode
            // For example, set a flag or call a method to handle select mode
            // You can also change the appearance of the buttons to indicate the current mode.
        });

        drawModeButton.addActionListener(e -> {
            // Add logic to switch to draw mode
            // For example, set a flag or call a method to handle draw mode
            // You can also change the appearance of the buttons to indicate the current mode.
        });

        colorButton.addActionListener(e -> {
            // Show a color chooser dialog to select a color
            Color selectedColor = JColorChooser.showDialog(null, "Select Color", color);
            if (selectedColor != null) {
                // Add logic to change the drawing color to the selected color
                // For example, set the selectedColor as the current drawing color
                System.out.println(selectedColor);
                colorButton.setBackground(selectedColor);
                controller.setDrawingColor(selectedColor);

                //Has to change the color of a selected shape in case it is in selected mode
                //controller.setCurrentShapeColor();
            }
        });

        this.add(Box.createHorizontalGlue());
        this.add(selectModeButton);
        this.addSeparator();
        this.add(drawModeButton);
        this.addSeparator();
        this.add(colorButton);
        this.addSeparator();

        //Initially always hidden
        this.setVisible(false);

    }


}
