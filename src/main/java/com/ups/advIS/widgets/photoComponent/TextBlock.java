package com.ups.advIS.widgets.photoComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class TextBlock extends Annotation {

    //Characters stored in the TextBlock
    List<Character> textChars = new ArrayList<>();

    //Coordinates of the insertion point
    private int x;
    private int y;

    private int width;
    private int height;

    public TextBlock(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Function to delete both the char of the Backspace key and the one before, so the actual char to delete
     */
    private void deleteOperation() {
        textChars.remove(textChars.size() - 1);

        if(!textChars.isEmpty()) {
            textChars.remove(textChars.size() - 1);
        }

    }


    /**
     * Drawing function of each TextBox.
     */
    public void draw(Graphics g, PhotoComponent canvas, int imageX, int imageY) {

        //TODO Break points andando a capo (FORSE)

        if (textChars.size() != 0) {

            BufferedImage image = canvas.getModel().getImage();

            g.setColor(getColor());

            FontMetrics fontMetrics = g.getFontMetrics(); // Get font metrics for text measurements

            int currentX = this.x + imageX;
            int currentY = this.y + imageY;
            this.width = 0;
            this.height = fontMetrics.getHeight();

            boolean isCapsLockOn = false;
            boolean isShiftPressed = false;

            //If the last char is a delete operation, delete it and the last chart
            if(textChars.get(textChars.size()-1).hashCode() == 8) {
                deleteOperation();
            }

            for (Character c : textChars) {
                int charHashCode = c.hashCode();

                // Handle special characters based on hash codes. Enter
                if(charHashCode == 10) {
                    // Enter (Newline)
                    currentX = this.x + imageX;
                    currentY += fontMetrics.getHeight();

                    if(currentY <= imageY + image.getHeight()) {
                        //Update height
                        height += fontMetrics.getHeight();
                    }
                }

                // Apply uppercase if Caps Lock is on or Shift is pressed
                char charToDraw = isCapsLockOn || isShiftPressed ? Character.toUpperCase(c) : c;
                String charString = String.valueOf(charToDraw);
                int charWidth = fontMetrics.stringWidth(charString); //Width of the single char

                // Check if adding the character would exceed the width of the drawing area
                if (currentX + charWidth > imageX + image.getWidth()) {
                    // Move to the next line
                    currentX = x + imageX;
                    currentY += fontMetrics.getHeight();
                    this.height += fontMetrics.getHeight();
                }

                // Adding the character only if it's not exceeding the height of the drawing area
                if (currentY <= imageY + image.getHeight()) {

                    g.drawString(charString, currentX, currentY);
                    // Update current position
                    currentX += charWidth;

                    this.width = Math.max(this.width, currentX - (x + imageX));
                }

            }

            if(isSelected()) {
                Graphics2D g2d = (Graphics2D) g;
                // Draw the rectangle that goes around the whole textBox based on the calculated width and height
                g.setColor(SELECTING_COLOR);
                // Set the stroke to create thicker borders
                g2d.setStroke(new BasicStroke(1));

                g2d.drawRect(x + imageX - 2, y + imageY - 13, width + 7, height + 5);
            }

        }

    }


}
