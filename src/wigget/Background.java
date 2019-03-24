package wigget;

import java.awt.*;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author StarsOne
 * @date Create in  2019/3/22/0022 16:45
 * @description
 */
public class Background extends JPanel {
    private int width,height;
    public Background(int width,int height) {
        this.width = width;
        this.height = height;
        setSize(width,height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon imageIcon = new ImageIcon("." + File.separator + "res" + File.separator + "bg.jpg");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT ));
        imageIcon.paintIcon(this,g,0,0);
    }
}
