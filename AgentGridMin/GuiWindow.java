package AgentGridMin;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bravorr on 10/19/16.
 */
public class GuiWindow extends JFrame {

    private JPanel panel;
    private GridBagConstraints gbc = new GridBagConstraints();
    /**
     * width of the display window, in pixels
     */
    public final int width;
    /**
     * height of the display window, in pixels
     */
    public final int height;

    public GuiWindow(String title, int pixW, int pixH, int gridW, int gridH) {
        width = pixW;
        height = pixH;
        this.setSize(width, height);
        this.setResizable(false);//fixes window size
        this.setVisible(true);
        this.setLocationRelativeTo(null);//puts window in middle of screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(title);
        panel = new JPanel();
        panel.setVisible(true);
        panel.setLayout(new GridBagLayout());
        this.add(panel);
    }
    public <T extends java.awt.Component> void AddComponent(T obj, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        panel.add(obj,gbc);
        obj.setVisible(true);
        if(obj instanceof VisualizerOld){
            ((VisualizerOld)obj).createBufferStrategy(3);
        }
        if(obj instanceof Visualizer){
            ((Visualizer)obj).g=(Graphics2D)(((Visualizer)obj).getGraphics());
        }
        this.pack();
    }
}

