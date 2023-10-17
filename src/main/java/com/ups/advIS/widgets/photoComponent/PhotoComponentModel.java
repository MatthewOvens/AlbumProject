package com.ups.advIS.widgets.photoComponent;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhotoComponentModel {

    private BufferedImage image;


    private List<Shape> shapes = new ArrayList<>();
    private List<TextBlock> texts = new ArrayList<>();
    private Shape currentShape = null;
    private TextBlock currentTextBox = null;
    private List<ChangeListener> changeListeners = new ArrayList<>();

    private String activeMode = "draw";  //Draw by deafault as the initial value

    private Annotation selectedAnnotation;

    private Color drawingAnnotationColor = Color.BLACK;  //Black by default

    public PhotoComponentModel(BufferedImage image) {
        this.image = image;
    }




    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public List<TextBlock> getTexts() {
        return texts;
    }

    public void addTexts(TextBlock textBlock) {
        this.texts.add(textBlock);
    }

    public TextBlock getCurrentTextBox() {
        return currentTextBox;
    }

    public void setCurrentTextBox(TextBlock currentTextBox) {
        this.currentTextBox = currentTextBox;
    }

    public List<Shape> getShapes() {
        return List.copyOf(shapes);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public Annotation getSelectedAnnotation() {
        return selectedAnnotation;
    }

    public void setSelectedAnnotation(Annotation selectedAnnotation) {
        this.selectedAnnotation = selectedAnnotation;
    }

    public Color getDrawingAnnotationsColor() {
        return drawingAnnotationColor;
    }

    public void setDrawingAnnotationColor(Color drawingColor) {
        this.drawingAnnotationColor = drawingColor;
    }

    public String getActiveMode() {
        return activeMode;
    }

    public void setActiveMode(String mode) {
        this.activeMode = mode;
    }

    public void notifyChangeListeners() {
        Iterator var1 = this.changeListeners.iterator();

        while (var1.hasNext()) {
            ChangeListener listener = (ChangeListener) var1.next();
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
