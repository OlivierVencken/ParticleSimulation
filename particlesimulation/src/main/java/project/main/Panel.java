package project.main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import project.inputs.KeyboardInputs;

public class Panel extends JPanel{

    private Simulation simulation;

    /**
     * Panel of the simulation
     * - Background is black
     * - Adds keylisteners to interact with the simulation
     * @param particleSimulation Simulation to add to the panel
     */
    public Panel(Simulation particleSimulation) {
        setBackground(Color.BLACK);
        addKeyListener(new KeyboardInputs(particleSimulation));
        simulation = particleSimulation;
    }

    /**
     * Main paintcomponent
     * Gets called each repaint
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        simulation.render(g);
    }
}
