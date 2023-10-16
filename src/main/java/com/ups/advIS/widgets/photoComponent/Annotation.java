package com.ups.advIS.widgets.photoComponent;

import java.awt.*;

public class Annotation {

    private Color color;
    private boolean isSelected = true; //False by default

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
