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
    private PhotoComponentModel model = new PhotoComponentModel();
    private PhotoComponentUI ui = new PhotoComponentUI();

    /**
     * But be sure to realize that the component may be larger or smaller than the photo it displays
     * (because the user may resize the window, for example).
     * Your component should have a size and a preferredSize that are the size of the photo itself,
     * but you probably don't want any minimumSize or maximumSize. When your PhotoComponent is initialized,
     * and before any photo is loaded, you probably want to use some default value for its size and preferred size.
     */
    private int size = 500; //Default value TODO to put in the model
    private int preferredSize = 500; //Default value TODO to put in the model

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

        this.setPreferredSize(new Dimension(preferredSize, preferredSize));

        setMouseListeners();
        setKeyboardListeners();
        model.setImage(image); //Could have done inizializing the model here calling the eventual constructor PhotoComponentModel(image)
        this.setPreferredSize(new Dimension(model.getImage().getWidth(), model.getImage().getHeight()));

        //To revalidate and repaint every time a new PhotoComponent is added
        model.addChangeListener(e -> {
            repaint();
            revalidate();
        });

    }

    public PhotoComponentModel getModel() {
        return model;
    }

    /**
     * MouseListeners implementation
     */
    private void setMouseListeners() {

        this.addMouseListener(new MouseAdapter() {

            private long lastClickTime = 0;
            private final long doubleClickDelay = 300;

            @Override
            public void mouseClicked(MouseEvent e) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClickTime <= doubleClickDelay) {
                    // Double-click detected
                    flip();
                } else {
                    // Single click
                    if(isFlipped && isBehindPhoto(e.getPoint())) {

                        //Eventual check for previous text boxes
                        //Text shit
                        TextBlock textBlock = new TextBlock(e.getX() - ui.getImageX(), e.getY() - ui.getImageY());
                        model.setCurrentTextBox(textBlock);
                        model.addTexts(textBlock);

                        setFocusable(true);
                        requestFocus();

                    }

                }

                lastClickTime = currentTime;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(isFlipped && isBehindPhoto(e.getPoint())) {
                    model.setCurrentShape(new Shape(Color.BLACK));
                    model.getCurrentShape().addPoint(new Point(e.getPoint().x - ui.getImageX(), e.getPoint().y - ui.getImageY()));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Shape currentShape = model.getCurrentShape();
                if(isFlipped && currentShape != null) {
                    // Finalize the current shape when the mouse is released
                    model.addShape(currentShape);
                    model.setCurrentShape(null);
                    repaint();
                }
            }

        });

        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                Shape currentShape = model.getCurrentShape();

                if(isFlipped) {
                    if(isBehindPhoto(e.getPoint())) {
                        // Update the current shape's endpoint while dragging to dynamically draw it
                        if (model.getCurrentShape() != null) {
                            model.getCurrentShape().addPoint(new Point(e.getPoint().x - ui.getImageX(), e.getPoint().y - ui.getImageY()));
                            repaint();
                        }
                        else {
                            model.setCurrentShape(new Shape(Color.BLACK));
                            model.getCurrentShape().addPoint(new Point(e.getPoint().x - ui.getImageX(), e.getPoint().y - ui.getImageY()));
                            repaint();
                        }
                    }
                    else {
                        if(currentShape != null) {
                            // Finalize the current shape
                            model.addShape(currentShape);
                            model.setCurrentShape(null);
                        }
                    }
                }
            }
        });

    }

    /**
     * KeyboardListeners implementation
     */
    public void setKeyboardListeners() {

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if(isFlipped) {
                    model.getCurrentTextBox().addChar(e.getKeyChar());
                    repaint();
                }
            }

        });

    }

    public void flip() {

        this.isFlipped = !this.isFlipped;
        this.repaint();

    }

    /**
     * Function to check if the stroke is inside the photo limits or in the background
     * @param point
     * @return true if the point is inside the borders of the photo, false otherwise
     */
    public boolean isBehindPhoto(Point point) {
        BufferedImage image = model.getImage();

        return point.getX() >= ui.getImageX() && point.getX() <= ui.getImageX() + image.getWidth() &&
                point.getY() >= ui.getImageY() && point.getY() <= ui.getImageY() + image.getHeight();

    }

    /**
     * Paint function that start the paint of everything inside the component
     */
    @Override
    protected void paintComponent(Graphics pen) {

        ui.paintPhoto(pen, this, isFlipped, Color.BLUE, Color.DARK_GRAY);

    }

}
