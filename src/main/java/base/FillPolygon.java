package base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * Created by whimsy on 12/8/14.
 *
 * 画多边形, 利用射线法判断是否在多边形内, 对像素进行染色
 */
public class FillPolygon extends JFrame implements MouseListener {
    JPanel p;



    final static int WIDTH = 600;
    final static int HEIGHT = 400;
    final static int DOT_SIZE = 2;

    boolean needClearFirst = false;

    List<Point> nodes = new ArrayList<Point>();

    public void init() {
        setTitle("FillPolygon Demo");



        p = new JPanel();

        add(p, "Center");
        p.setBackground(Color.white);
        p.addMouseListener(this);


        setSize(WIDTH,HEIGHT);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


    }

    public static void main(String [] args) {
        FillPolygon frame = new FillPolygon();
        frame.init();
    }


    public double cross(Point a, Point b, Point c) {
        return (double) (b.x - a.x) * (c.y - a.y) - (double) (b.y - a.y) * (c.x - a.x);
    }
    public int sign(double value) {
        if (value > 1e-8) return 1;
        if (value < -1e-8) return -1;
        return 0;

    }
    public boolean intersection(Point a, Point b, Point c, Point d) {
        if (sign(cross(a,b,c)) * sign(cross(a, b, d)) > 0) return false;
        if (sign(cross(c,d,a)) * sign(cross(c,d,b)) > 0)  return false;
        return true;
    }

    private void fill() {

        Point sp = new Point(9998, 9999);

        int n = nodes.size();

        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                int intersectionNumber = 0;
                for (int k = 0; k < nodes.size(); ++k) {
                    if (intersection(nodes.get(k), nodes.get((k + 1) % n), new Point(i,j), sp)) {
                        ++intersectionNumber;
                    }
                }

                if (intersectionNumber % 2 == 1) {
                    Graphics g = p.getGraphics();
                    g.drawLine(i, j, i, j);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (needClearFirst) {
            clearScreen();
        }


        nodes.add(new Point(e.getX(), e.getY()));
        Graphics g = p.getGraphics();
        int n = nodes.size();
        g.drawOval((int) nodes.get(n - 1).getX() - DOT_SIZE/2,
                      (int) nodes.get(n - 1).getY() - DOT_SIZE/2 ,
                      DOT_SIZE,
                      DOT_SIZE);

        if (n > 1) {
            g.drawLine((int) nodes.get(n-2).getX(),
                          (int) nodes.get(n-2).getY(),
                          (int) nodes.get(n-1).getX(),
                          (int) nodes.get(n-1).getY());
        }

        if (e.getClickCount() == 2) {
            g.drawLine((int) nodes.get(0).getX(),
                          (int) nodes.get(0).getY(),
                          (int) nodes.get(n-1).getX(),
                          (int) nodes.get(n-1).getY());


            needClearFirst = true;
            fill();

        }


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    private void clearScreen() {
        Graphics g = p.getGraphics();

        g.setColor(Color.white);
        g.clearRect((int) p.getX(), p.getY(), p.getWidth(), p.getHeight());
        nodes = new ArrayList<Point>();
        needClearFirst = false;
        repaint();
    }


}
