package simpleaoc;

import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;

public class ImagePanel extends JPanel {

    /*
     * extends the JPanel class to override the paintComponent method, allowing
     * for an image to be used for the background
     */

    private Image img;

    public ImagePanel(String img) {
        this.img=new ImageIcon(getClass().getResource(img)).getImage();
    }


    public void paintComponent(Graphics g) {
        Dimension d = getPreferredSize();
        g.drawImage(img, 0, 0, (int) d.getWidth(), (int) d.getHeight(), null);
    }
}
