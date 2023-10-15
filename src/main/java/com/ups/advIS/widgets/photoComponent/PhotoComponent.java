package com.ups.advIS.widgets.photoComponent;

import com.ups.advIS.album.Album;
import com.ups.advIS.editToolbar.EditToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

//The widget Controller, the main class

/**
 * How will you model this widget? What goes into the abstract concept of this annotated photo widget?
 * What sort of state will it have? Where should all that go?
 * You will probably need to keep at least a reference to the image on disk, a boolean indicating whether it is in its “flipped”
 * state or not, plus a representation of any annotations on the photo (described later).
 *
 * How should the photo widget be drawn? How should it react to mouse and keyboard events? Where should these go?
 */
public class PhotoComponent extends JComponent {

    //Model and View inizialization
    private PhotoComponentModel model;
    private PhotoComponentUI ui;

    //Reference to the toolbar in order to communicate easier
    private EditToolbar editToolbar;

    //Flipped state of the component
    private boolean isFlipped = false;

    public PhotoComponent() {

        //To upload a new PhotoComponent with a default image

        /*
        this.setPreferredSize(new Dimension(preferredSize, preferredSize));

        setMouseListeners();
        //With a default photo?
        model.setImage("C:/Users/forna/Desktop/EIT/UPS/Uni stuff/Advanced programming/AlbumProject/src/main/resources/images/candy_shop.jpg");

         */
    }

    public PhotoComponent(BufferedImage image, EditToolbar editToolbar) {

        this.editToolbar = editToolbar;

        model = new PhotoComponentModel(image);
        ui = new PhotoComponentUI(this);

        //To revalidate and repaint every time the listeners get notified
        model.addChangeListener(e -> {
            repaint();
            revalidate();
        });

    }

    public PhotoComponentModel getModel() {
        return model;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
        model.notifyChangeListeners();
    }

    public void showEditToolbar(boolean isVisible) {
        editToolbar.getUi().setVisible(isVisible);
    }

    //Functions to manage the Model
    public BufferedImage getImage() {
        return model.getImage();
    }
    public void setCurrentShape() {
        model.setCurrentShape(new Shape(model.getDrawingAnnotationsColor()));
    }
    public Shape getCurrentShape() {
        return model.getCurrentShape();
    }
    public List<Shape> getShapes() {
        return model.getShapes();
    }
    public void setCurrentTextBox(TextBlock textBlock) {
        textBlock.setColor(model.getDrawingAnnotationsColor());
        model.setCurrentTextBox(textBlock);
    }
    public TextBlock getCurrentTextBox() {
        return model.getCurrentTextBox();
    }
    public List<TextBlock> getTexts() {
        return model.getTexts();
    }
    public void addPointInCurrentShape(Point p) {
        model.getCurrentShape().addPoint(p);
        model.notifyChangeListeners();
    }
    public void addCharInCurrentTextBox(char character) {
        if(this.isFlipped()) {
            model.getCurrentTextBox().addChar(character);
            model.notifyChangeListeners();
        }
    }
    public void addTexts(TextBlock textBlock) {
        model.addTexts(textBlock);
    }
    public void finalizeShape(Shape currentShape) {
        model.addShape(currentShape);
        model.setCurrentShape(null);
    }

    /**
     * Paint function that start the paint of everything inside the component
     */
    @Override
    protected void paintComponent(Graphics pen) {
        ui.paintPhoto(pen, isFlipped, model.getDrawingAnnotationsColor());
    }

}
