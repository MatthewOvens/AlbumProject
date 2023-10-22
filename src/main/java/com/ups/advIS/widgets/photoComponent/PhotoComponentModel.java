package com.ups.advIS.widgets.photoComponent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhotoComponentModel {

    private BufferedImage image;

    private List<Shape> shapes = new ArrayList<>();
    private List<TextBox> texts = new ArrayList<>();
    private Shape currentShape = null;
    private TextBox currentTextBox = null;
    private List<ChangeListener> changeListeners = new ArrayList<>();

    //Initial movement point, used for the movement function
    private Point initialDragPoint;
    //Initial movement list of points of the related Shape, used for the movement function
    private List<Point> initialShapePoints = new ArrayList<>();

    private String activeMode = "draw";  //Draw by deafault as the initial value
    private Color drawingAnnotationColor = Color.BLACK;  //Black by default

    public PhotoComponentModel(BufferedImage image) {
        this.image = image;
    }


    public BufferedImage getImage() {
        return image;
    }

    public List<TextBox> getTexts() {
        return texts;
    }

    public void addTexts(TextBox textBox) {
        this.texts.add(textBox);
    }

    public TextBox getCurrentTextBox() {
        return currentTextBox;
    }

    public void setCurrentTextBox(TextBox currentTextBox) {
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
        changeListeners.add(listener);
    }

    public Color getDrawingAnnotationsColor() {
        return drawingAnnotationColor;
    }

    public void setDrawingAnnotationColor(Color drawingColor) {
        this.drawingAnnotationColor = drawingColor;
    }

    public String getActiveMode() {
        return activeMode;
    }

    public void setActiveMode(String mode) {
        this.activeMode = mode;
    }

    public Point getInitialDragPoint() {
        return initialDragPoint;
    }

    /**
     * Function that sets the initial point of the TextBox before the movement. Used for the movement function
     */
    public void setInitialDragPoint(Point initialDragPoint) {
        this.initialDragPoint = initialDragPoint;
    }

    public List<Point> getInitialShapePoints() {
        return List.copyOf(this.initialShapePoints);
    }

    /**
     * Function that sets the initial Shape before the movement. List of points of the related Shape, used for the movement function
     */
    public void setInitialShapePoints(List<Point> points) {
        if(this.initialShapePoints.size() != 0) {
            this.initialShapePoints.clear();
        }
        for (Point point : points) {
            this.initialShapePoints.add(new Point(point));
        }

    }

    /**
     * Function that iterate over the changeListeners and throw them
     */
    public void notifyChangeListeners() {
        Iterator var1 = this.changeListeners.iterator();

        while (var1.hasNext()) {
            ChangeListener listener = (ChangeListener) var1.next();
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
