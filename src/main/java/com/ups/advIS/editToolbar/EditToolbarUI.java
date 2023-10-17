package com.ups.advIS.editToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class EditToolbarUI extends JToolBar {

    private Color color = Color.BLACK; //default black as a color

    EditToolbar controller;

    //Group of buttons to manage the two "radio buttons"
    private ButtonGroup buttonsGroup = new ButtonGroup();

    public EditToolbarUI(EditToolbar controller) {

        this.controller = controller;

        this.setBackground(Color.decode("#DEDAD9"));
        this.setFloatable(false);

        JToggleButton selectModeButton = new JToggleButton("select"); //Add icon
        JToggleButton drawModeButton = new JToggleButton("draw"); //Add icon
        JButton colorButton = new JButton();
        colorButton.setBackground(color);

        buttonsGroup.add(selectModeButton);
        buttonsGroup.add(drawModeButton);

        buttonsGroup.setSelected(drawModeButton.getModel(), true);

        selectModeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controller.setSelectMode("select");
                }
            }
        });

        drawModeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controller.setSelectMode("draw");
                }
            }
        });

        colorButton.addActionListener(e -> {
            // Show a color chooser dialog to select a color
            Color selectedColor = JColorChooser.showDialog(null, "Select Color", color);
            if (selectedColor != null) {
                colorButton.setBackground(selectedColor);
                controller.setDrawingColor(selectedColor);

                //Has to change the color of a selected shape in case it is in selected mode
                controller.setCurrentAnnotationColor(selectedColor);
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
