package com.ups.advIS.album;

import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Album {

    private AlbumUI albumUI;

    PhotoComponent photoComponent;

    /**
     * Constructor for the Album class, creates the main window.
     *
     * @param title The title of the application window.
     */
    public Album(String title) {
        albumUI = new AlbumUI(this, title);
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
        albumUI.pack();
        show();
    }

    public void deleteCurrentPhoto() {
        //Clean of the body
        photoComponent = null;
        albumUI.photoPanel.setViewportView(null);
        albumUI.editToolbar.getUi().setVisible(false);
    }

}

