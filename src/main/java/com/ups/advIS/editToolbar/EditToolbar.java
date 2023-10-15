package com.ups.advIS.editToolbar;

import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import javax.swing.*;
import java.awt.*;

public class EditToolbar extends JComponent {

    EditToolbarUI ui;

    //Reference to the photoComponent in order to communicate easier
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

    public void setDrawingColor(Color color) {

        System.out.println(color);
        photoComponent.getModel().setDrawingAnnotationColor(color);

    }


}
