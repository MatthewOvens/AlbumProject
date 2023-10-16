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
    private final int lineHeight = 15;

    public TextBlock(int x, int y) {
        this.x = x;
        this.y = y;
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

        //TODO Break points andando a capo

        if (textChars.size() != 0) {

            BufferedImage image = canvas.getModel().getImage();

            g.setColor(getColor());

            FontMetrics fontMetrics = g.getFontMetrics(); // Get font metrics for text measurements

            int currentX = x + imageX;
            int currentY = y + imageY;

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
                    currentX = x + imageX;
                    currentY += lineHeight;
                }

                // Apply uppercase if Caps Lock is on or Shift is pressed
                char charToDraw = isCapsLockOn || isShiftPressed ? Character.toUpperCase(c) : c;
                String charString = String.valueOf(charToDraw);
                int charWidth = fontMetrics.stringWidth(charString); //Width of the single char

                // Check if adding the character would exceed the width of the drawing area
                if (currentX + charWidth > imageX + image.getWidth()) {
                    // Move to the next line
                    currentX = x + imageX;
                    currentY += lineHeight;
                }

                // Adding the character only if it's not exceeding the height of the drawing area
                if (currentY <= imageY + image.getHeight()) {
                    g.drawString(charString, currentX, currentY);
                    // Update current position
                    currentX += charWidth;
                }

            }
        }
    }


}
