/**
 * 
 */
package Kurven;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Einfuehrung in die Computergrafik
 *@author F. N. Rudolph (c) 2012
 *10.05.2016
 */
public class Uebung4 {
	JFrame jf;
	ZeichenFlaeche zf;

	/**
	 * 
	 */
	public Uebung4() {
		jf = new JFrame("Hermitekurve");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(1500, 900);
		jf.setLocation(50,50);
		
		zf = new ZeichenFlaeche();
		jf.add(zf,BorderLayout.CENTER);
		
		jf.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Uebung4();

	}

}
