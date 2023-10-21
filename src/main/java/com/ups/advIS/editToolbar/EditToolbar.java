package com.ups.advIS.editToolbar;

import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import javax.swing.*;
import java.awt.*;

public class EditToolbar extends JComponent {

    EditToolbarUI ui;

    //Reference to the photoComponent in order to communicate easier between the two
    PhotoComponent photoComponent;

    public EditToolbar() {
        this.ui = new EditToolbarUI(this);
    }

    public EditToolbarUI getUi() {
        return ui;
    }

    /**
     * set of the PhotoComponent when it's get created
     */
    public void setPhotoComponent(PhotoComponent photoComponent) {
        this.photoComponent = photoComponent;
    }

    /**
     * Function that set the current color for the PhotoComponent
     */
    public void setDrawingColor(Color color) {
        photoComponent.getModel().setDrawingAnnotationColor(color);
    }

    /**
     * Function that set the current mode "draw"/"select"
     */
    public void setSelectMode(String mode) {
        photoComponent.getModel().setActiveMode(mode);
    }

    /**
     * Function that change the color of a selected Annotation
     */
    public void setCurrentAnnotationColor(Color selectedColor) {
        if(photoComponent.getSelectedAnnotation() != null) {
            photoComponent.getSelectedAnnotation().setColor(selectedColor);
            photoComponent.getModel().notifyChangeListeners();
        }
    }

}
