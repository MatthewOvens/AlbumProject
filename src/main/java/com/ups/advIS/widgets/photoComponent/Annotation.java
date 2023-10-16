package com.ups.advIS.widgets.photoComponent;

import java.awt.*;

public class Annotation {

    final static Color SELECTING_COLOR = Color.RED;
    private Color color;
    private boolean isSelected = false; //False by default

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
