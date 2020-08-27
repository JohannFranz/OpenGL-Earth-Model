package Globus;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.lwjgl.opengl.GL11;

import Listener.BlendFunctionListener;
import Listener.SliderColorListener;
import Listener.SliderPerspectiveListener;
import Listener.SliderRotationListener;
import Listener.SliderScaleListener;
import Listener.SliderTranslationListener;
import helper.Testat;

public class GlobeControls implements Testat{
	
	private Globe globe;
	JPanel controlPane;
	MatrixType matrixType;
	
	private JComboBox<BlendFunction> srcFunctions;
	private JComboBox<BlendFunction> dstFunctions;
	
	private int rotationXValue = 0;
	private int rotationYValue = 0;
	private int rotationZValue = 0;
	private float scaleValue = 1;

	public GlobeControls(String name, MatrixType mType, Globe globe, int xPos, int yPos)
	{
		this.globe = globe;
		matrixType = mType;

		controlPane = new JPanel();
		controlPane.setLayout(new GridLayout(7,0));
		
		if ( mType == MatrixType.PerspectiveMatrix ) {
			initPerspectiveControls();
		} else {
			initSwingControls();	
		}
		
		JFrame sliderFrame = new JFrame(name);
		sliderFrame.setLayout(new BorderLayout());
		sliderFrame.add(controlPane, BorderLayout.CENTER);
		sliderFrame.pack();
		sliderFrame.setLocation(xPos, yPos);
		sliderFrame.setVisible(true);
	}

	private void initPerspectiveControls() {
		// TODO Auto-generated method stub
		initPerspectiveSlider();
		initAlphaBlending();
	}

	private void initAlphaBlending() {
		// TODO Auto-generated method stub
		BlendFunction[] functions = getBlendFunctions();
		
		JPanel alphaPanel = new JPanel();
		alphaPanel.setBorder(BorderFactory.createTitledBorder("Alpha-Blending_Source"));
		
		srcFunctions = new JComboBox<BlendFunction>(functions);
		srcFunctions.setName("src");
		
		BlendFunctionListener blendFunctionListener = new BlendFunctionListener(this);
		srcFunctions.addActionListener(blendFunctionListener);
		
		alphaPanel.add(srcFunctions);
		controlPane.add(alphaPanel);
		
		alphaPanel = new JPanel();
		alphaPanel.setBorder(BorderFactory.createTitledBorder("Alpha-Blending_Destination"));
		
		dstFunctions = new JComboBox<BlendFunction>(functions);
		dstFunctions.setName("dst");
		
		blendFunctionListener = new BlendFunctionListener(this);
		dstFunctions.addActionListener(blendFunctionListener);
		
		alphaPanel.add(dstFunctions);
		controlPane.add(alphaPanel);
	}

	private BlendFunction[] getBlendFunctions() {
		// TODO Auto-generated method stub
		BlendFunction[] functions = new BlendFunction[9];
		
		functions[0] = new BlendFunction(GL11.GL_ZERO, "GL_ZERO");
		functions[1] = new BlendFunction(GL11.GL_ONE, "GL_ONE");
		functions[2] = new BlendFunction(GL11.GL_DST_COLOR, "GL_DST_COLOR");
		functions[3] = new BlendFunction(GL11.GL_ONE_MINUS_DST_COLOR, "GL_ONE_MINUS_DST_COLOR");
		functions[4] = new BlendFunction(GL11.GL_SRC_ALPHA, "GL_SRC_ALPHA");
		functions[5] = new BlendFunction(GL11.GL_ONE_MINUS_SRC_ALPHA, "GL_ONE_MINUS_SRC_ALPHA");
		functions[6] = new BlendFunction(GL11.GL_DST_ALPHA, "GL_DST_ALPHA");
		functions[7] = new BlendFunction(GL11.GL_ONE_MINUS_DST_ALPHA, "GL_ONE_MINUS_DST_ALPHA");
		functions[8] = new BlendFunction(GL11.GL_SRC_ALPHA_SATURATE, "GL_SRC_ALPHA_SATURATE");
		
		return functions;
	}

	private void initPerspectiveSlider() {
		// TODO Auto-generated method stub
		JPanel perspectivePanel, alphaPanel;
		
		SliderPerspectiveListener perspectiveListener = new SliderPerspectiveListener(this);
		SliderColorListener alphaValListener = new SliderColorListener(this);
		
		for ( int j = 0; j < 3; j++)
		{
			String axis = "X_Perspective";
			if ( j == 1)
			{
				axis = "Y_Perspective";
			} else if ( j == 2 )
			{
				axis = "Z_Perspective";
			}
			perspectivePanel = new JPanel();
			perspectivePanel.setBorder(BorderFactory.createTitledBorder(axis));
			
			JSlider perspectiveSlider = new JSlider(0,200,0);
			perspectiveSlider.setName("" + j);
			perspectiveSlider.addChangeListener(perspectiveListener);
			
			perspectivePanel.add(perspectiveSlider);
			controlPane.add(perspectivePanel);
		}
		
		alphaPanel = new JPanel();
		alphaPanel.setBorder(BorderFactory.createTitledBorder("Alpha-Wert"));
		
		JSlider alphaSlider = new JSlider(0,255,255);
		alphaSlider.addChangeListener(alphaValListener);
		
		alphaPanel.add(alphaSlider);
		controlPane.add(alphaPanel);
	}

