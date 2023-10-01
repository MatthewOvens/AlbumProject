package com.ups.advIS.widgets.photoComponent;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class TextBlock {

    List<Character> textChars = new ArrayList<>();

    //Coordinates of the insertion point
    private int x;
    private int y;
    private int lineHeight;
    private int numberOfLines;
    private Color color;

    public TextBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Character> getTextChart() {
        return textChars;
    }

    public void addChar(char character) {
        this.textChars.add(character);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Drawing function of each shape. It basically draw a line between each point in the list and the previous one
     * if it's the first one of the list it draws just a single point
     * @param g
     */
    public void draw(Graphics g, int canvasWidth) {
        if (textChars.size() != 0) {
            g.setColor(color);

            FontMetrics fontMetrics = g.getFontMetrics(); // Get font metrics for text measurements
            int lineHeight = fontMetrics.getHeight(); // Get the height of a line (ascent + descent + leading)

            int currentX = x;
            int currentY = y;

            for (Character c : textChars) {
                String charString = c.toString();
                int charWidth = fontMetrics.stringWidth(charString);

                // Check if adding the character would exceed the width of the drawing area
                if (currentX + charWidth > canvasWidth) {
                    // Move to the next line
                    currentX = x;
                    currentY += lineHeight;
                }

                g.drawString(charString, currentX, currentY);

                // Update current position
                currentX += charWidth;
            }
        }
    }


}
