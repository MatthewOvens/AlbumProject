package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class PhotoComponentUI {

    private JScrollPane panel;

    private int imageX;
    private int imageY;
    private int imageNewWidth;
    private int imageNewHeight;

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public int getImageNewWidth() {
        return imageNewWidth;
    }

    public int getImageNewHeight() {
        return imageNewHeight;
    }

    //Possible graphical frames around the component Background stuff

    /*
    So that the component looks good in cases where the window is larger than the photo itself,
    you should render some nice-looking background behind the photo,
    which will be visible when the photo doesn't cover the entire extent of the component.
    This can be as simple as a solid color, or some pattern such as graph paper.
    You'd create this effect by simply doing some drawing in your drawing method before rendering the image.
     */
    public void paintPhoto(Graphics g, PhotoComponent canvas, Boolean isFlipped) {

        paintBackground(g, canvas);

        int parentComponentWidth = canvas.getWidth();
        int parentComponentHeight = canvas.getHeight();
        BufferedImage image = canvas.getModel().getImage();

        //ChatCPT help code in order to scale in the right way the images inside the parent component
        //prompt: "How can I maintain the image contained inside a parent component in Java Swing, keeping its original aspect ratio?"
        // Calculate the scaling factors to fit the image inside the component
        double scaleX = (double) parentComponentWidth / image.getWidth();
        double scaleY = (double) parentComponentHeight / image.getHeight();

        // Use the minimum scaling factor to maintain aspect ratio
        double scale = Math.min(scaleX, scaleY);

        // Calculate the dimensions of the scaled image
        imageNewWidth = (int) (image.getWidth() * scale);
        imageNewHeight = (int) (image.getHeight() * scale);

        // Calculate the position to center the image within the component
        imageX = (parentComponentWidth - imageNewWidth) / 2;
        imageY = (parentComponentHeight - imageNewHeight) / 2;

        //Useful to avoid code duplication
        if(isFlipped) {
            g.setColor(Color.WHITE);
            g.fillRect(imageX, imageY, imageNewWidth, imageNewHeight);
            g.drawRect(imageX, imageY, imageNewWidth, imageNewHeight);

            paintDrawnAnnotations(g, canvas);
            paintTextAnnotations(g, canvas);

            g.dispose();
        }
        else {
            g.drawImage(image, imageX, imageY, imageNewWidth, imageNewHeight, null);

            g.dispose();
        }
    }

    public void paintBackground(Graphics g, PhotoComponent canvas) {

        // Create a Graphics2D object for advanced drawing
        Graphics2D g2d = (Graphics2D) g;

        // Set the background color
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.drawRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Set the drawing color and stroke
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.0f));

        //Rendering of the background drawings
        //Code found online to draw a particular pattern
        for (int x = 0; x < canvas.getWidth(); x += 40) {
            for (int y = 0; y < canvas.getHeight(); y += 40) {
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

    /*
     What this means is that the user should be able to draw freehand strokes by dragging the mouse on the back of the
     photo with the button pressed. The component should show the stroke while it is in the process of being drawn,
     to give appropriate feedback to the user.
     Drawing should only occur in the white back-of-photo area, not the background.


     Hint: Remember that you'll need to redraw all of these same strokes anytime the Swing repaint pipeline
     tells you that you need to paint your component. The classic way to do this is to add strokes to a display list
     that contains the things to be rendered, and then in your paint code you simply iterate through the items to be painted,
     rendering them to the screen.

     Hint: Painted strokes will look much better if you use Java2D's anti-aliasing mechanism.
     Look at the setRenderingHints()method on Graphics2D.
     */
    public void paintDrawnAnnotations(Graphics g, PhotoComponent canvas) {

        //Corretto che reprinti tutto dall'inizio anche quando sto modificando qua?

        List<Shape> shapes = canvas.getModel().getShapes();
        Shape currentShape = canvas.getModel().getCurrentShape();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw all stored shapes
        for (Shape shape : shapes) {
            shape.draw(g2d);
        }

        // Draw the current shape (if any)
        if (currentShape != null) {
            currentShape.draw(g2d);
        }

    }

    /*
    The way this should work is that the user clicks on the photo back to set an insertion point for the text.
    Then, any typing will begin to fill the photo back starting at that insertion point.
    Clicking again will reset the insertion point to another position.

    - You should implement wrapping. This means that when your typing hits the end of the photo back the text should continue to
    display starting on the next line.
    - Use reasonable line spacing. Remember: ascent + descent + leading.

    The key is to remember that, as with strokes above, you'll need to keep a data structure for the text that will be rendered
    by your component. One way to architect things is to simply create a new object to hold a text block whenever the insertion point is set;
    this object only needs to remember the insertion point and the set of characters entered at that point.
    characters are typed Whenever they are simply added to the current text block object. The job of your paint code, then,
    is simply to iterate over the list of text blocks and draw them to the screen, wrapping as you draw based on the size of the photo back.

     Hint: Telling the difference between "text mode" and "draw mode" should be easy:
     If you see a mouse down followed by movement, you can assume you're drawing.
     If you see a mouse click (press followed by release), you can assume you've set the text insertion point for keyboard entry.
     (QUESTA UNA STRONZATA SECONOD ME BASTEREBBE CAMBIARE UN ATTIMO METTENDOCI PER ESEMPIO LO STROKE CON IL CLICK DESTRO)

     Hint: If you find yourself needing to do fancy FocusManagerstuff, you're probably working too hard.
     You should just be able to add KeyListeners to your component, and call setFocusable(true)on it.
     */
    public void paintTextAnnotations(Graphics g, PhotoComponent canvas) {

        //Corretto che reprinti tutto dall'inizio anche quando sto modificando qua?

        List<TextBlock> textBlocks = canvas.getModel().getTexts();
        TextBlock currentTextBlock = canvas.getModel().getCurrentTextBox();

        //Graphics2D g2d = (Graphics2D) g;
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw all stored shapes
        for (TextBlock text : textBlocks) {
            text.draw(g, canvas.getWidth());
        }

        // Draw the current shape (if any)
        if (currentTextBlock != null) {
            currentTextBlock.draw(g, canvas.getWidth());
        }

    }

}
