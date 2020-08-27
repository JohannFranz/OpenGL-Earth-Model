package Kurven;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Kurven extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2153140687998421198L;
	private List<Point2D> kontrollPunkte;
	private Zeichenbereich zb;
	
	public Kurven() {
		kontrollPunkte = new ArrayList<Point2D>();
		zb = new Zeichenbereich(kontrollPunkte);
		add(zb);
		
		
		
		setTitle("Kurven");
		setSize(640, 480);
		setVisible(true);
	}

	
    public static void main(String[] args) {
        new Kurven();
    }

}

class Zeichenbereich extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3792158505074712830L;
	private List<Point2D> kontrollPunkte;
	private int radius;
	
	public Zeichenbereich(List<Point2D> kp) {
		kontrollPunkte = kp;
		radius = 6;
		
		addMouseListener(this);
	}
	
	@Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        zeichne(g);
    }
    
	private void zeichne(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

	    g2d.setPaint(Color.blue);

	        /*for (int i = 0; i < kontrollPunkte.size() - 1; i++) {
	            Point2D cur = kontrollPunkte.get(i);
	            Point2D next = kontrollPunkte.get(i+1);
	        	g2d.drawLine((int)cur.getX(), (int)cur.getY(), (int)next.getX(), (int)next.getY());
	        }*/
        for ( Point2D x : kontrollPunkte) {
        	g2d.drawArc((int)x.getX(), (int)x.getY(), radius, radius, 0, 360);
        }
        
        //zeichne HermiteSpline
        double[] px, py, psx, psy;
        px = new double[kontrollPunkte.size()+2];
        py = new double[kontrollPunkte.size()+2];
        
        double [] [] matrixA = new double [kontrollPunkte.size()] [kontrollPunkte.size()];
        double [] [] matrixB = new double [kontrollPunkte.size()] [kontrollPunkte.size()+2];
        
        for ( int i = 0; i < kontrollPunkte.size(); ++i ) {
        	Point2D p = kontrollPunkte.get(i);
        	px[i+1] = p.getX();
        	px[i+1] = p.getY();
        	if ( i > 0 && i < kontrollPunkte.size() ) {
        		matrixA[i][i-1] = 1d;
        		matrixA[i][i] = 4d;
        		matrixA[i][i+1] = 1d;
        		
        		matrixB[i][i] = -3d;
        		matrixB[i][i+2] = 3d;
        	}
        }
        matrixA[0][0] = 1d;
        matrixA[kontrollPunkte.size()-1][kontrollPunkte.size()-1] = 1d;
        
        matrixB[0][0] = 1d;
        matrixA[kontrollPunkte.size()-1][kontrollPunkte.size()+1] = 1d;
        
        //matrix a invertieren, mit matrix b multiplizieren, spline zeichnen
    }

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		Point2D p = new Point2D.Double(e.getX(), e.getY());
		kontrollPunkte.add(p);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}