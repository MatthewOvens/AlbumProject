package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import javax.swing.plaf.ToolBarUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
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

                    if(controller.getModel().getActiveMode() == "draw") {
                        System.out.println("draw mode");
                        manageDrawing(e);
                    }
                    else if(controller.getModel().getActiveMode() == "select") {
                        System.out.println("select mode");
                        manageSelecting(e);
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

    /**
     * Function that manage the behaviour of the PhotoComponent while in the drawing mode
     */
    private void manageDrawing(MouseEvent e) {
        if(controller.isFlipped() && isBehindPhoto(e.getPoint())) {

            //Eventual check for previous text boxes
            TextBlock textBlock = new TextBlock(e.getX() - imageX, e.getY() - imageY);
            controller.setCurrentTextBox(textBlock);
            controller.addTexts(textBlock);

            controller.setFocusable(true);
            controller.requestFocus();

        }
    }

    /**
     * Function that manage the behaviour of the PhotoComponent while in the selecting mode
     */
    private void manageSelecting(MouseEvent e) {
        if(controller.isFlipped()) {

            List<TextBlock> textBlocks = this.controller.getTexts();
            List<Shape> shapes = this.controller.getShapes();

            Point clickPoint = new Point(e.getPoint().x, e.getPoint().y);

            boolean gotTextSelection = false;

            //Checking if clicked in a TextBox and eventually select it
            for (TextBlock text : textBlocks) {

                text.setIsSelected(false); //Delete of the eventual previous selected textBoxes

                if(isBehindPhoto(e.getPoint())) {
                    int x = text.getX();
                    int y = text.getY();
                    int width = text.getWidth();
                    int height = text.getHeight();

                    boolean isInsideHorizontally = (clickPoint.x > imageX + x) && (clickPoint.x < imageX + x + width);
                    boolean isInsideVertically = (clickPoint.y > imageY + y - 13) && (clickPoint.y < imageY + y + height);

                    if(isInsideHorizontally && isInsideVertically) {
                        //TODO gestire le textBox che si sovrappongono?

                        if(!gotTextSelection) {
                            controller.selectAnnotation(text);
                            gotTextSelection = true;
                        }

                    }
                }

            }

            //If no TextBoxes have been selected check of the Shapes
            //Checking if clicked near a Shape and eventually select it
            final int SELECTION_RANGE = 5;
            boolean gotShapeSelection = false;

            //For each shape control if the click happened close to the shape and eventually select it
            for (Shape shape : shapes) {

                shape.setIsSelected(false); //Delete of the eventual previous selected textBoxes

                if(!gotShapeSelection && !gotTextSelection) {
                    List<Line2D> lines = shape.lines;
                    for (Line2D line: lines) {

                        Point lineStart = new Point((int)line.getX1(), (int)line.getY1());
                        Point lineEnd = new Point((int)line.getX2(), (int)line.getY2());

                        double distance = distancePointToLineSegment(lineStart, lineEnd, clickPoint);

                        if (distance <= SELECTION_RANGE) {
                            gotShapeSelection = true;
                        }

                    }
                    if(gotShapeSelection) {
                        controller.selectAnnotation(shape);
                    }
                }

            }

            controller.getModel().notifyChangeListeners();

        }
    }

    /**
     * Help from chatGPT: "Distance point-segment in a java function"
     * params: lineStart.x,lineStart.y -> line start point / lineEnd.x,lineEnd.y -> line end point / clickPoint.x,clickPoint.y -> click coordinates
     */
    private double distancePointToLineSegment(Point lineStart, Point lineEnd, Point clickPoint) {
        // Calculate the squared length of the line segment
        double length2 = (lineEnd.x - lineStart.x) * (lineEnd.x - lineStart.x) + (lineEnd.y - lineStart.y) * (lineEnd.y - lineStart.y);

        // Check if the line segment is of zero length
        if (length2 == 0) {
            return Math.sqrt((clickPoint.x - lineStart.x) * (clickPoint.x - lineStart.x) + (clickPoint.y - lineStart.y) * (clickPoint.y - lineStart.y));
        }

        // Calculate the parameter along the line where the closest point is
        double t = ((clickPoint.x - lineStart.x) * (lineEnd.x - lineStart.x) + (clickPoint.y - lineStart.y) * (lineEnd.y - lineStart.y)) / length2;

        // Clamp t to ensure the closest point lies within the line segment
        t = Math.max(0, Math.min(1, t));

        // Calculate the coordinates of the closest point on the line segment
        double xClosest = lineStart.x + t * (lineEnd.x - lineStart.x);
        double yClosest = lineStart.y + t * (lineEnd.y - lineStart.y);

        // Calculate the distance between the closest point and the given point
        double distance = Math.sqrt((clickPoint.x - xClosest) * (clickPoint.x - xClosest) + (clickPoint.y - yClosest) * (clickPoint.y - yClosest));

        return distance;
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

        // Draw all stored textBoxes
        for (TextBlock text : textBlocks) {
            text.draw(g, this.controller, imageX, imageY);
        }

        // Draw the current textbox (if any)
        if (currentTextBlock != null) {
            currentTextBlock.draw(g, this.controller, imageX, imageY);
        }

    }

}
