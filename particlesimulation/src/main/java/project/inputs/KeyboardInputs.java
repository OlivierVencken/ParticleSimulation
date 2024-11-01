package project.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import project.main.Simulation;
import project.particles.ParticleManager;

public class KeyboardInputs implements KeyListener{

    ParticleManager particleManager;

    /**
     * Keyboard input class to interact with the simulation
     * @param simulation Particle simulation
     */
    public KeyboardInputs(Simulation simulation) {
        particleManager = simulation.getParticleManager();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Exit the simulation
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            // Change environment settings
            case KeyEvent.VK_COMMA:
                particleManager.setFriction(particleManager.getFriction() - 0.01);
                break;
            case KeyEvent.VK_PERIOD:
                particleManager.setFriction(particleManager.getFriction() + 0.01);
                break;
            case KeyEvent.VK_RIGHT:
                particleManager.setDt(particleManager.getDt() * 1.2);
                break;
            case KeyEvent.VK_LEFT:
            particleManager.setDt(particleManager.getDt() * (double) 5/6);
                break;
            case KeyEvent.VK_UP:
                particleManager.setForceFactor(particleManager.getForceFactor() + 1);
                break;
            case KeyEvent.VK_DOWN:
                particleManager.setForceFactor(particleManager.getForceFactor() - 1);  
                break;
            case KeyEvent.VK_K:
                particleManager.setRMax(particleManager.getRMax() - 1);
                break;
            case KeyEvent.VK_L:
                particleManager.setRMax(particleManager.getRMax() + 1);  
                break;

            // Move particles 
            case KeyEvent.VK_W:
                particleManager.moveParticlesY(-10);
                break;
            case KeyEvent.VK_S:
                particleManager.moveParticlesY(10);
                break;
            case KeyEvent.VK_A:
                particleManager.moveParticlesX(-10);
                break;
            case KeyEvent.VK_D:
                particleManager.moveParticlesX(10);
                break;

            // Particle settings
            case KeyEvent.VK_I:
                particleManager.changeGlow();
                break;
            case KeyEvent.VK_SPACE:
                particleManager.ChangeRules();
                break;

            // Preset attraction matrix
            case KeyEvent.VK_1:
                double[][] preset1 = { // total repulsion
                    {0.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                };
                particleManager.setAttraction(preset1);
                break;
            case KeyEvent.VK_2:
                double[][] preset2 = { // only attract own color/group
                    {1.0,  0.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  1.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  1.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   1.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    1.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.0,    1.0},
                };
                particleManager.setAttraction(preset2);
                break;
            case KeyEvent.VK_3:
                double[][] preset3 = { // rbg worm
                    {1.0,  -0.1,  0.0,   0.0,    0.0,    0.0},
                    {0.2,  1.0,  0.0,   0.0,    0.0,    0.0},
                    {0.0,  0.2,  1.0,   0.0,    0.0,    0.0},
                    {0.0,  0.0,  0.2,   1.0,    0.0,    0.0},
                    {0.0,  0.0,  0.0,   0.2,    1.0,    0.0},
                    {0.0,  0.0,  0.0,   0.0,    0.2,    1.0},
                };
                particleManager.setAttraction(preset3);
                break;
            case KeyEvent.VK_4:
                double[][] preset4 = { 
                    {-1.0,  1.0,  1.0,   1.0,    1.0,    1.0},
                    {1.0,  -1.0,  1.0,   1.0,    1.0,    1.0},
                    {1.0,  1.0,  -1.0,   1.0,    1.0,    1.0},
                    {1.0,  1.0,  1.0,   -1.0,    1.0,    1.0},
                    {1.0,  1.0,  1.0,   1.0,    -1.0,    1.0},
                    {1.0,  1.0,  1.0,   1.0,    1.0,    -1.0},
                };
                particleManager.setAttraction(preset4);
                break;

            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
