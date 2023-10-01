package com.ups.advIS.widgets.photoComponent;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Shape implements Drawable{

    List<Point> points = new ArrayList<>();
    private Color color;

    public Shape(Color color) {
        this.color = color;
    }

    @Override
    public void addPoint(Point point) {
        this.points.add(point);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Drawing function of each shape. It basically draw a line between each point in the list and the previous one
     * if it's the first one of the list it draws just a single point
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        if (points.size() != 0) {
            g.setColor(color);

            for (Point point : points) {
                if(points.indexOf(point) == 0) {
                    g.drawLine(point.x, point.y, point.x, point.y);
                }
                else {
                    g.drawLine(points.get(points.indexOf(point)-1).x, points.get(points.indexOf(point)-1).y, point.x, point.y);
                }
            }
        }
    }

}
