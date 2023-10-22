package com.ups.advIS.widgets.photoComponent;

import com.ups.advIS.editToolbar.EditToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PhotoComponent extends JComponent {

    //Model and View inizialization
    private PhotoComponentModel model;
    private PhotoComponentUI ui;

    private Annotation selectedAnnotation;

    //Reference to the toolbar in order to communicate easier
    private EditToolbar editToolbar;

    //Flipped state of the component
    private boolean isFlipped = false;

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

    public void selectAnnotation(Annotation annotation) {
        selectedAnnotation = annotation;
    }

    public Annotation getSelectedAnnotation() {
        return selectedAnnotation;
    }

    public void showEditToolbar(boolean isVisible) {
        editToolbar.getUi().setVisible(isVisible);
    }


    //Functions to manage the Model. Here the Controller is working in the middle for the communication between View and Model
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
    public void setCurrentTextBox(TextBox textBox) {
        textBox.setColor(model.getDrawingAnnotationsColor());
        model.setCurrentTextBox(textBox);
    }
    public TextBox getCurrentTextBox() {
        return model.getCurrentTextBox();
    }
    public List<TextBox> getTexts() {
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
    public void addTexts(TextBox textBox) {
        model.addTexts(textBox);
    }
    public void finalizeShape(Shape currentShape) {
        model.addShape(currentShape);
        model.setCurrentShape(null);
    }
    public Point getInitialDragPoint() {
        return model.getInitialDragPoint();
    }

    public void setInitialDragPoint(Point initialDragPoint) {
        model.setInitialDragPoint(initialDragPoint);
    }

    public List<Point> getInitialShapePoints() {
        return model.getInitialShapePoints();
    }

    public void setInitialShapePoints(List<Point> points) {
        model.setInitialShapePoints(points);
    }


    /**
     * Paint function that start the paint of everything inside the component
     */
    @Override
    protected void paintComponent(Graphics pen) {
        ui.paintPhoto(pen, isFlipped);
    }

}
