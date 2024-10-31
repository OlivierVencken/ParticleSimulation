package project.particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public class Particle {
    private double x;
    private double y;
    private int radius;
    private Color color;
    private double xspeed;
    private double yspeed;
    private int group;
    private boolean glow;

    private Color[] colors = {Color.red, new Color(255, 140, 0), Color.yellow, Color.green, Color.blue, new Color(156, 81, 182)};

    /**
     * Particle class 
     * @param x location of the particle at the x-axis
     * @param y location of the particle at the Y-axis
     * @param radius radius of the particle
     * @param xspeed speed of the particle in the x-direction
     * @param yspeed speed of the particle in the y-direction
     * @param group group in which the particle belongs 
     * @param glow boolean if particle glow is enabled
     */
    public Particle(double x, double y, int radius, double xspeed, double yspeed, int group, boolean glow) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = getGroupColor(group);
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        this.group = group;
        this.glow = glow;
    }

    /**
     * Renders the particle
     * Draws a oval to draw particle as a circle
     * If glow is enabled, glow is drawn via Graphics2D
     * @param g  Graphics g
     */
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x - radius, (int) y - radius, 2 * radius, 2 * radius);
        if (glow) {
            renderGlow(g);
        }
    }

    /**
     * Renders glow around the particles
     * Reduces framerate by a lot, so only run with few particles
     * @param g
     */
    private void renderGlow(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int rad = radius * 15;
        float a = (float) x;
        float b = (float) y;
        Point2D center = new Point2D.Float(a,b);
        float[] distance =  {0.0f, 1.0f};
        Color[] colors = {new Color (color.getRed(), color.getGreen(), color.getBlue(), 15), new Color(0,0,0,0)};
        RadialGradientPaint p = new RadialGradientPaint(center, rad, distance, colors);
        g2d.setPaint(p);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f));
        g2d.fillOval((int) (x - rad), (int) (y - rad), (int) 2 * rad, (int) 2 * rad); 
    }

    /**
     * Returns the color of a group
     * @param group group of the particle
     * @return color of the group
     */
    public Color getGroupColor(int group) {
        for (int i = 0; i < colors.length; i++) {
            if (i == group) {
                return colors[i];
            }
        }
        return Color.white; // return white if no group is found
    }


    // Getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public double getXspeed() {
        return xspeed;
    }

    public void setXspeed(double xspeed) {
        this.xspeed = xspeed;
    }

    public double getYspeed() {
        return yspeed;
    }

    public void setYspeed(double yspeed) {
        this.yspeed = yspeed;
    }

    public int getGroupNumber() {
        return group;
    }

    public void setGroup(int group) {
        this.color = getGroupColor(group); // match color with group
        this.group = group;
    }

    public boolean isGlowEnabled() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }
}
