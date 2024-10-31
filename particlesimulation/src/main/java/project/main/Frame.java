package project.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame{

    public static int width = 1800;
    public static int height = 1200;

    /**
     * Frame of the simulation.
     * - not resizable 
     * @param panel Panel with the particles
     */
    public Frame(JPanel panel) {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
            width = gd.getDefaultConfiguration().getBounds().width;
            height = gd.getDefaultConfiguration().getBounds().height;
        } else {
            setSize(width, height);
            setVisible(true);
        }

        add(panel);
        setVisible(true);
    } 
}
