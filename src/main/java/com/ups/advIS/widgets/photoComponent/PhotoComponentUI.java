package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import javax.swing.plaf.ToolBarUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

//View + Controller
public class PhotoComponentUI {

    private int imageX;
    private int imageY;

    private BufferedImage image;

    PhotoComponent controller;

    public PhotoComponentUI(PhotoComponent controller) {

        this.controller = controller;

        this.image = this.controller.getImage();

        //TODO make it functioning !!!
        this.controller.setPreferredSize(new Dimension(this.image.getWidth(), this.image.getHeight()));

        //Event Listeners (INPUT)
        setMouseListeners();
        setKeyboardListeners();
    }

    /**
     * MouseListeners implementation
     */
    private void setMouseListeners() {

        controller.addMouseListener(new MouseAdapter() {

            private long lastClickTime = 0;
            private final long doubleClickDelay = 300;

            @Override
            public void mouseClicked(MouseEvent e) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClickTime <= doubleClickDelay) {
                    // Double-click detected
                    flip();
                    showEditToolbar();
                } else {
                    // Single click
                    if(controller.isFlipped() && isBehindPhoto(e.getPoint())) {

                        //Eventual check for previous text boxes
                        //Text shit
                        TextBlock textBlock = new TextBlock(e.getX() - imageX, e.getY() - imageY);
                        controller.setCurrentTextBox(textBlock);
                        controller.addTexts(textBlock);

                        controller.setFocusable(true);
                        controller.requestFocus();

                    }

                }

                lastClickTime = currentTime;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Shape currentShape = controller.getCurrentShape();
                if(controller.isFlipped() && currentShape != null) {
                    // Finalize the current shape when the mouse is released
                    controller.finalizeShape(currentShape);
                }
            }

        });

        controller.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                Shape currentShape = controller.getCurrentShape();

                if(controller.isFlipped()) {
                    if(isBehindPhoto(e.getPoint())) {
                        // Update the current shape's endpoint while dragging to dynamically draw it
                        if(controller.getCurrentShape() == null) {
                            controller.setCurrentShape();
                        }

                        controller.addPointInCurrentShape(new Point(e.getPoint().x - imageX, e.getPoint().y - imageY));
                    }
                    else {
                        if(currentShape != null) {
                            // Finalize the current shape
                            controller.finalizeShape(currentShape);
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

        controller.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                controller.addCharInCurrentTextBox(e.getKeyChar());
            }
        });

    }

    public void flip() {
        controller.setFlipped(!controller.isFlipped());
    }

    /**
     * Function to check if the stroke is inside the photo limits or in the background
     * @param point
     * @return true if the point is inside the borders of the photo, false otherwise
     */
    public boolean isBehindPhoto(Point point) {
        BufferedImage image = controller.getImage();

        return point.getX() >= imageX && point.getX() <= imageX + image.getWidth() &&
                point.getY() >= imageY && point.getY() <= imageY + image.getHeight();

    }

    /**
     * Hide and show the edit toolbar each time the user double click over the image
     */
    public void showEditToolbar() {

        if(controller.isFlipped()) {
            this.controller.showEditToolbar(true);
        }
        else {
            this.controller.showEditToolbar(false);
        }

    }


    public void paintPhoto(Graphics g, Boolean isFlipped, Color annotationsColor) {

        paintBackground(g);

        int parentComponentWidth = this.controller.getWidth();
        int parentComponentHeight = this.controller.getHeight();
        BufferedImage image = this.controller.getImage();

        // Calculate the position to center the image within the component
        imageX = (parentComponentWidth - image.getWidth()) / 2;
        imageY = (parentComponentHeight - image.getHeight()) / 2;

        g.drawImage(image, imageX, imageY, image.getWidth(), image.getHeight(), null);

        if(isFlipped) {
            paintDrawnAnnotations(g);
            paintTextAnnotations(g);
        }

        g.dispose();
    }

    /**
     * Function to draw the background of the component
     */
    public void paintBackground(Graphics g) {

        // Create a Graphics2D object for advanced drawing
        Graphics2D g2d = (Graphics2D) g;

        // Set the background color
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, this.controller.getWidth(), this.controller.getHeight());
        g2d.drawRect(0, 0, this.controller.getWidth(), this.controller.getHeight());

        // Set the drawing color and stroke
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.0f));

        //Rendering of the background drawings
        //Code found online to draw a particular pattern
        for (int x = 0; x < this.controller.getWidth(); x += 40) {
            for (int y = 0; y < this.controller.getHeight(); y += 40) {
                // Draw circles at even positions
                if ((x / 40 + y / 40) % 2 == 0) {
                    g2d.fillOval(x, y, 20, 20);
                }
                // Draw squares at odd positions
                else {
                    g2d.fillRect(x, y, 20, 20);
                }
            }
        }
    }

    /**
     * Function to draw free-hand drawings inside the canva
     */
    public void paintDrawnAnnotations(Graphics g) {

        List<Shape> shapes = this.controller.getShapes();
        Shape currentShape = this.controller.getCurrentShape();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point scalePoint = new Point(imageX, imageY);

        // Draw all stored shapes
        for (Shape shape : shapes) {
            shape.draw(g2d, scalePoint);
        }

        // Draw the current shape (if any)
        if (currentShape != null) {
            currentShape.draw(g2d, scalePoint);
        }

    }

    /**
     * Function to render dynamically what the user is typing
     */
    public void paintTextAnnotations(Graphics g) {

        List<TextBlock> textBlocks = this.controller.getTexts();
        TextBlock currentTextBlock = this.controller.getCurrentTextBox();

        // Draw all stored shapes

        for (TextBlock text : textBlocks) {
            text.draw(g, this.controller, imageX, imageY);
        }

        // Draw the current shape (if any)
        if (currentTextBlock != null) {
            currentTextBlock.draw(g, this.controller, imageX, imageY);
        }

    }

}
