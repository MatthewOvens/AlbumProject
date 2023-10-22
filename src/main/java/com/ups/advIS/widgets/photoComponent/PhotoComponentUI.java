package com.ups.advIS.widgets.photoComponent;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;

//View + Controller
public class PhotoComponentUI {

    private final int SELECTION_RANGE = 5;

    private int imageX;
    private int imageY;

    //Flag used to manage the annotation dragging
    private boolean isDraggable = false;

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
                    controller.setFlipped(!controller.isFlipped());
                    showEditToolbar();
                } else {
                    // Single click
                    if(controller.getModel().getActiveMode().equals("draw")) {
                        manageTextBoxDrawing(e);
                    }
                    else if(controller.getModel().getActiveMode().equals("select")) {
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

            @Override
            public void mousePressed(MouseEvent e) {

                Annotation selectedAnnotation = controller.getSelectedAnnotation();

                if(controller.isFlipped() &&
                        isBehindPhoto(e.getPoint()) &&
                        controller.getModel().getActiveMode().equals("select") &&
                        selectedAnnotation != null) {

                    if(selectedAnnotation instanceof Shape) {

                        //Check if it is over the shape
                        if(isOverShape(((Shape)selectedAnnotation), e.getPoint())) {
                            controller.setInitialDragPoint(e.getPoint());
                            controller.setInitialShapePoints(((Shape)selectedAnnotation).getPoints());
                            isDraggable = true;
                        }
                        else {
                            isDraggable = false;
                        }

                    }
                    else if(selectedAnnotation instanceof TextBox) {

                        //Check if it is inside the box
                        if(isInsideTextBox((TextBox)selectedAnnotation, e.getPoint())) {
                            controller.setInitialDragPoint(e.getPoint());
                            isDraggable = true;
                        }
                        else {
                            isDraggable = false;
                        }
                    }
                }
            }

        });

        controller.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if(controller.isFlipped()) {

                    if(controller.getModel().getActiveMode().equals("draw")) {
                        manageShapesDrawing(e);
                    }
                    else if(controller.getModel().getActiveMode().equals("select")) {
                        manageDraggingAnnotation(e);
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
     * Function that manage the behaviour of the PhotoComponent on TextBoxes while in the drawing mode
     */
    private void manageTextBoxDrawing(MouseEvent e) {
        if(controller.isFlipped() && isBehindPhoto(e.getPoint())) {

            TextBox textBox = new TextBox(e.getX() - imageX, e.getY() - imageY);
            controller.setCurrentTextBox(textBox);
            controller.addTexts(textBox);

            controller.setFocusable(true);
            controller.requestFocus();

        }
    }

    /**
     * Function that manage the behaviour of the PhotoComponent on Shapes while in the drawing mode
     */
    private void manageShapesDrawing(MouseEvent e) {
        Shape currentShape = controller.getCurrentShape();
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

    /**
     * Function that manage the behaviour of the PhotoComponent while in the selecting mode
     */
    private void manageSelecting(MouseEvent e) {
        if(controller.isFlipped()) {

            List<TextBox> textBoxes = controller.getTexts();
            List<Shape> shapes = controller.getShapes();

            Point clickPoint = new Point(e.getPoint().x, e.getPoint().y);

            boolean gotTextSelection = false;

            //Checking if clicked in a TextBox and eventually select it
            for (TextBox text : textBoxes) {

                text.setIsSelected(false); //Delete of the eventual previous selected textBoxes

                if(isBehindPhoto(e.getPoint())) {

                    if(isInsideTextBox(text, e.getPoint())) {

                        if(!gotTextSelection) {
                            controller.selectAnnotation(text);
                            text.setIsSelected(true);
                            gotTextSelection = true; //Deal with one selection at a time
                        }
                    }
                }

            }

            //If no TextBoxes have been selected check of the Shapes
            boolean gotShapeSelection = false;

            //For each shape control if the click happened close to the shape and eventually select it
            for (Shape shape : shapes) {

                shape.setIsSelected(false); //Delete of the eventual previous selected textBoxes

                if(!gotShapeSelection && !gotTextSelection) {

                    gotShapeSelection = isOverShape(shape, clickPoint);

                    if(gotShapeSelection) {
                        controller.selectAnnotation(shape);
                        shape.setIsSelected(true);
                    }
                }

            }

            if(!gotShapeSelection && !gotTextSelection) {
                controller.selectAnnotation(null);
            }

            controller.getModel().notifyChangeListeners();

        }
    }

    /**
     * Function managing the dragging of a general annotation
     */
    private void manageDraggingAnnotation(MouseEvent e) {

        Annotation selectedAnnotation = controller.getSelectedAnnotation();

        if(selectedAnnotation != null) {

            if(selectedAnnotation instanceof Shape) {

                Shape selectedShape = (Shape)selectedAnnotation;

                if(isDraggable) {

                    int xOffset = e.getX() - controller.getInitialDragPoint().x;
                    int yOffset = e.getY() - controller.getInitialDragPoint().y;

                    List<Point> initialPoints = controller.getInitialShapePoints();

                    //Checking loop over the points of the shape is being moved
                    //If also one of its points go out of the canva, the function returns without updating its points
                    for (int i = 0; i < selectedShape.getPoints().size(); i++) {
                        if (!isBehindPhoto(e.getPoint())) {
                            return;
                        }
                    }

                    // Update the Shapes' position based on the mouse movement
                    for (int i = 0; i < selectedShape.getPoints().size(); i++) {
                        Point newPoint = new Point(initialPoints.get(i).x + xOffset, initialPoints.get(i).y + yOffset);
                        selectedShape.getPoints().get(i).setLocation(newPoint);
                    }

                    // Repaint the canvas or component to reflect the new TextBox position
                    controller.getModel().notifyChangeListeners();

                }

            }
            else if(selectedAnnotation instanceof TextBox) {

                TextBox selectedTextBox = (TextBox)selectedAnnotation;

                if(isDraggable) {

                    int xOffset = e.getX() - controller.getInitialDragPoint().x;
                    int yOffset = e.getY() - controller.getInitialDragPoint().y;

                    // Update the TextBox's position based on the mouse movement
                    if (isBehindPhoto(e.getPoint())) {
                        selectedTextBox.setX(controller.getInitialDragPoint().x + xOffset - imageX);
                        selectedTextBox.setY(controller.getInitialDragPoint().y + yOffset - imageY);
                    }

                    // Repaint the canvas or component to reflect the new TextBox position
                    controller.getModel().notifyChangeListeners();

                }
            }
        }
    }

    /**
     * Boolean function that is going to detect if the cursor is inside the area of the selected TextBox
     */
    private boolean isInsideTextBox(TextBox textBox, Point clickPoint) {

        int x = textBox.getX();
        int y = textBox.getY();
        int width = textBox.getWidth();
        int height = textBox.getHeight();

        boolean isInsideHorizontally = (clickPoint.x >= imageX + x) && (clickPoint.x <= imageX + x + width);
        boolean isInsideVertically = (clickPoint.y >= imageY + y - 13) && (clickPoint.y <= imageY + y + height);

        return isInsideVertically && isInsideHorizontally;
    }

    /**
     * Boolean function that is going to detect if the cursor is inside the area of the selected Shape
     */
    private boolean isOverShape(Shape shape, Point clickPoint) {

        boolean isInside = false;

        List<Line2D> lines = shape.getLines();
        for (Line2D line: lines) {

            Point lineStart = new Point((int)line.getX1(), (int)line.getY1());
            Point lineEnd = new Point((int)line.getX2(), (int)line.getY2());

            double distance = distancePointToLineSegment(lineStart, lineEnd, clickPoint);

            if (distance <= SELECTION_RANGE) {
                isInside = true;
            }
        }

        return isInside;

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
            controller.showEditToolbar(true);
        }
        else {
            controller.showEditToolbar(false);
        }

    }


    public void paintPhoto(Graphics g, Boolean isFlipped) {

        paintBackground(g);

        int parentComponentWidth = controller.getWidth();
        int parentComponentHeight = controller.getHeight();
        BufferedImage image = controller.getImage();

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
        g2d.fillRect(0, 0, controller.getWidth(), controller.getHeight());
        g2d.drawRect(0, 0, controller.getWidth(), controller.getHeight());

        // Set the drawing color and stroke
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.0f));

        //Rendering of the background drawings
        //Code found online to draw a particular pattern
        for (int x = 0; x < controller.getWidth(); x += 40) {
            for (int y = 0; y < controller.getHeight(); y += 40) {
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

        List<Shape> shapes = controller.getShapes();
        Shape currentShape = controller.getCurrentShape();

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

        List<TextBox> textBoxes = controller.getTexts();
        TextBox currentTextBox = controller.getCurrentTextBox();

        // Draw all stored textBoxes
        for (TextBox text : textBoxes) {
            text.draw(g, controller, imageX, imageY);
        }

        // Draw the current textbox (if any)
        if (currentTextBox != null) {
            currentTextBox.draw(g, controller, imageX, imageY);
        }

    }

}
