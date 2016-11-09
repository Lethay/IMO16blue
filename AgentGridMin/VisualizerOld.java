package AgentGridMin;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.image.BufferedImage;

/**
 * Class used to initialize visualization window and Draw to it
 * Created by rafael on 9/22/16.
 */
public final class VisualizerOld extends Canvas{
    private Graphics g;
    /**
     * width of the display window, in pixels
     */
    public final int width;
    /**
     * height of the display window, in pixels
     */
    public final int height;

    public VisualizerOld(int pixW, int pixH) {
        width=pixW;
        height=pixH;

        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
    }
    public void PrepareDraw(){
        g=this.getBufferStrategy().getDrawGraphics();
        g.clearRect(0,0,width,height);
    }
    public void FinishDraw(){
        this.getBufferStrategy().show();
        g.dispose();
    }

    /**
     * sets the visualizer color for all subsequent Draw calls
     * @param red red component of color, bound between [0-1]
     * @param green green component of color, bound between [0-1]
     * @param blue blue component of color, bound between [0-1]
     */
    public void SetColor(double red,double green,double blue){
        g.setColor(new Color((float)Utils.BoundVal(red,0,1),(float)Utils.BoundVal(green,0,1),(float)Utils.BoundVal(blue,0,1)));
    }

    /**
     * draws a rectangle, arguments are all in pixels
     * @param x x component of top left corner of rectangle
     * @param y y component of top left corner of rectangle
     * @param w width of rectangle
     * @param h height of rectangle
     */
    public void DrawRect(int x,int y,int w,int h){
        g.fillRect(x,y,w,h);
    }

    /**
     * draws a rectangle on a discrete grid.
     * grid is scaled up to fit window size as closely as possible while remaining discrete
     * @param gridW width of the grid, used for scaling
     * @param gridH height of the grid, used for scaling
     * @param x x component of rectangle to be drawn on grid
     * @param y y component of rectangle to be drawn on gird
     */
    public void DrawDiscScaledRect(int x,int y,int gridW,int gridH){
        float conversionFactorX=width*1.0f/gridW;
        float conversionFactorY=height*1.0f/gridH;
        DrawRect((int)(x*conversionFactorX),(int)((gridH-1-y)*conversionFactorY),(int)conversionFactorX,(int)conversionFactorY);
    }
}
