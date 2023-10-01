package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
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
    private PhotoComponentModel model = new PhotoComponentModel();
    private PhotoComponentUI ui = new PhotoComponentUI();

    /**
     * But be sure to realize that the component may be larger or smaller than the photo it displays
     * (because the user may resize the window, for example).
     * Your component should have a size and a preferredSize that are the size of the photo itself,
     * but you probably don't want any minimumSize or maximumSize. When your PhotoComponent is initialized,
     * and before any photo is loaded, you probably want to use some default value for its size and preferred size.
     */
    private int size = 200; //Default value
    private int preferredSize = 200; //Default value

    //Flipped state of the component
    private boolean isFlipped = false;

    public PhotoComponent() {
        //super();

        this.setPreferredSize(new Dimension(preferredSize, preferredSize));


        //setImage()
        setMouseListeners();
        //With a default photo?
        model.setImage("C:/Users/forna/Desktop/EIT/UPS/Uni stuff/Advanced programming/AlbumProject/src/main/resources/images/candy_shop.jpg");

        //Why we need it?
        model.addChangeListener(e -> {
            repaint(); //Serve?
            revalidate(); //Serve?
        });

        //model.addChangeListener(e -> repaint());  //Or other thing? For later to modify dinamically every object inizialized
    }

    //For later
    public PhotoComponent(String imagePathName) {

        this.setPreferredSize(new Dimension(preferredSize, preferredSize));

        //setImage()
        setMouseListeners();
        setKeyboardListeners();
        //With a default photo?
        model.setImage(imagePathName);

        //Why we need it?
        model.addChangeListener(e -> {
            repaint(); //Serve?
            revalidate(); //Serve?
        });

    }

    public PhotoComponentModel getModel() {
        return model;
    }

    /*
    //For later
    public void add(Image image) {
        model.add(image);
    }
    */

    private void setMouseListeners() {

        int pr = 32;

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
                        TextBlock textBlock = new TextBlock(e.getX(), e.getY());
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

                System.out.println("mousePressed");

                if(isFlipped && isBehindPhoto(e.getPoint())) {
                    model.setCurrentShape(new Shape(Color.BLACK));
                    model.getCurrentShape().addPoint(e.getPoint());
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
                            model.getCurrentShape().addPoint(e.getPoint());
                            repaint();
                        }
                        else {
                            model.setCurrentShape(new Shape(Color.BLACK));
                            model.getCurrentShape().addPoint(e.getPoint());
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

    public void setKeyboardListeners() {

        int prova = 2;

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed");

                if(isFlipped) {
                    model.getCurrentTextBox().addChar(e.getKeyChar());
                    repaint();
                }
            }

        });

    }

    /*
    If the users the window so that the shrink component cannot display the entire photograph,
    it should be scrollable so that the user can pan around the image.
    The easiest way to do this is to simply insert your PhotoComponent into a JScrollPane,
    and then insert that JScrollPane into the container area in your application.
    There are settings on JScrollPane that determine whether the scrollbars are always displayed,
    or just when they are needed (it's fine if the scrollbars are always displayed).
    The way JScrollPane works is that it allows its child to be any size it wants,
    but clips and positions it so that the correct part is shown under the visible region.
    This is why you want to make sure your component has a size and preferred size.
    If you reload a photo and change the size, you will probably want to call revalidate()on
    yourPhotoComponent so that the scroll pane “notices” that its size has been updated.
     */

    //At the event listener to "double click" it calls the method to render the back
    //The background should remain the same, only the image is going to be sobstituted by the plain white area
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

        return point.getX() >= ui.getImageX() && point.getX() <= ui.getImageX() + ui.getImageNewWidth() &&
                point.getY() >= ui.getImageY() && point.getY() <= ui.getImageY() + ui.getImageNewHeight();

    }

    /*
    Hint: Your paintComponent method will have two paths through it, depending on the setting of this boolean.
    In the default path, it will draw the background and then the image.
    In the flipped path, it will draw the background, draw the white surface, and then draw the annotations (see below).
     */
    @Override
    protected void paintComponent(Graphics pen) {

        ui.paintPhoto(pen, this, isFlipped);

    }









    /*

    // Handle typed characters
    public void typeText(String text) {
        // Find the text block at the insertion point or create a new one
        TextBlock currentBlock = findOrCreateTextBlock(insertionPointX, insertionPointY);

        // Append the typed text to the current block
        currentBlock.appendText(text);

        // Check for text wrapping and adjust insertion point
        handleTextWrapping(currentBlock);
    }

    // Iterate over text blocks and render them
    public void paint(Graphics g) {
        for (TextBlock block : textBlocks) {
            block.draw(g);
        }
    }

    // Helper method to find or create a text block at a specific position
    private TextBlock findOrCreateTextBlock(int x, int y) {
        // Implement logic to find or create a text block
        // based on the position (x, y) and insertion point
    }

    // Helper method to handle text wrapping within a block
    private void handleTextWrapping(TextBlock block) {
        // Implement logic to handle text wrapping within the block
    }

    */

}
