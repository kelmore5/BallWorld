import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * <pre class="doc_header">
 * An extension of JPanel that implements a MouseListener.
 * Used for {@link BallWorld} for the main panel in its frame.
 * Keeps track of the list of balls, the current circle and panel color,
 * the current ball radius, and the position of all the balls (for drawing
 * purposes).
 * </pre>
 *
 * @author kelmore5
 * @custom.date Fall 2011
 */
public class BallWorldPanel extends JPanel implements MouseListener {
    Color panelColor, circleColor;
    private int circleRadius;
    private ArrayList<Ball> balls;
    private ArrayList<int[]> positions;

    /**
     * Instantiates a new Ball world panel.
     */
    BallWorldPanel() {
        positions = new ArrayList<>();              //Create the position ArrayList
        balls = new ArrayList<>();                  //Create the ball ArrayList
        panelColor = Color.black;                   //Specify default panel color (black)
        circleColor = Color.red;                    //Specify default circle color (red)
        circleRadius = 100;                         //Specify default radius (100)
        addMouseListener(this);        //Add the mouse listener
    }

    public void paintComponent(Graphics g) {
        //Set panel color and then draw all the balls
        g.setColor(panelColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Ball c : balls) {
            c.draw(g);
        }
    }

    /**
     * Allows for the set of balls in the panel to be replaced with a new one
     *
     * @param b An ArrayList of Ball's
     */
    void setBalls(ArrayList<Ball> b) {
        balls = b;
    }

    /**
     * Allows for the set of ball positions in the panel to be replaced with a new one
     *
     * @param p An ArrayList of positions
     */
    void setPositions(ArrayList<int[]> p) {
        positions = p;
    }


    /**
     * Sets the panel color.
     *
     * @param c the new color for the panel
     */
    void setPanelColor(Color c) {
        panelColor = c;
    }

    /**
     * Sets the circle color.
     *
     * @param c the new color for the circles
     */
    void setCircleColor(Color c) {
        circleColor = c;
    }

    /**
     * Gets all the balls in the panel
     *
     * @return An ArrayList of all the balls
     */
    ArrayList<Ball> getBalls() {
        return balls;
    }

    /**
     * Gets the positions of all the balls
     *
     * @return An ArrayList of all the positions of the balls
     */
    ArrayList<int[]> getPositions() {
        return positions;
    }

    /**
     * Gets the panel color.
     *
     * @return the panel color
     */
    Color getPanelColor() {
        return panelColor;
    }

    /**
     * Gets the circle color.
     *
     * @return the circle color
     */
    Color getCircleColor() {
        return circleColor;
    }

    /**
     * Gets the current radius of the balls
     *
     * @return the radius
     */
    int getRadius() {
        return circleRadius;
    }

    /**
     * Empties the list of balls
     */
    void clearBalls() {
        balls.clear();
    }

    /**
     * Sets the radius for new balls created
     *
     * @param radius the new radius
     */
    void setRadius(int radius) {
        circleRadius = radius;
    }

    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        //Get the position of the mouse
        int[] point = new int[2];
        point[0] = e.getX() - circleRadius;
        point[1] = e.getY() - circleRadius;

        //Add new position to @positions,
        //Add new ball to @balls,
        //repaint
        positions.add(point);
        balls.add(new Ball(circleColor, point, circleRadius));
        repaint();
    }
}
