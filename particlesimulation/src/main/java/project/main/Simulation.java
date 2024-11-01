package project.main;

import java.awt.Color;
import java.awt.Graphics;

import project.particles.ParticleManager;

public class Simulation implements Runnable{
    
    private Panel panel;
    private Thread simulationThread;

    private final int FPS_SET = 120;
    private final int UPS_SET = 60;

    private int frames = 0;
    private int updates = 0;
    private long lastCheck = 0;
    private String fps = "FPS: ";
    private String ups = "UPS: ";

    private ParticleManager particleManager;

    /**
     * Simulation constructor
     * - Initiates classes
     * - Runs the main loop
     */
    public Simulation() {
        initClasses();

        panel = new Panel(this);
        new Frame(panel);
        particleManager.createParticles();

        panel.requestFocus();

        startSimulation();
    }

    /*
     * Initiates the classes
     */
    private void initClasses() {
        particleManager = new ParticleManager();
    }

    /**
     * Returns the particle manager
     * @return Particle manager
     */
    public ParticleManager getParticleManager() {
        return particleManager;
    }

    /**
     * Starts the simulation
     */
    private void startSimulation() {
        simulationThread = new Thread(this);
        simulationThread.start();
    }

    /**
     * Updates the simulation
     */
    public void update() {
        particleManager.update();
    }

    /**
     * Renders the particle manager
     * @param g  Graphics hg
     */
    public void render(Graphics g) {
        particleManager.render(g);
        updateCounters(g);
    }

    /**
     * Update counters each second
     * @param g
     */
    private void updateCounters(Graphics g) {
        if (System.currentTimeMillis() - lastCheck >= 1000) {
            lastCheck = System.currentTimeMillis();
            fps = "FPS: " + frames;
            ups = "UPS: " + updates;
            frames = 0;
            updates = 0;
        }

        int borderOffset = 60;
        
        g.setColor(Color.GREEN);
        g.drawString(fps, Frame.width - borderOffset, 20);
        g.drawString(ups, Frame.width - borderOffset, 40);

        particleManager.showInfo(g, borderOffset);
    }

    /**
     * Runnable thread
     * Runs the loop on seperate thread
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET; 
        double timePerUpdate = 1000000000.0 / UPS_SET; 

        long previousTime = System.nanoTime();

        double deltaU = 0;
        double deltaF = 0;

        while(true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                panel.repaint();
                frames++;
                deltaF--;
            }
        }
    }
}
