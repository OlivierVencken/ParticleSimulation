package project.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import project.main.Frame;

public class ParticleManager {
    private Random random = new Random();
    private int numberParticles = 600;
    private int numberOfGroups = 6;

    private ArrayList<Particle> particles = new ArrayList<>();
    private boolean particlesCreated = false;
    private Grid grid;

    private double rMax = 172; 
    private double friction = 0.90;
    private int forceFactor  = 1;
    private double dt = 0.01;

    private double totalForcex = 0;
    private double totalForcey = 0;
    private double[][] attraction = {
        //red, orange, yellow, green, blue, purple
         {1.0,  0.0,    0.0,    0.0,   0.0,  0.0},
         {0.0,  1.0,    0.0,    0.0,   0.0,  0.0},
         {0.0,  0.0,    1.0,    0.0,   0.0,  0.0},
         {0.0,  0.0,    0.0,    1.0,   0.0,  0.0},
         {0.0,  0.0,    0.0,    0.0,   1.0,  0.0},
         {0.0,  0.0,    0.0,    0.0,   0.0,  1.0},
    };

    /**
     * Particle manager class
     * Holds all the logic and calculations of the particles
     */
    public ParticleManager() {

    }

    /**
     * Creates the particle at the start of the simulation
     * @numberOfGroups number of groups it creates
     * @numberParticles number of particles in each group
     * Total amount of particles is numberOfGroups * numberParticles
     */
    public void createParticles() {
        for (int i = 0; i < numberOfGroups; i++) {
            for (int j = 0; j < numberParticles; j++) {
                double x = random.nextInt(5, Frame.width - 5);
                double y = random.nextInt(5, Frame.height - 5);
                int radius = 2; // fixed radius for now
                double xspeed = 0;
                double yspeed = 0;
                boolean glowEnabled = false;
                particles.add(new Particle(x, y, radius, xspeed, yspeed, i, glowEnabled));
            }
        }
        particlesCreated = true;
    }

    /**
     * Update logic for all particles
     */
    public void update() {
        grid = new Grid(rMax, particles);

        for (Particle particle : particles) {
            updateVelocity(particle, grid);
            updatePosition(particle);
        }
    }

    /**
     * Updates the velocity for a particle
     * Uses boolean enableSP to use spatial partitioning
     * @param particle Particle which velocity will be updated
     */
    private void updateVelocity(Particle particle, Grid grid) {
        totalForcex = 0;
        totalForcey = 0;

        // Efficient spatial partitioning algorithm 
        ArrayList<Particle> neighbors = grid.getNeighbors(particle);
        for (Particle neighbor : neighbors) {
            calculateDistance(particle.getX(), particle.getY(), neighbor.getX(), neighbor.getY(), attraction[particle.getGroupNumber()][neighbor.getGroupNumber()]);
        }
        
        totalForcex *= rMax * forceFactor;
        totalForcey *= rMax * forceFactor;
        particle.setXspeed(particle.getXspeed() * friction);
        particle.setYspeed(particle.getYspeed() * friction);
        particle.setXspeed(particle.getXspeed() + totalForcex * dt);
        particle.setYspeed(particle.getYspeed() + totalForcey * dt);
    }

    /**
     * Updates the position of a particle depening on  xspeed and yspeed
     * @param particle
     */
    private void updatePosition(Particle particle) {
        particle.setX(particle.getX() + particle.getXspeed() * dt);
        particle.setY(particle.getY() + particle.getYspeed() * dt);
        setInFrame(particle);
    }

