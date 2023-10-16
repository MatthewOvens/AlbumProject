package com.ups.advIS.album;

import com.ups.advIS.editToolbar.EditToolbar;
import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Should it be a View? Cause it's initializing the whole thing. How to deal with this which is an upper class?
public class Album { //JFrame managed to visualize the windows elements

    private AlbumModel albumModel;
    private AlbumUI albumUI;

    PhotoComponent photoComponent;

    /**
     * Constructor for the Album class, creates the main window.
     *
     * @param title The title of the application window.
     */
    public Album(String title) {
        albumModel = new AlbumModel();
        albumUI = new AlbumUI(this, title);

        try {
            File file = new File("C:/Users/forna/Desktop/EIT/UPS/Uni stuff/Advanced programming/AlbumProject/src/main/resources/icons/placesIcon.png");
            if (file.exists()) {
                BufferedImage image = ImageIO.read(file);
                addPhotoComponent(image);
            } else {
                System.err.println("Image file not found: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public PhotoComponent getPhotoComponent() {
        return photoComponent;
    }

    public void show() {
        albumUI.setVisible(true);
        albumUI.pack();
    }

    /**
     * Adding of the PhotoComponent and visualization of it
     */
    public void addPhotoComponent(BufferedImage image) {

        //For now the image get override
        deleteCurrentPhoto();

        photoComponent = new PhotoComponent(image, albumUI.editToolbar);
        albumUI.photoPanel.setViewportView(photoComponent);
        albumUI.editToolbar.setPhotoComponent(photoComponent);
        show();
    }

    public void deleteCurrentPhoto() {
        //Clean of the body
        photoComponent = null;
        albumUI.photoPanel.setViewportView(null);
        albumUI.editToolbar.getUi().setVisible(false);
    }

}

