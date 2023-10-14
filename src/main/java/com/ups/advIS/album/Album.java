package com.ups.advIS.album;

import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledEditorKit;

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
    }

    public void show() {
        albumUI.setVisible(true);
        albumUI.pack();
    }

    public void showEditToolbar(boolean isVisible) {
        albumUI.setIsEditToolbarVisible(isVisible);
    }

    /**
     * Adding of the PhotoComponent and visualization of it
     */
    public void addPhotoComponent(BufferedImage image) {
        photoComponent = new PhotoComponent(image, this);
        albumUI.photoPanel.setViewportView(photoComponent);
        show();
    }

    public void deleteCurrentPhoto() {
        //Clean of the body
        photoComponent = null;
        albumUI.photoPanel.setViewportView(null);
        showEditToolbar(false);
    }

}

