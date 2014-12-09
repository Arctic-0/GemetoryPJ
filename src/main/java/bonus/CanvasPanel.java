package bonus;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Created by whimsy on 12/9/14.
 */
public class CanvasPanel extends JPanel {
    private Cube host;



    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        host.reRender(g);
    }

    public Cube getHost() {
        return host;
    }

    public void setHost(Cube host) {
        this.host = host;
    }
}
