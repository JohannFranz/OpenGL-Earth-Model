package Globus;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math.Matrix4f;


public class MatrixWindow {

	JLabel m00, m01, m02, m03;
	JLabel m10, m11, m12, m13;
	JLabel m20, m21, m22, m23;
	JLabel m30, m31, m32, m33;
	
	//Matrix4f matrix;
	
	JPanel controlPane;
	
	public MatrixWindow(String name, Matrix4f matrix, int xPos, int yPos)
	{
		//this.matrix = matrix;
		controlPane = new JPanel();
		m00 = new JLabel();
		m10 = new JLabel();
		m20 = new JLabel();
		m30 = new JLabel();
		
		m01 = new JLabel();
		m11 = new JLabel();
		m21 = new JLabel();
		m31 = new JLabel();
		
		m02 = new JLabel();
		m12 = new JLabel();
		m22 = new JLabel();
		m32 = new JLabel();
		
		m03 = new JLabel();
		m13 = new JLabel();
		m23 = new JLabel();
		m33 = new JLabel();
		
		initMatrixValues(matrix);
		
		JFrame frame = new JFrame(name);
		frame.setLayout(new BorderLayout());
		frame.add(controlPane, BorderLayout.CENTER);
		frame.setSize(180, 150);
		frame.setLocation(xPos, yPos);
		frame.setVisible(true);
	}

	private void initMatrixValues(Matrix4f matrix) {
		// TODO Auto-generated method stub
		controlPane.setLayout(new GridLayout(4,4));
		update(matrix);
		
		controlPane.add(m00);
		controlPane.add(m10);
		controlPane.add(m20);
		controlPane.add(m30);
		
		controlPane.add(m01);
		controlPane.add(m11);
		controlPane.add(m21);
		controlPane.add(m31);
		
		controlPane.add(m02);
		controlPane.add(m12);
		controlPane.add(m22);
		controlPane.add(m32);
		
		controlPane.add(m03);
		controlPane.add(m13);
		controlPane.add(m23);
		controlPane.add(m33);
	}

	public void update(Matrix4f matrix) {
		// TODO Auto-generated method stub
		//this.matrix = matrix;
		float[] matrixArray = matrix.getArray();
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		m00.setText(df.format(matrixArray[0]));
		m10.setText(df.format(matrixArray[1]));
		m20.setText(df.format(matrixArray[2]));
		m30.setText(df.format(matrixArray[3]));
		
		m01.setText(df.format(matrixArray[4]));
		m11.setText(df.format(matrixArray[5]));
		m21.setText(df.format(matrixArray[6]));
		m31.setText(df.format(matrixArray[7]));
		
		m02.setText(df.format(matrixArray[8]));
		m12.setText(df.format(matrixArray[9]));
		m22.setText(df.format(matrixArray[10]));
		m32.setText(df.format(matrixArray[11]));
		
		m03.setText(df.format(matrixArray[12]));
		m13.setText(df.format(matrixArray[13]));
		m23.setText(df.format(matrixArray[14]));
		m33.setText(df.format(matrixArray[15]));
	}
}