    /**
     * Calcualtes the distance from to points to eachother
     * @param x1 1st point x-coordinate 
     * @param y1 1st point y-coordinate 
     * @param x2 2nd point x-coordinate 
     * @param y2 2nd point y-coordinate
     * @param a attraction value (not needed in this calculation)
     */
    private void calculateDistance(double x1, double y1 ,  double x2,  double y2, double a) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        dx = checkdx(dx);
        dy = checkdy(dy);
        double distance = Math.sqrt( dx * dx + dy * dy);
        if (distance > 0 && distance < rMax) {
            double F = force(distance/rMax, a);
            totalForcex += dx/distance * F;
            totalForcey += dy/distance * F;
        }
    }

    /**
     * Calculates the force of attraction between 2 particles with a certain distance between them
     * @param d Fistance between particles
     * @param a Attraction multiplier (can be negative for repulsion)
     * @return Force (double)
     */
    private double force(double d, double a) {
        double b = 0.2;
        if (d < b) {
            return d/b - 1;
        } else if (b < d &&  d < 1) {
            return a * (1 - Math.abs(2 * d - 1 - b) / (1 - b));
        } else {
            return 0; 
        }
    }

    /**
     * Prevents a particle from going offscreen
     * If a particle gets offscreen, is simply comes out of the other side
     * @param particle
     */
    private void setInFrame(Particle particle) {
        if (particle.getX() > Frame.width) {
            particle.setX(particle.getX() % Frame.width);
        }
        if (particle.getX() < 0) {
            particle.setX((particle.getX() % Frame.width + Frame.width) % Frame.width);
        }
        if (particle.getY() > Frame.height) {
            particle.setY(particle.getY() % Frame.height);
        } 
        if (particle.getY() < 0) {
            particle.setY((particle.getY() % Frame.height + Frame.height) % Frame.height);
        } 
    }

    /**
     * Checks if a distance in the x direction really is the shortest distance 
     */
    private double checkdx(double dx) {
        if (dx > 0.5 * Frame.width) {
            dx -= Frame.width;
        }
        if (dx < -0.5 * Frame.width) {
            dx += Frame.width;
        } 
        return dx;
    }

    /**
     * Checks if a distance in the y direction really is the shortest distance 
     */
    private double checkdy(double dy) {
        if (dy > 0.5 * Frame.height) {
            dy -= Frame.height;
        }
        if (dy < -0.5 * Frame.height) {
            dy += Frame.height;
        }
        return dy;
    }

    /**
     * Draws the info of the simulation in the top right corner
     * @param g
     * @param borderOffset
     */
    public void showInfo(Graphics g, int borderOffset) {
        g.setColor(new Color(60, 60, 60, 80));
        g.fillRect(Frame.width - 74, 0, 74, 126);

        g.setColor(Color.white);
        g.drawString("rMax: " + Math.round(rMax * 10) / 10, Frame.width - borderOffset, 60);
        g.drawString("dt: " + Math.round(dt * 10000) / 10000.0, Frame.width - borderOffset, 80);
        g.drawString("fric: " + friction, Frame.width - borderOffset, 100);
        g.drawString("F: " + Math.round(forceFactor * 10000) / 10000.0, Frame.width - borderOffset, 120);

        int startX = Frame.width - 200;
        int startY = 0;
        int cellSize = 21;
        for (int i = 0; i < attraction.length; i++) {
            for (int j = 0; j < attraction.length; j++) {
                Color gridColor = getGridColor(attraction[i][j]);
                g.setColor(gridColor);
                g.fillRect(startX + j * cellSize, startY + i * cellSize, cellSize, cellSize);
            }
        }
    }

    private Color getGridColor(double value) {
        if (value > 0) {
            return new Color(0, 128, 0, (int) (value * 255));
        } else if (value < 0) {
            return new Color(128, 0, 0, (int) (value * -255));
        } else {
            return new Color(60, 60, 60, 20);
        } 
    }

    /**
     * Randomises rules for attraction between particles
     */
    public void ChangeAttraction() {
        for (int i = 0; i < attraction.length; i++) {
            for (int j = 0; j < attraction[i].length; j++) {
                attraction[i][j] = random.nextInt(-5, 5) / 5.0;
            }
        }
    }

    /**
     * Changes the glow boolean for the particle glow effect
     */
    public void changeGlow() {
        for (Particle particle : particles) {
            if (particle.isGlowEnabled()) {
                particle.setGlow(false);
            } else {
                particle.setGlow(true);
            }
        }
    }

    /**
     * Moves the particle a certain value in the x-axis
     * @param value Amount to be moved
     */
    public void moveParticlesX(int value) {
        for (Particle particle : particles) {
            particle.setX(particle.getX() + value);
        }
    }

    /**
     * Moves the particle a certain value in the y-axis
     * @param value Amount to be moved
     */
    public void moveParticlesY(int value) {
        for (Particle particle : particles) {
            particle.setY(particle.getY() + value);
        }
    }

    /**
     * Renders the particles
     * @param g Graphics g
     */
    public void render(Graphics g) {
        if (particlesCreated) {
            for (Particle particle : particles) {
                particle.render(g);
            }

            // for debugging
            //grid.render(g);
        }
    }


    // Getters and setters
    public void setAttraction(double[][] attraction) {
        this.attraction = attraction;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public int getForceFactor() {
        return forceFactor;
    }

    public void setForceFactor(int forceFactor) {
        this.forceFactor = forceFactor;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public double getRMax() {
        return rMax;
    }

    public void setRMax(double radius) {
        this.rMax = radius;
    }

    public int getNumberParticles() {
        return numberParticles;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }
}
