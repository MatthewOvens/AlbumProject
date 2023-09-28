package com.ups.advIS.widgets.photoComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class PhotoComponentUI {

    private JScrollPane panel;

    //Possible graphical frames around the component

    /*
    So that the component looks good in cases where the window is larger than the photo itself,
    you should render some nice-looking background behind the photo,
    which will be visible when the photo doesn't cover the entire extent of the component.
    This can be as simple as a solid color, or some pattern such as graph paper.
    You'd create this effect by simply doing some drawing in your drawing method before rendering the image.
     */
    public void paint(Graphics pen, PhotoComponent canvas) {
        pen.setColor(Color.pink);
        Rectangle bounds = canvas.getBounds();
        pen.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        pen.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void paintBackground() {

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
    public void paintDrawnAnnotations() {

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
    public void paintTextAnnotations() {

    }

    public void paintImage() {

    }

}
