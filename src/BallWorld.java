import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * <pre class="doc_header">
 * The main class for the Ball World package.
 * An extension of JFrame that handles creating the menu as well as being the main display.
 * This class is a simple demonstration of JFrame, JPanel, and the MouseListener.
 * The objective is to give the user the ability to create balls of different sizes and colors
 * through clicking on a JPanel
 * </pre>
 *
 * @author kelmore5
 * @custom.date Fall 2011
 */
public class BallWorld extends JFrame implements Runnable {
    private BallWorldPanel mainPanel;
    private File currentFile;
    private BallWorld()
    {
        super("Ball World");
        mainPanel = new BallWorldPanel();
        currentFile = new File("drawings.dp");
    }
    public void run()
    {
        //Set size of JFrame
        setSize(500,500);

        //Create menubar/items
        JMenuBar mbar = new JMenuBar();
        setJMenuBar(mbar);

        //Create file menu
        //Including save, open, clear balls, and quit
        JMenu fileMenu = new JMenu("File");
        mbar.add(fileMenu);

        //Save menu item
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(e -> {
            try {
                saveToCurrentFile();
            }
            catch(IOException ex) {
                System.err.printf("Could not save to %s\n", currentFile.getAbsolutePath());
            }
        });

        //Open menu item
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        openItem.addActionListener(e -> {
            try {
                readFromCurrentFile();
                mainPanel.repaint();
            }
            catch(IOException ex) {
                System.err.printf("Could not open %s\n", currentFile.getAbsolutePath());
            }
            catch(ClassNotFoundException ex) {
                System.err.println("Programmer screwup...");
            }
        });

        //Clear balls menu item
        JMenuItem clearItem = new JMenuItem("Clear Balls");
        fileMenu.add(clearItem);
        clearItem.addActionListener(e -> {
            mainPanel.clearBalls();
            repaint();
        });

        //Quit menu item
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        quitItem.addActionListener(e -> {
            System.exit(0); //make a normal termination
        });

        //Create the color file menu
        //Including red, green, blue menu items as well as random and custom
        JMenu colorMenu = new JMenu("Color");
        mbar.add(colorMenu);
        ArrayList<ColorMenuItem> colorMenuItems = new ArrayList<>();
        colorMenuItems.add(new ColorMenuItem(Color.red, "Red"));
        colorMenuItems.add(new ColorMenuItem(Color.green, "Green"));
        colorMenuItems.add(new ColorMenuItem(Color.blue, "Blue"));
        for(ColorMenuItem c: colorMenuItems) {
            colorMenu.add(c);
        }
        colorMenu.add(new CustomColor("Choose Circle Color", mainPanel.circleColor));
        colorMenu.add(new RandomColor("Random Circle Color"));

        //Background file menu
        //Including red, green, blue, custom, and random
        JMenu backgroundMenu = new JMenu("Background");
        mbar.add(backgroundMenu);
        ArrayList<BackgroundMenuItem> backgroundMenuItems = new ArrayList<>();
        backgroundMenuItems.add(new BackgroundMenuItem(Color.red, "Red"));
        backgroundMenuItems.add(new BackgroundMenuItem(Color.green, "Green"));
        backgroundMenuItems.add(new BackgroundMenuItem(Color.blue, "Blue"));
        for(BackgroundMenuItem c: backgroundMenuItems)
        {
            backgroundMenu.add(c);
        }
        backgroundMenu.add(new CustomColor("Choose Background Color", mainPanel.panelColor));
        backgroundMenu.add(new RandomColor("Random Background Color"));

        //File menu for radius
        //Includes 5, 10, 50, 100, custom, and random
        JMenu sizeMenu = new JMenu("Radius");
        mbar.add(sizeMenu);
        ArrayList<SizeMenuItem> sizeMenuItems = new ArrayList<>();
        sizeMenuItems.add(new SizeMenuItem(5, "5"));
        sizeMenuItems.add(new SizeMenuItem(10, "10"));
        sizeMenuItems.add(new SizeMenuItem(50, "50"));
        sizeMenuItems.add(new SizeMenuItem(100, "100"));
        for(SizeMenuItem c: sizeMenuItems) {
            sizeMenu.add(c);
        }
        JMenuItem customSizeMenuItem = new JMenuItem("Custom");
        sizeMenu.add(customSizeMenuItem);
        customSizeMenuItem.addActionListener(e -> {
            int out = 0;
            String customSize = JOptionPane.showInputDialog("Please input a value:");
            try {
                out = Integer.parseInt(customSize);
            }
            catch(NumberFormatException ex) {
                out = mainPanel.getRadius();
            }
            finally {
                mainPanel.setRadius(out);
            }
        });
        sizeMenu.add(new RandomNumber());

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    /**
     * Function to save current ball configuration to storage
     * @throws IOException  Thrown when file cannot be saved
     */
    private void saveToCurrentFile() throws IOException
    {
        //Open object output stream
        //Save positions, panel color, circle color, radius, and balls
        //Then close
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentFile));
        oos.writeObject(mainPanel.getPositions());
        oos.writeObject(mainPanel.getPanelColor());
        oos.writeObject(mainPanel.getCircleColor());
        oos.writeInt(mainPanel.getRadius());
        oos.writeObject(mainPanel.getBalls());
        oos.close();
    }

    /**
     * Function to read a saved ball file and paint it to the frame
     * @throws IOException  Thrown when file cannot be read from storage
     * @throws ClassNotFoundException   //Thrown when an ObjectInputStream cannot be created
     */
    @SuppressWarnings("unchecked")
    private void readFromCurrentFile() throws IOException, ClassNotFoundException
    {
        //Open file and get:
        //Ball positions, panel color, circle color, radius, and balls
        //Then paint to frame
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentFile));
        mainPanel.setPositions((ArrayList<int[]>) ois.readObject());
        mainPanel.setPanelColor((Color) ois.readObject());
        mainPanel.setCircleColor((Color) ois.readObject());
        mainPanel.setRadius(ois.readInt());
        mainPanel.setBalls((ArrayList<Ball>) ois.readObject());
        repaint();
        ois.close();
    }

    /**
     * Class for defining the menu
     */
    public class BackgroundMenuItem extends JMenuItem
    {
        /**
         * Color of the panel background
         */
        private final Color color;

        /**
         * Instantiates a new Background menu item.
         *
         * @param _color     the color of the panel
         * @param _colorName the name for the file menu option for panel color
         */
        BackgroundMenuItem(Color _color, String _colorName)
        {
            //Set menu to color name and then paint panel
            super(_colorName);
            color = _color;
            addActionListener(e -> {
                mainPanel.setPanelColor(color);
                mainPanel.repaint();
            });
        }
    }

    /**
     * Menu item for changing ball menu color
     */
    public class ColorMenuItem extends JMenuItem
    {
        /**
         * Color of the balls in the frame
         */
        private final Color color;

        /**
         * Instantiates a new Color menu item.
         *
         * @param _color     the color of the balls
         * @param _colorName the name for the color file menu option
         */
        ColorMenuItem(Color _color, String _colorName)
        {
            //Set file menu and then set color
            super(_colorName);
            color = _color;
            addActionListener(e -> mainPanel.setCircleColor(color));
        }

    }

    /**
     * Menu item for changing radius size of balls
     */
    public class SizeMenuItem extends JMenuItem
    {
        /**
         * The radius of the balls
         */
        private final int radius;

        /**
         * Instantiates a new Size menu item.
         *
         * @param _radius     the radius of the balls
         * @param _radiusName the name of the file menu option
         */
        SizeMenuItem(int _radius, String _radiusName)
        {
            super(_radiusName);
            radius = _radius;
            addActionListener(e -> mainPanel.setRadius(radius));
        }
    }

    /**
     * File menu item for changing ball to a custom color
     */
    public class CustomColor extends JMenuItem
    {
        /**
         * The custom color for the ball
         */
        private final Color color;
        /**
         * The name of the file menu option
         */
        private final String customName;

        /**
         * Instantiates a new Custom color.
         *
         * @param _customName the name of the menu option
         * @param _color      the custom color for the balls
         */
        CustomColor(String _customName, Color _color)
        {
            //Set color/name
            super("Custom");
            color = _color;
            customName = _customName;

            //Add action listener for creating a JColorChooser which is Java's
            //built in method for having a user submit a custom color
            //Once color chose, repaint
            addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(mainPanel, customName, color);
                if(customName.contains("Circle"))
                {
                    mainPanel.setCircleColor(newColor);
                }
                else
                {
                    mainPanel.setPanelColor(newColor);
                    mainPanel.repaint();
                }
            });
        }
    }

    /**
     * Menu item for creating a random color
     */
    public class RandomColor extends JMenuItem
    {
        /**
         * The name of the file menu option
         */
        private final String customName;

        /**
         * Instantiates a new Random color.
         *
         * @param _customName the name of the file menu option
         */
        RandomColor(String _customName)
        {
            //Set variables
            super("Random");
            customName = _customName;

            //Create action listener for creating a random color
            addActionListener(e -> {
                //Make an array to hold the random red, green, blue (RGB) values
                ArrayList<Integer> randomRGB = new ArrayList<>();
                for(int n = 0; n <= 2; n++)
                {
                    //Get values
                    randomRGB.add((int) Math.round(Math.random()*255));
                }

                //Create color, set as circle or panel, and repaint
                Color randomColor = new Color(randomRGB.get(0), randomRGB.get(1), randomRGB.get(2), 255);
                if(customName.contains("Circle"))
                {
                    mainPanel.setCircleColor(randomColor);
                }
                else
                {
                    mainPanel.setPanelColor(randomColor);
                    mainPanel.repaint();
                }
            });
        }
    }

    /**
     * Menu item for selecting a random radius for the balls
     */
    public class RandomNumber extends JMenuItem
    {
        /**
         * Instantiates a new Random number.
         */
        RandomNumber()
        {
            super("Random");
            addActionListener(e -> mainPanel.setRadius((int) Math.round(Math.random()*500)));
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        BallWorld bw = new BallWorld();
        javax.swing.SwingUtilities.invokeLater(bw);
    }
}
