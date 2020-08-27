/**
 * 
 */
package Spline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Einfuehrung in die Computergrafik
 *@author F. N. Rudolph (c) 2012
 *10.05.2016
 */

public class ZeichenFlaeche extends JPanel implements MouseMotionListener,
		MouseListener {
//	private int x=50, y =50;
	private int radius = 10;
	private boolean istGeschlossen = false;
	
//	private int x0=50, x1=275, x2=650, x3=900;
//	private int y0=500, y1=400, y2=350, y3=600;
	
	private double[][] P = new double[][]{
			{50, 500},
			{275, 400},
			{650, 350},
			{900, 300},
			{1000, 300},
			{1000, 500}
	};
	
//	private double[][] A = {
//		{2,1,0,0},
//		{1,4,1,0},
//		{0,1,4,1},
//		{0,0,1,2}
//	};
//	
//	private double[][] B = {
//		{-3,  3,  0,  0},
//		{-3,  0,  3,  0},
//		{ 0, -3,  0,  3},
//		{ 0,  0, -3,  3}
//	};

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
		
		// draw all the points
		for (int i=0; i<P.length; i++){
			g.fillOval(
					(int)P[i][0] - r2,
					(int)P[i][1] - r2,
					radius, radius);
		}
		
		g.setColor(Color.GREEN);
		
		double[][] A, B;
		int n = P.length;
		A = new double[n][n];
		B = new double[n][n];
		
		if (!istGeschlossen){
			// Erste Zeile, natürlicher Anfang
			A[0][0] =  2;
			A[0][1] =  1;
			B[0][0] = -3;
			B[0][1] =  3;
		}
		else{
			// Erste Zeile, geschlossener Spline (Mittelpunktformel)
			A[0][0] = 4;
			A[0][1] = 1;
			A[0][n-1] = 1;
			
			B[0][1] = 3;
			B[0][n-1] = -3;
		}
		
		// Mittelpunkte (Mittelpunktformel)
		for (int i = 1; i < n-1; i++) {
			A[i][i-1] = 1;
			A[i][i] = 4; //Hauptdiagonale immer 4!
			A[i][i+1] = 1;
			
			B[i][i-1] = -3;
			B[i][i+1] = 3;
		}
		
		// Letzte Zeile
		if (!istGeschlossen){
			// natürliches Ende
			A[n-1][n-2] = 1;
			A[n-1][n-1] = 2;
			B[n-1][n-2] = -3;
			B[n-1][n-1] = 3;
		}
		else{
			// geschlossener Spline
			A[n-1][0] = 1;
			A[n-1][n-2] = 1;
			A[n-1][n-1] = 4;
			
			B[n-1][0] = 3;
			B[n-1][n-2] = -3;
		}
		
//		Matrix.print("A",A);
//		Matrix.print("B",B);
		
		double[][] Ai = Matrix.invertiereMatrix(A);
		double[][] AiB = Matrix.matMult(Ai, B);
//		double[][] P = {
//			{x0,y0},
//			{x1,y1},
//			{x2,y2},
//			{x3,y3},
//		};
		double[][] T = Matrix.matMult(AiB, P); //T = P'
		
				//P0		    P1          T0        T1  
		double xStart, yStart, xEnd, yEnd, t0x, t0y, t1x, t1y;
		
		for (int i = 0; i < (istGeschlossen ? n : n - 1); i++) {
			//Anfangspunkt der Kurve
			xStart = P[i][0];
			yStart = P[i][1];
			//Endpunkt der Kurve
			xEnd = P[(i+1) % n][0];
			yEnd = P[(i+1) % n][1];
			//Anfangstangente
			t0x  = T[i][0];
			t0y  = T[i][1];
			//Endtangente
			t1x  = T[(i+1) % n][0];
			t1y  = T[(i+1) % n][1];
			
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
		
		// draw Bezier
		drawBezier(g);
	}
	
//	
	
	private double[] lerp(double[] start, double[] end, double t){
		return new double[]{
				(end[0] - start[0])*t + start[0],
				(end[1] - start[1])*t + start[1]
		};
	}
	
	private void drawBezier(Graphics g){
		g.setColor(Color.red);
		for (double t=0.0625; t<1; t+=0.0625){
			
			// Tiefe Kopie des Punktarrays
			double[][] points = new double[P.length][2];
			for (int i=0; i<points.length; i++){
				points[i][0] = P[i][0];
				points[i][1] = P[i][1];
			}
			
			// De-Casteljau Algorithmus
			for (int i=points.length-1; i>=1; i--){
				for (int j=0; j<i; j++){
					points[j] = lerp(points[j], points[j+1], t);
				}
			}
			
			g.fillOval(
					(int)points[0][0] - (radius/2),
					(int)points[0][1] - (radius/2),
					radius, radius);
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
		int index = P.length - 1;
		P[index][0] = e.getX();
		P[index][1] = e.getY();
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("xy: " + e.getX() + ", " + e.getY());
		
	}

}
