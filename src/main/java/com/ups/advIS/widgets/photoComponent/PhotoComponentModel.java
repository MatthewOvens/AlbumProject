package com.ups.advIS.widgets.photoComponent;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
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

    //List of actual images stored, with link, maybe position, size..
    //private List<Image> images = new ArrayList<>();

    private BufferedImage image;
    private List<ChangeListener> listeners = new ArrayList<>();
    //Annotations points
    //Text annotations

    /*
    public List<Image> getImages() {
        return List.copyOf(images);
        //return shapes;  NOT THIS CAUSE THE UI SHOULD NOT BE MODIFIED FROM OUTSIDE THE CLASS
    }
    */

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
