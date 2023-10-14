package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
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
                } else {
                    // Single click
                    if(controller.isFlipped() && isBehindPhoto(e.getPoint())) {

                        //Eventual check for previous text boxes
                        //Text shit
                        TextBlock textBlock = new TextBlock(e.getX() - imageX, e.getY() - imageY);
                        controller.getModel().setCurrentTextBox(textBlock);
                        controller.getModel().addTexts(textBlock);

                        controller.setFocusable(true);
                        controller.requestFocus();

                    }

                }

                lastClickTime = currentTime;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(controller.isFlipped() && isBehindPhoto(e.getPoint())) {
                    controller.getModel().setCurrentShape(new Shape(Color.BLACK));
                    controller.getModel().getCurrentShape().addPoint(new Point(e.getPoint().x - imageX, e.getPoint().y - imageY));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Shape currentShape = controller.getModel().getCurrentShape();
                if(controller.isFlipped() && currentShape != null) {
                    // Finalize the current shape when the mouse is released
                    controller.getModel().addShape(currentShape);
                    controller.getModel().setCurrentShape(null);
                    controller.repaint(); //TODO spostare?
                }
            }

        });

        controller.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                Shape currentShape = controller.getModel().getCurrentShape();

                if(controller.isFlipped()) {
                    if(isBehindPhoto(e.getPoint())) {
                        // Update the current shape's endpoint while dragging to dynamically draw it
                        if (controller.getModel().getCurrentShape() != null) {
                            controller.getModel().getCurrentShape().addPoint(new Point(e.getPoint().x - imageX, e.getPoint().y - imageY));
                            controller.repaint(); //TODO spostare in shape?
                        }
                        else {
                            controller.getModel().setCurrentShape(new Shape(Color.BLACK));
                            controller.getModel().getCurrentShape().addPoint(new Point(e.getPoint().x - imageX, e.getPoint().y - imageY));
                            controller.repaint();   //TODO spostare in shape?
                        }
                    }
                    else {
                        if(currentShape != null) {
                            // Finalize the current shape
                            controller.getModel().addShape(currentShape);
                            controller.getModel().setCurrentShape(null);
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
                if(controller.isFlipped()) {
                    controller.getModel().getCurrentTextBox().addChar(e.getKeyChar());
                    controller.repaint(); //TODO spostare?
                }
            }

        });

    }

    public void flip() {
        controller.setFlipped(!controller.isFlipped());
        controller.repaint(); //TODO spostare?
    }

    /**
     * Function to check if the stroke is inside the photo limits or in the background
     * @param point
     * @return true if the point is inside the borders of the photo, false otherwise
     */
    public boolean isBehindPhoto(Point point) {
        BufferedImage image = controller.getModel().getImage();

        return point.getX() >= imageX && point.getX() <= imageX + image.getWidth() &&
                point.getY() >= imageY && point.getY() <= imageY + image.getHeight();

    }

    public void paintPhoto(Graphics g, PhotoComponent canvas, Boolean isFlipped, Color drawingsColor, Color textsColor) {

        paintBackground(g, canvas);

        int parentComponentWidth = canvas.getWidth();
        int parentComponentHeight = canvas.getHeight();
        BufferedImage image = canvas.getModel().getImage();

        // Calculate the position to center the image within the component
        imageX = (parentComponentWidth - image.getWidth()) / 2;
        imageY = (parentComponentHeight - image.getHeight()) / 2;


        //Useful to avoid code duplication
        if(isFlipped) {
            g.setColor(Color.WHITE);
            g.fillRect(imageX, imageY, image.getWidth(), image.getHeight());
            g.drawRect(imageX, imageY, image.getWidth(), image.getHeight());

            paintDrawnAnnotations(g, canvas, drawingsColor);
            paintTextAnnotations(g, canvas, textsColor);

            g.dispose();
        }
        else {
            g.drawImage(image, imageX, imageY, image.getWidth(), image.getHeight(), null);

            g.dispose();
        }
    }

    /**
     * Function to draw the background of the component
     */
    public void paintBackground(Graphics g, PhotoComponent canvas) {

        // Create a Graphics2D object for advanced drawing
        Graphics2D g2d = (Graphics2D) g;

        // Set the background color
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.drawRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Set the drawing color and stroke
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.0f));

        //Rendering of the background drawings
        //Code found online to draw a particular pattern
        for (int x = 0; x < canvas.getWidth(); x += 40) {
            for (int y = 0; y < canvas.getHeight(); y += 40) {
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
    public void paintDrawnAnnotations(Graphics g, PhotoComponent canvas, Color color) {

        List<Shape> shapes = canvas.getModel().getShapes();
        Shape currentShape = canvas.getModel().getCurrentShape();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point scalePoint = new Point(imageX, imageY);

        // Draw all stored shapes
        for (Shape shape : shapes) {
            shape.setColor(color);

            shape.draw(g2d, scalePoint);
        }

        // Draw the current shape (if any)
        if (currentShape != null) {
            currentShape.setColor(color);
            currentShape.draw(g2d, scalePoint);
        }

    }

    /**
     * Function to render dynamically what the user is typing
     */
    public void paintTextAnnotations(Graphics g, PhotoComponent canvas, Color color) {

        List<TextBlock> textBlocks = canvas.getModel().getTexts();
        TextBlock currentTextBlock = canvas.getModel().getCurrentTextBox();

        // Draw all stored shapes

        for (TextBlock text : textBlocks) {
            text.setColor(color);
            text.draw(g, canvas, imageX, imageY);
        }

        // Draw the current shape (if any)
        if (currentTextBlock != null) {
            currentTextBlock.draw(g, canvas, imageX, imageY);
        }

    }

}
