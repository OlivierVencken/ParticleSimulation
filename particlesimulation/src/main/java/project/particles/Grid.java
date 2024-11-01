package project.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

import project.main.Frame;

public class Grid {

    private double cellSize;
    private int cols;
    private int rows;
    private ArrayList<HashSet<Particle>> cells;

    /**
     * Grid contructor for spatial partitioning
     * @param cellSize size of a gridcell
     */
    public Grid(double cellSize, ArrayList<Particle> particles) {
        this.cols = (int) Math.ceil(Frame.width / cellSize);
        this.rows = (int) Math.ceil(Frame.height / cellSize);
        this.cellSize = cellSize;

        cells = new ArrayList<>(cols * rows);
        for (int i = 0; i < cols * rows; i++) {
            cells.add(new HashSet<>());
        }

        for (Particle particle : particles) {
            addParticle(particle);
        }
    }

    /**
     * Adds a particle to a gridcell based on its position
     * @param particle particle to be added to the grid
     */
    public void addParticle(Particle particle) {
        int cellIndex = getCellIndex(particle);
        cells.get(cellIndex).add(particle);
    }

    /**
     * Returns the neighbors of a particle
     * First gets the cell and adjacent cells,
     * then retrieves the particles inside these cells
     * @param particle particle to get neighbors from 
     * @return Arraylist of neighbor particles
     */
    public ArrayList<Particle> getNeighbors(Particle particle) {
        ArrayList<Particle> neighbors = new ArrayList<>();
        int cellIndex = getCellIndex(particle);
        int col = cellIndex % cols;
        int row = cellIndex / cols;
    
        for (int i = col - 1; i <= col + 1; i++) {
            for (int j = row - 1; j <= row + 1; j++) {
                int wrappedI = (i + cols) % cols; // Wrap around for columns
                int wrappedJ = (j + rows) % rows; // Wrap around for rows
                neighbors.addAll(cells.get(wrappedJ * cols + wrappedI));
            }
        }
        // Remove the central particle itself if it was added
        neighbors.remove(particle);
        return neighbors;
    }
    
    /**
     * Returns the index of a cell of the particle
     * @param particle particle which cell index is returned
     * @return cell index of particle
     */
    private int getCellIndex(Particle particle) {
        int col = (int) (particle.getX() / cellSize);
        int row = (int) (particle.getY() / cellSize);
        return row * cols + col;
    }

    /**
     * Renders the gridcells for debugging
     * @param g
     */
    public void render(Graphics g) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                g.setColor(Color.white);
                g.drawRect((int) (i*cellSize), (int) (j*cellSize), (int) cellSize, (int) cellSize);
            }
        }
    }
}