	private void initSwingControls() {
		// TODO Auto-generated method stub
		initSlider(controlPane);
	}
	
	private void initSlider(JPanel controlPane) {
		// sTODO Auto-generated method stub
		
		
		initRotationSlider();
		initScaleSlider();
		initTranslationSlider();
	}
	 
	private void initTranslationSlider() {
		// TODO Auto-generated method stub
		JPanel translationPanel;
		
		SliderTranslationListener translationListener = new SliderTranslationListener(this);
		
		for ( int j = 0; j < 3; j++)
		{
			String axis = "X_Translation";
			if ( j == 1)
			{
				axis = "Y_Translation";
			} else if ( j == 2 )
			{
				axis = "Z_Translation";
			}
			translationPanel = new JPanel();
			translationPanel.setBorder(BorderFactory.createTitledBorder(axis));
			
			JSlider translationSlider = new JSlider(0,200,0);
			translationSlider.setName("" + j);
			translationSlider.addChangeListener(translationListener);
			
			translationPanel.add(translationSlider);
			controlPane.add(translationPanel);
		}
	}

	private void initScaleSlider() {
		// TODO Auto-generated method stub

		JPanel scalePanel = new JPanel();
		scalePanel.setBorder(BorderFactory.createTitledBorder("Skalierung"));
		
		SliderScaleListener scaleListener = new SliderScaleListener(this);
		
		JSlider scaleSlider = new JSlider(1,200,100);
		scaleSlider.addChangeListener(scaleListener);
		
		scalePanel.add(scaleSlider);
		controlPane.add(scalePanel);
	}

	private void initRotationSlider() {
		// TODO Auto-generated method stub
		JPanel rotationPanel;
		
		
		SliderRotationListener rotationListener = new SliderRotationListener(this);
		
		for ( int j = 0; j < 3; j++)
		{
			String axis = "X_Rotation";
			if ( j == 1)
			{
				axis = "Y_Rotation";
			} else if ( j == 2 )
			{
				axis = "Z_Rotation";
			}
			rotationPanel = new JPanel();
			rotationPanel.setBorder(BorderFactory.createTitledBorder(axis));
			
			JSlider rotationSlider = new JSlider(0,360,0);
			rotationSlider.setName("" + j);
			rotationSlider.addChangeListener(rotationListener);
			
			rotationPanel.add(rotationSlider);
			controlPane.add(rotationPanel);
		}
	}

	public void updateCoords(String name, float val) {

	}

	public void updateColors(String name, float val) {
		globe.setAlphaVal(val);
	}

	@Override
	public void updateRotation(String name, int value) {
		// TODO Auto-generated method stub
		Integer axis = Integer.parseInt(name);
		if ( axis == 0 ) {
			globe.rotate(value - rotationXValue, 0, matrixType);
			rotationXValue = value;
		} else if ( axis == 1 ) {
			globe.rotate(value - rotationYValue, 1, matrixType);
			rotationYValue = value;
		} else {
			globe.rotate(value - rotationZValue, 2, matrixType);
			rotationZValue = value;
		}
	}

	@Override
	public void updateScale(float newScaleValue) {
		// TODO Auto-generated method stub
		globe.scale(scaleValue, newScaleValue, matrixType);
		scaleValue = newScaleValue;
	}

	@Override
	public void updateTranslation(String name, float value) {
		// TODO Auto-generated method stub
		Integer axis = Integer.parseInt(name);
		if ( axis == 0 ) {
			globe.translate(value, 0, matrixType);
		} else if ( axis == 1 ) {
			globe.translate(value, 1, matrixType);
		} else {
			globe.translate(value, 2, matrixType);
		}
	}

	@Override
	public void updatePerspective(String name, float value) {
		// TODO Auto-generated method stub
		Integer axis = Integer.parseInt(name);
		if ( axis == 0 ) {
			globe.changePerspective(value, 0);
		} else if ( axis == 1 ) {
			globe.changePerspective(value, 1);
		} else {
			globe.changePerspective(value, 2);
		}
	}

	@Override
	public void updateBlendFunction(String name, int function) {
		// TODO Auto-generated method stub
		if ( name == "src" ) {
			globe.changeSrcBlendFunction(function);
		} else if ( name == "dst" ) {
			globe.changeDstBlendFunction(function);
		}
	}
}
