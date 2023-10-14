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
    private List<ChangeListener> listeners = new ArrayList<>();

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
        listeners.add(listener);
    }

    private void notifyChangeListeners() {
        Iterator var1 = this.listeners.iterator();

        while (var1.hasNext()) {
            ChangeListener listener = (ChangeListener) var1.next();
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
