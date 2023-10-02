package com.ups.advIS.widgets.photoComponent;
import java.awt.*;

public interface Drawable {
    //void addPoint(Point point);
    void setColor(Color color);
    void draw(Graphics g, Point scalePoint);
}