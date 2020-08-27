/**
 * 
 */
package Kurven;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * Einfuehrung in die Computergrafik
 *@author F. N. Rudolph (c) 2012
 *10.05.2016
 */
public class ZeichenFlaeche extends JPanel implements MouseMotionListener,
		MouseListener {
//	private int x=50, y =50;
	private int radius = 10;
	            //T0   P0      P1      T1
	private int x0=50, x1=275, x2=650, x3=900;
	private int y0=500, y1=400, y2=350, y3=600;
	
	private double[][] A = {
		{2,1,0,0},
		{1,4,1,0},
		{0,1,4,1},
		{0,0,1,2}
	};
	
	private double[][] B = {
		{-3,  3,  0,  0},
		{-3,  0,  3,  0},
		{ 0, -3,  0,  3},
		{ 0,  0, -3,  3}
	};

	/**
	 * 
	 */
	public ZeichenFlaeche() {
		super();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int r2 =radius/2;
		
		g.fillOval(x1-r2, y1-r2, radius, radius);
		g.fillOval(x2-r2, y2-r2, radius, radius);
		g.drawLine(x1, y1, x2, y2);
		//g.setColor(Color.RED);
		g.fillOval(x0-r2, y0-r2, radius, radius);
		g.fillOval(x3-r2, y3-r2, radius, radius);
		g.drawLine(x1, y1, x0, y0);
		g.drawLine(x3, y3, x2, y2);
		
		g.setColor(Color.GREEN);
		
		double[][] Ai = Matrix.invertiereMatrix(A);
		double[][] AiB = Matrix.matMult(Ai, B);
		double[][] P = {
			{x0,y0},
			{x1,y1},
			{x2,y2},
			{x3,y3},
		};
		double[][] T = Matrix.matMult(AiB, P); //T = P'
		
				//P0		    P1          T0        T1  
		double xStart, yStart, xEnd, yEnd, t0x, t0y, t1x, t1y;
		
		for (int i = 0; i < P.length - 1; i++) {
			//Anfangspunkt der Kurve
			xStart = P[i][0];
			yStart = P[i][1];
			//Endpunkt der Kurve
			xEnd = P[i+1][0];
			yEnd = P[i+1][1];
			//Anfangstangente
			t0x  = T[i][0];
			t0y  = T[i][1];
			//Endtangente
			t1x  = T[i+1][0];
			t1y  = T[i+1][1];
			
			double xAlt = xStart;
			double yAlt = yStart;
			double xNeu, yNeu;
			double delta = 0.0625d;
			for (double t = delta; t < 1d; t += delta){
				double[] h = blendFuncHermite(t);
				xNeu =
					//h1*p0   h2*p1     h3*t0           h4*t1
					h[0]*xStart + h[1]*xEnd + h[2]*t0x + h[3]*t1x;
				yNeu =
					h[0]*yStart + h[1]*yEnd + h[2]*t0y + h[3]*t1y;
				
				g.drawLine((int)xAlt, (int)yAlt, (int)xNeu, (int)yNeu);
				xAlt = xNeu;
				yAlt = yNeu;
			}
			g.drawLine((int)xAlt, (int)yAlt, (int)xEnd, (int)yEnd);
		}
	}
	
	private double[] blendFuncHermite(double t){
		double t2 = t * t;
		double t3 = t2 * t;
		return new double[]{
			2 * t3 - 3 * t2 + 1, // H1(t)
			-2*t3 + 3*t2,        // H2(t)
			t3 - 2*t2 + t,       // H3(t)
			t3 - t2              // H4(t)			
		};
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x3 = e.getX();
		y3 = e.getY();
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("xy: " + e.getX() + ", " + e.getY());
		
	}
	
}
