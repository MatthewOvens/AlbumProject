package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
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

    //Flipped state of the component
    private boolean isFlipped = false;

    public PhotoComponent() {

        //To upload a new PhotoComponent with a default image

        /*
        this.setPreferredSize(new Dimension(preferredSize, preferredSize));

        setMouseListeners();
        //With a default photo?
        model.setImage("C:/Users/forna/Desktop/EIT/UPS/Uni stuff/Advanced programming/AlbumProject/src/main/resources/images/candy_shop.jpg");

        //Why we need it?
        model.addChangeListener(e -> {
            repaint(); //Serve?
            revalidate(); //Serve?
        });
         */
    }

    public PhotoComponent(BufferedImage image) {
        model = new PhotoComponentModel(image);
        ui = new PhotoComponentUI(this);

        //To revalidate and repaint every time a new PhotoComponent is added
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
    }

    //Functions to manage the Model
    public BufferedImage getImage() {
        return model.getImage();
    }
    public void setCurrentShape() {
        model.setCurrentShape(new Shape(Color.BLACK));
    }
    public Shape getCurrentShape() {
        return model.getCurrentShape();
    }
    public List<Shape> getShapes() {
        return model.getShapes();
    }
    public void setCurrentTextBox(TextBlock textBlock) {
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
        this.repaint();
    }
    public void addCharInCurrentTextBox(char character) {
        if(this.isFlipped()) {
            model.getCurrentTextBox().addChar(character);
            this.repaint();
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
        ui.paintPhoto(pen, isFlipped, Color.BLUE, Color.DARK_GRAY);
    }

}
