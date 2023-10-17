package com.ups.advIS.widgets.photoComponent;

import javax.sound.sampled.Line;
import java.awt.geom.Line2D;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class Shape extends Annotation{

    private List<Point> points = new ArrayList<>();
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
     * @param g
     */
    public void draw(Graphics2D g, Point scalePoint) {
        if (points.size() >= 2) {

            if(isSelected()) {
                g.setColor(SELECTING_COLOR);
                // Set the stroke to create thicker borders
                g.setStroke(new BasicStroke(3));
            }
            else {
                g.setColor(getColor());
                g.setStroke(new BasicStroke(1));
            }

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
