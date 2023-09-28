package com.ups.advIS.album;

import com.ups.advIS.widgets.photoComponent.PhotoComponent;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.StyledEditorKit;

//Should it be a View? Cause it's initializing the whole thing. How to deal with this which is an upper class?
public class Album extends JFrame { //JFrame managed to visualize the windows elements

    JPanel statusPanel;
    JPanel bodyPanel;
    //JPanel bodyPanel;
    JPanel photoPanel;
    JScrollPane bodyPhotoContainer;
    public static JLabel statusBar;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int MINIMUM_SIZE = 400;
    public final Dimension frameSize = new Dimension(WIDTH,HEIGHT);
    public final Dimension minimumFrameSize = new Dimension(MINIMUM_SIZE,MINIMUM_SIZE);
    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenu view;
    public JToolBar toolbar;
    public JToggleButton peopleButton;
    public JToggleButton placesButton;
    public JToggleButton schoolButton;

    public int prova;

    PhotoComponent photoComponent;

    /**
     * Constructor for the Album class, creates the main window.
     *
     * @param title The title of the application window.
     */
    public Album(String title) {
        super(title); //Create a frame with the title passed

        setFrame();
        createStatusBar();
        createMenuBar();
        createBody();
        createToolbar();

        addPhotoComponent();
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

        //Import menu item
        JMenuItem importMenu = new JMenuItem("Import");
        importMenu.setFocusable(true);
        importMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a JFileChooser for importing photos
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(Album.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    statusBar.setText("Going to do something with the selected file (not implemented yet)");
                }
            }
        });
        fileMenu.add(importMenu);

        //Delete menu item
        JMenuItem delete = new JMenuItem("Delete");
        delete.setFocusable(true);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("Delete, will eventually delete a photo");
            }
        });
        fileMenu.add(delete);

        //Quit menu item
        JMenuItem quit = new JMenuItem("Quit");
        quit.setFocusable(true);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("Quit, exit the application");
                System.exit(1);
            }
        });
        fileMenu.add(quit);

        //View
        view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V); //To interact with it through the keyboard (Alt+V)
        menuBar.add(view);

        //Photo viewer
        JRadioButtonMenuItem photoViewer = new JRadioButtonMenuItem("Photo Viewer");
        photoViewer.setSelected(true);
        photoViewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("photoViewer, show one photo at a time");
            }
        });
        view.add(photoViewer);

        //Browser
        JRadioButtonMenuItem browser = new JRadioButtonMenuItem("Browser");
        browser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("Browser, will eventually show a grid of thumbnails");
            }
        });
        view.add(browser);

        /**** StackOverflow help
         * How to select one Radio button at a time
         */
        ButtonGroup group = new ButtonGroup();
        group.add(photoViewer);
        group.add(browser);

    }

    /**
     * Configures the main frame's size, minimum size, and location.
     */
    private void setFrame() {
        this.setSize(frameSize);
        this.setPreferredSize(frameSize);
        // Set a minimum size for the frame to ensure components are not too compressed
        this.setMinimumSize(minimumFrameSize);
        this.setLocationRelativeTo(null); //To position the frame at the center of the screen
    }

    /**
     * Creates the main content area (bodyPanel) of the window.
     */
    private void createBody() {
        //Set of a Grid layout in order to resize the PhotosComponents in the right way while resizing the frame
        bodyPanel = new JPanel();
        //photoPanel = new JPanel(new GridLayout(1,1,4,4));
        //photoPanel = new JPanel(new GridLayout(1,1,4,4));
        //bodyPanel.add(photoPanel);
        bodyPanel.setBackground(Color.decode("#A6F5FF"));
        this.add(bodyPanel);
    }

    private void addPhotoComponent() {

        //Render immediately the images stored in the model

        List<Image> storedImages = retriveStoredImages();

        //Loop over the retrived images adding the whole thing.

        //photoComponent = new PhotoComponent();
        //photoPanel.add(photoComponent);
        //bodyPanel.add(photoComponent);

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

        //

        return new ArrayList<>();

    }

}

