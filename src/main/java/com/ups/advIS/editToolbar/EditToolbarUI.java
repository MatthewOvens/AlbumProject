package com.ups.advIS.editToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class EditToolbarUI extends JToolBar {

    EditToolbar controller;

    private Color color = Color.BLACK; //Black as the default color

    //Group of buttons to manage the two "radio buttons"
    private ButtonGroup buttonsGroup = new ButtonGroup();
    private JToggleButton selectModeButton;
    private JToggleButton drawModeButton;
    private JButton colorButton;

    public EditToolbarUI(EditToolbar controller) {

        this.controller = controller;

        this.setBackground(Color.decode("#DEDAD9"));
        this.setFloatable(false);

        selectModeButton = new JToggleButton("select");
        drawModeButton = new JToggleButton("draw");
        colorButton = new JButton();
        colorButton.setBackground(color);

        buttonsGroup.add(selectModeButton);
        buttonsGroup.add(drawModeButton);

        buttonsGroup.setSelected(drawModeButton.getModel(), true);

        setListeners();


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

    private void setListeners() {

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

    }

}
