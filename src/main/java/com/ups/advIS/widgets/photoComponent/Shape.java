package com.ups.advIS.widgets.photoComponent;

import java.awt.geom.Line2D;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class Shape extends Annotation{

    //List of points stored for each shape used to draw them
    private List<Point> points = new ArrayList<>();

    //List of lines inside each shape
    private List<Line2D> lines = new ArrayList<>();

    public Shape(Color color) {
        setColor(color);
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Line2D> getLines() {
        return lines;
    }

    /**
     * Drawing function of each shape. It basically draw a line between each point in the list and the previous one
     * if it's the first one of the list it draws just a single point
     */
    public void draw(Graphics2D g, Point scalePoint) {
        if (points.size() >= 2) {

            g.setColor(getColor());

            if(isSelected()) {
                g.setStroke(new BasicStroke(3));
            }
            else {
                g.setStroke(new BasicStroke(1));
            }

            lines.clear();

            for (Point point : points) {

                int x = point.x + scalePoint.x;
                int y = point.y + scalePoint.y;

                if(points.indexOf(point) == 0) {
                    Line2D line = new Line2D.Float(x, y, x, y);
                    lines.add(line);
                    g.draw(line);
                }
                else {
                    Point previousPoint = points.get(points.indexOf(point) - 1);
                    int prevX = previousPoint.x + scalePoint.x;
                    int prevY = previousPoint.y + scalePoint.y;

                    Line2D line = new Line2D.Float(prevX, prevY, x, y);
                    lines.add(line);

                    g.draw(line);

                }
            }

        }
    }

}
