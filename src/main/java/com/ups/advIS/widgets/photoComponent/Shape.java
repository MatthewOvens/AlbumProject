package com.ups.advIS.widgets.photoComponent;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class Shape implements Drawable{

    List<Point> points = new ArrayList<>();
    private Color color;

    public Shape(Color color) {
        this.color = color;
    }

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
    public void draw(Graphics g, Point scalePoint) {
        if (points.size() != 0) {
            g.setColor(color);

            for (Point point : points) {

                int x = point.x + scalePoint.x;
                int y = point.y + scalePoint.y;

                if(points.indexOf(point) == 0) {
                    g.drawLine(x, y, x, y);
                }
                else {
                    Point previousPoint = points.get(points.indexOf(point) - 1);
                    int prevX = previousPoint.x + scalePoint.x;
                    int prevY = previousPoint.y + scalePoint.y;
                    g.drawLine(prevX, prevY, x, y);
                }
            }
        }
    }

}
