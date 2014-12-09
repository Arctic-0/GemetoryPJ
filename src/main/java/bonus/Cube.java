package bonus;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Created by whimsy on 12/9/14.
 */
public class Cube extends JFrame implements KeyListener{

    final static int HALF_WIDTH = 100;
    final static int WINDOW_WIDTH = 400;
    final static int DOT_WIDTH = 4;
    Point3D [] points = new Point3D[8];
    double alpha = 0.1;
    double beta = 0.1;
    double gamma = 0.1;


    int [][] facet = {
                         {0,1,2,3},  // A Red
                         {4,5,6,7},  // B Blue
                         {2,3,7,6},  // C Cyan
                         {0,1,5,4},  // D Green
                         {1,2,6,5},  // E Yellow
                         {0,4,7,3}   // F Orange
    };

    Color [] penColor = {
            Color.red, Color.blue, Color.cyan, Color.green, Color.yellow, Color.orange
    };

    JPanel p;

    int turnNumber = 18;

    public void init() {

        setTitle("Cube Rotate Demo");

        points[0] = new Point3D(-1, -1, 1);
        points[1] = new Point3D(1, -1, 1);
        points[2] = new Point3D(1, 1, 1);
        points[3] = new Point3D(-1, 1, 1);

        points[4] = new Point3D(-1, -1, -1);
        points[5] = new Point3D(1, -1, -1);
        points[6] = new Point3D(1, 1, -1);
        points[7] = new Point3D(-1, 1, -1);


        CanvasPanel tempPanel = new CanvasPanel();
        tempPanel.setHost(this);
        p = tempPanel;
        add(p, "Center");


        this.addKeyListener(this);
        this.setSize(WINDOW_WIDTH, WINDOW_WIDTH);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String [] args) {
       Cube frame = new Cube();
       frame.init();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W :
                alpha += Math.PI / turnNumber;
                break;
            case KeyEvent.VK_S :
                alpha -= Math.PI / turnNumber;
                break;
            case KeyEvent.VK_D :
                beta += Math.PI / turnNumber;
                break;
            case KeyEvent.VK_A :
                beta -= Math.PI / turnNumber;
                break;
            case KeyEvent.VK_E :
                gamma += Math.PI / turnNumber;
                break;
            case KeyEvent.VK_Q :
                gamma -= Math.PI / turnNumber;
                break;

            default:
//                repaint();
//               reRender();
//                p.repaint();
                break;
        }

        System.out.printf("%.2f %.2f %.2f\n", alpha, beta, gamma);
//        reRender();
        p.repaint();
//        paintComponents(this.getGraphics());
    }


    private void printMatrix(double [][] matrix) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                System.out.printf("%.2f ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }


    public void reRender(Graphics g) {


        // rotate along axis X
        double [][] matrixAlpha = new double[4][4];
        matrixAlpha[0][0] = 1;
        matrixAlpha[1][1] = Math.cos(alpha);
        matrixAlpha[1][2] = Math.sin(alpha);
        matrixAlpha[2][1] = -Math.sin(alpha);
        matrixAlpha[2][2] = Math.cos(alpha);
        matrixAlpha[3][3] = 1;


        // rotate along axis Y;
        double [][] matrixBeta = new double[4][4];
        matrixBeta[0][0] = Math.cos(beta);
        matrixBeta[0][2] = -Math.sin(beta);
        matrixBeta[1][1] = 1;
        matrixBeta[2][0] = Math.sin(beta);
        matrixBeta[2][2] = Math.cos(beta);
        matrixBeta[3][3] = 1;

        double [][] matrixGamma = new double[4][4];
        matrixGamma[0][0] = Math.cos(gamma);
        matrixGamma[0][1] = Math.sin(gamma);
        matrixGamma[1][0] = -Math.sin(gamma);
        matrixGamma[1][1] = Math.cos(gamma);
        matrixGamma[2][2] = 1;
        matrixGamma[3][3] = 1;




        g.setColor(Color.white);
        g.clearRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());


        Point3D [] points2 = new Point3D[8];
        for (int i = 0; i < 8; ++i) {
            double [] row = new double[4];
            row[0] = points[i].getX();
            row[1] = points[i].getY();
            row[2] = points[i].getZ();
            row[3] = 1;

            row = multiply(row, matrixAlpha);
            row = multiply(row, matrixBeta);
            row = multiply(row, matrixGamma);

            points2[i] = new Point3D(row[0], row[1], row[2]);

            drawDot(g, row[0], row[1]);


        }

        // draw lines between nodes

        g.setColor(Color.black);
        drawline(g, points2[0], points2[1]);
        drawline(g, points2[1], points2[2]);
        drawline(g, points2[2], points2[3]);
        drawline(g, points2[3], points2[0]);
        drawline(g, points2[4], points2[5]);
        drawline(g, points2[5], points2[6]);
        drawline(g, points2[6], points2[7]);
        drawline(g, points2[7], points2[4]);
        drawline(g, points2[0], points2[4]);
        drawline(g, points2[1], points2[5]);
        drawline(g, points2[2], points2[6]);
        drawline(g, points2[3], points2[7]);



        // fill in color
        double [] center = new double[6];

        for (int i = 0; i < 6; ++i) {
            center[i] = avg(points2, facet[i]);
        }

        int [] idx = new int[6];
        for (int i = 0; i < 6; ++i) {
            idx[i] = i;
        }

        for (int i = 0; i < 6; ++i) {
            for (int j = i+1; j < 6; ++j) {
                if (center[idx[i]] > center[idx[j]]) {
                    int t = idx[i];
                    idx[i] = idx[j];
                    idx[j] = t;
                }
            }
        }

        for (int i = 0; i < 6; ++i) {
            Polygon polygon = new Polygon();
            for (int j = 0; j < 4; ++j) {
                int nodeId = facet[idx[i]][j];
                polygon.addPoint(transform(points2[nodeId].getX()), transform(points2[nodeId].getY()));
            }

            g.setColor(penColor[idx[i]]);
            g.fillPolygon(polygon);
        }


    }

    private double avg(Point3D [] points, int [] nodeIdx) {
        double cul = 0;
        for (int i = 0; i < 4; ++i) {
            cul += points[nodeIdx[i]].getZ();
        }
        return cul / 4;
    }

    public int transform(double x) {
        return (int) (x * HALF_WIDTH) + WINDOW_WIDTH / 2;
    }

    private void drawline(Graphics g, Point3D p1, Point3D p2) {

        g.drawLine(transform(p1.getX()), transform(p1.getY()), transform(p2.getX()), transform(p2.getY()));
    }

    private void drawDot(Graphics g, double x, double y) {
        g.drawOval(transform(x) - DOT_WIDTH / 2,
                      transform(y) - DOT_WIDTH / 2,
                      DOT_WIDTH,
                      DOT_WIDTH);
    }

    private double [] multiply(double [] row, double[][] matrix) {
        double [] ret = new double[4];
        for (int i = 0; i < 4; ++i) {
            ret[i] = 0;
            for (int j = 0; j < 4; ++j) {
                ret[i] += row[j] * matrix[i][j];
            }
        }
        return ret;
    }
}
