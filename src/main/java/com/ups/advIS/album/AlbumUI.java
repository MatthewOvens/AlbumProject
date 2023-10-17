package com.ups.advIS.album;

import com.ups.advIS.editToolbar.EditToolbar;
import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumUI extends JFrame{

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int MINIMUM_SIZE = 400;
    public final Dimension frameSize = new Dimension(WIDTH,HEIGHT);
    public final Dimension minimumFrameSize = new Dimension(MINIMUM_SIZE,MINIMUM_SIZE);

    public static JLabel statusBar;
    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenu view;
    public JToolBar toolbar;
    public JToggleButton peopleButton;
    public JToggleButton placesButton;
    public JToggleButton schoolButton;

    public EditToolbar editToolbar;

    public JPanel statusPanel;
    public JPanel bodyPanel;
    public JScrollPane photoPanel;

    private Album albumController;

    public AlbumUI(Album controller, String title) {
        super(title);

        albumController = controller;

        setFrame();
        createStatusBar();
        createMenuBar();
        createBody();
        createToolbar();
        createEditToolbar();

    }

    public void createEditToolbar() {
        editToolbar = new EditToolbar();
        this.add(editToolbar.getUi(), BorderLayout.NORTH);
    }

    /**
     * Creates and configures the menu bar with menus and menu items.
     */
    private void createMenuBar() {

        //Menu
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        //File
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F); //To interact with it through the keyboard (Alt+F)
        menuBar.add(fileMenu);

        //File -> Import menu item
        JMenuItem importMenu = new JMenuItem("Import");
        importMenu.setFocusable(true);
        fileMenu.add(importMenu);

        //File -> Delete menu item
        JMenuItem delete = new JMenuItem("Delete");
        delete.setFocusable(true);
        fileMenu.add(delete);

        //File -> Quit menu item
        JMenuItem quit = new JMenuItem("Quit");
        quit.setFocusable(true);
        fileMenu.add(quit);

        //View
        view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V); //To interact with it through the keyboard (Alt+V)
        menuBar.add(view);

        //View -> Photo viewer
        JRadioButtonMenuItem photoViewer = new JRadioButtonMenuItem("Photo Viewer");
        photoViewer.setSelected(true);
        view.add(photoViewer);

        //View -> Browser
        JRadioButtonMenuItem browser = new JRadioButtonMenuItem("Browser");
        view.add(browser);

        /**** StackOverflow help
         * How to select one Radio button at a time
         */
        ButtonGroup group = new ButtonGroup();
        group.add(photoViewer);
        group.add(browser);

        //Listeners
        importMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a JFileChooser for importing photos
                JFileChooser fileChooser = new JFileChooser();

                // File filter that accepts image files (e.g., PNG, JPEG, GIF)
                FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(imageFilter);

                int returnValue = fileChooser.showOpenDialog(AlbumUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();

                        // Check if the selected file has an allowed image file extension
                        String fileName = selectedFile.getName().toLowerCase();
                        if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif")) {
                            BufferedImage image = ImageIO.read(selectedFile);
                            albumController.addPhotoComponent(image);
                        } else {
                            statusBar.setText("Invalid File. Please select a valid image file (PNG, JPG, JPEG, GIF).");
                        }
                    }
                    catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                albumController.deleteCurrentPhoto();
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("Quit, exit the application");
                System.exit(1);
            }
        });

        photoViewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("photoViewer, show one photo at a time");
            }
        });

        browser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("Browser, will eventually show a grid of thumbnails");
            }
        });

    }

    /**
     * Configures the main frame's size, minimum size, and location.
     */
    private void setFrame() {
        setSize(frameSize);
        setPreferredSize(frameSize);
        // Set a minimum size for the frame to ensure components are not too compressed
        setMinimumSize(minimumFrameSize);
        setLocationRelativeTo(null); //To position the frame at the center of the screen
    }

    /**
     * Creates the main content area (bodyPanel) of the window.
     */
    private void createBody() {
        bodyPanel = new JPanel(new BorderLayout());
        photoPanel = new JScrollPane();
        bodyPanel.add(photoPanel, BorderLayout.CENTER);

        this.add(bodyPanel);
    }

    /**
     * Adds an item listener to a toggle button to handle selection events.
     *
     * @param toggle The toggle button to which the listener is added.
     */
    private void setToggleButtonAction(JToggleButton toggle) {

        /** StackOverflow help
         * How to detect the selection of one of the toggle buttons
         */
        toggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if(toggle.getText() == "People") {
                        statusBar.setText("People toggle button selected");
                    }
                    else if(toggle.getText() == "Places") {
                        statusBar.setText("Places toggle button selected");
                    }
                    else if(toggle.getText() == "School") {
                        statusBar.setText("School toggle button selected");
                    }
                }
            }
        });

    }

    /**
     * Creates and configures the toolbar on the left side of the window.
     */
    private void createToolbar() {
        toolbar = new JToolBar(SwingConstants.VERTICAL);
        toolbar.setBackground(Color.decode("#DEDAD9"));
        toolbar.setBorder(new EmptyBorder(10, 10, 0, 20));
        toolbar.setFloatable(false);

        //Set of a Grid layout in order to resize the buttons in the right way while resizing the frame
        JPanel toolkitPanel = new JPanel(new GridLayout(10,1,4,4));

        toolkitPanel.setBackground(Color.decode("#DEDAD9"));
        toolbar.add(toolkitPanel);

        peopleButton = new JToggleButton("People", new ImageIcon("src/main/resources/icons/peopleIcon.png"));
        placesButton = new JToggleButton("Places", new ImageIcon("src/main/resources/icons/placesIcon.png"));
        schoolButton = new JToggleButton("School", new ImageIcon("src/main/resources/icons/schoolIcon.png"));

        setToggleButtonAction(peopleButton);
        setToggleButtonAction(placesButton);
        setToggleButtonAction(schoolButton);

        toolkitPanel.add(peopleButton);
        toolkitPanel.add(placesButton);
        toolkitPanel.add(schoolButton);
        this.add(toolbar, BorderLayout.WEST);

    }

    /**
     * Creates the status bar at the bottom of the window.
     */
    private void createStatusBar() {
        statusPanel = new JPanel();
        statusBar =  new JLabel("Status bar ...");
        statusPanel.add(statusBar);
        this.add(statusPanel, BorderLayout.SOUTH);
    }

    public List<Image> retriveStoredImages() {

        //Eventually to retriveStoredImages

        return new ArrayList<>();
    }



}
