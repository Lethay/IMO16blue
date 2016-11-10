package AgentGridMin;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Created by bravorr on 10/21/16.
 */
public class Visualizer extends JPanel{
    public final int xDim;
    public final int yDim;
    public final int scale;
    BufferedImage buff;
    int[] data;
    Graphics2D g;
    public Visualizer(int x, int y, int scaleFactor){
        this.setVisible(true);
        xDim=x;
        yDim=y;
        buff=new BufferedImage(xDim,yDim,BufferedImage.TYPE_INT_RGB);
        data=((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
        scale=scaleFactor;
        this.setPreferredSize(new Dimension((int)Math.ceil(xDim*scaleFactor), (int)Math.ceil(yDim*scaleFactor)));
        this.setMaximumSize(new Dimension((int)Math.ceil(xDim*scaleFactor), (int)Math.ceil(yDim*scaleFactor)));
        this.setMinimumSize(new Dimension((int)Math.ceil(xDim*scaleFactor), (int)Math.ceil(yDim*scaleFactor)));
    }
    public void SetBound(int x,int y,float r,float g,float b){
        r=Utils.BoundVal(r,0,1);
        g=Utils.BoundVal(g,0,1);
        b=Utils.BoundVal(b,0,1);
        Set(x,y,r,g,b);
    }
    public void SetHeat(int x,int y,double val){
        if(val>0) {
            float r = (float) Math.min(1, val * 3);
            float g = 0;
            float b = 0;
            if (val > 0.333) {
                g = (float) Math.min(1, (val - 0.333) * 3);
            }
            if (val > 0.666) {
                b = (float) Math.min(1, (val - 0.666) * 3);
            }
            Set(x, y, r, g, b);
        }
    }

    public void SetHeatCold(int x,int y,double val){
        if(val>0) {
            float b = (float) Math.min(1, val * 3);
            float g = 0;
            float r = 0;
            if (val > 0.333) {
                g = (float) Math.min(1, (val - 0.333) * 3);
            }
            if (val > 0.666) {
                r = (float) Math.min(1, (val - 0.666) * 3);
            }
            Set(x, y, r, g, b);
        }
    }

    public void Set(int x,int y,float r,float g,float b){
        Color c=new Color(r,g,b);
        //buff.setRGB(x,yDim-1-y,c.getRGB());
        data[(yDim-y-1)*xDim+x]=c.getRGB();
    }


    public void Clear(float r,float g,float b){
        Color c=new Color(r,g,b);
        Arrays.fill(data,c.getRGB());
    }
    @Override
    public void paint(Graphics g){
        ((Graphics2D)g).drawImage(buff.getScaledInstance(scale*xDim,scale*yDim,Image.SCALE_FAST),null,null);
        repaint();
    }
    //public void Draw(){
        //BufferStrategy bs=this.getBufferStrategy();
        //Graphics g=bs.getDrawGraphics();
        //g.drawImage(buff,0,0,(int)(xDim*scale),(int)(yDim*scale),null);
        //g.drawImage(buff,0,0,xDim,yDim,null);
        //g.dispose();
        //bs.show();
    //}
}
