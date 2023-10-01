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

    //A list of shapes, what the shape represents
    private List<Shape> shapes = new ArrayList<>();
    private List<TextBlock> texts = new ArrayList<>();
    private Shape currentShape = null;
    private TextBlock currentTextBox = null;
    private List<ChangeListener> listeners = new ArrayList<>();
    //Annotations points
    //Text annotations

    public PhotoComponentModel() { //For now, it should be custom with the image from the controller
        /*
        try {
            this.image = ImageIO.read(new File("./images/candy_shop.jpg"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(String pathname) {
        try {
            this.image = ImageIO.read(new File(pathname));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

        //Notify Event Listener????
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public void changeImage(Image image) {
        //images.add(image); //In case of a List
        image = image;
        //Need to notify them when a shape has been added
        //notifyChangeListeners();
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);

        //Sembra entri solo all'inizio, è corretto?
        System.out.println("Entrato in listener");
    }

    private void notifyChangeListeners() {
        Iterator var1 = this.listeners.iterator();

        while (var1.hasNext()) {
            ChangeListener listener = (ChangeListener) var1.next();
            listener.stateChanged(new ChangeEvent(this));
        }
    }


    //????
    public void revalidate() {

    }

}
