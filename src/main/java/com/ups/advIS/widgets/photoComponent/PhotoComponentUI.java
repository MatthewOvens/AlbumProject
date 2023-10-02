package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class PhotoComponentUI {

    private int imageX;
    private int imageY;

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
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

        // Draw all stored shapes
        for (Shape shape : shapes) {
            shape.setColor(color);
            shape.draw(g2d);
        }

        // Draw the current shape (if any)
        if (currentShape != null) {
            currentShape.setColor(color);
            currentShape.draw(g2d);
        }

    }

    /**
     * Function to render dinamically what the user is typing
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
