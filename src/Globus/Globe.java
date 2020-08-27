package Globus;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glGetString;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import helper.Image;
import helper.ShaderProgram;
import helper.Utils;
import helper.Window;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;

public class Globe {
	
	private Window window;
	
	private Matrix4f projection;
    private Matrix4f view;
    private Matrix4f model;
    
    private HashMap<MatrixType, Matrix4f> map;
    
    private static final float FOV = 60.0f;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    
    private final int vaoId;
    
    private Image textureDecal;
    private Image textureDecalNight;
    private Image textureGloss;
    private Image textureNormal;

	private ShaderProgram shaderProgram;
    private GlobeMesh globeMesh;
    
    private boolean needsUpdate;

    private MatrixWindow modelWindow;
    private MatrixWindow viewWindow;
    private MatrixWindow perspectiveWindow;
    
    private float alphaVal;
    private int srcBlendFunction;
    private int dstBlendFunction;
    
    public Globe()
    {
    	System.out.println("Hello LWJGL " + Version.getVersion() + "!");
    	 
    	window = new Window();
    	
    	System.out.println(glGetString(GL_VERSION));
    	initViewMatrices();
        initShader();
        initTexture();
    	initMatrixWindows();
    	
    	new GlobeControls("Model-Control", MatrixType.ModelMatrix, this, 640, 150);
    	new GlobeControls("View-Control", MatrixType.ViewMatrix, this, 880, 150);
    	new GlobeControls("Perspective-Control", MatrixType.PerspectiveMatrix, this, 1100, 150);
    	
    	vaoId = glGenVertexArrays();
    	
    	globeMesh = new GlobeMesh(vaoId);
    }

	private void initMatrixWindows() {
		// TODO Auto-generated method stub
		modelWindow = new MatrixWindow("Model", model, 0, 0);
		viewWindow = new MatrixWindow("View", view, 180, 0);
		perspectiveWindow = new MatrixWindow("Projektion", projection, 360, 0);
	}

	public void run() {
        try {   
            loop();
 
            window.destroy();
        } finally {
        	window.terminateGLFW();            
        }
    }
    
    private void initViewMatrices() {
		// TODO Auto-generated method stub
    	model = new Matrix4f();
    	
    	Vector3f up = new Vector3f(0,1,0);
    	Vector3f lookAt = new Vector3f(0,0,1);
    	Vector3f xAxis =  up.cross(lookAt);
    	
    	view = new Matrix4f(new Vector4f(xAxis, 0), new Vector4f(up, 0), new Vector4f(lookAt, 0), new Vector4f(0,0,3,1));
    	//view = Matrix4f.inverse(view);    	
    	
    	float aspectRatio = (float) window.getWidth() / window.getHeight();
    	projection = Matrix4f.perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    	
    	map = new HashMap<MatrixType, Matrix4f>();
    	map.put(MatrixType.ModelMatrix, model);
    	map.put(MatrixType.ViewMatrix, view);
    	map.put(MatrixType.PerspectiveMatrix, projection);
	}

    private void initShader()
    {
    	try {
			shaderProgram = new ShaderProgram();
			shaderProgram.createVertexShader(Utils.loadResource("/Globus/globe_vertex.vs"));
			shaderProgram.createFragmentShader(Utils.loadResource("/Globus/globe_fragment.fs"));
			shaderProgram.link(); 
			
			shaderProgram.createUniformParameters("projectionMatrix");
			shaderProgram.createUniformParameters("modelMatrix");
			shaderProgram.createUniformParameters("viewMatrix");
			shaderProgram.createUniformParameters("alphaVal");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void initTexture() {
		// TODO Auto-generated method stub
    	
    	textureDecal = new Image(vaoId, "EarthDecal_Day.jpg");
    	textureNormal = new Image(vaoId, "Earth_NormalMap.jpg");
    	textureDecalNight = new Image(vaoId, "EarthDecal_Night.jpg");
        textureGloss = new Image(vaoId, "Earth_Gloss.jpg");
    	try {
			shaderProgram.createUniformParameters("worldTexture");
			shaderProgram.createUniformParameters("worldTextureNight");
			shaderProgram.createUniformParameters("glossTexture");
			shaderProgram.createUniformParameters("normalTexture");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
    private void loop() {
    	// Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
    	while ( !glfwWindowShouldClose(window.getWindowHandle()) ) {
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
 
        	if ( needsUpdate)
        	{
        		globeMesh.update();
        		needsUpdate = false;
        	}
        	
        	GL11.glEnable(GL_BLEND);
        	//GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        	GL11.glBlendFunc(srcBlendFunction, dstBlendFunction);
        	
    		shaderProgram.bind();
    		//Matrix4f proj = projection.multiply(camera);
    		shaderProgram.setUniform("modelMatrix", model);
    		shaderProgram.setUniform("projectionMatrix", projection);
    		shaderProgram.setUniform("viewMatrix", Matrix4f.inverse(view));
    		shaderProgram.setUniform("alphaVal", alphaVal);
    		
    		shaderProgram.setUniform("worldTexture", 0);
    		shaderProgram.setUniform("worldTextureNight", 1);
    		shaderProgram.setUniform("glossTexture", 2);
    		shaderProgram.setUniform("normalTexture", 3);
    		
    		glActiveTexture(GL_TEXTURE0);
    		glBindTexture(GL_TEXTURE_2D, textureDecal.getTextureID());
    		glActiveTexture(GL_TEXTURE1);
    		glBindTexture(GL_TEXTURE_2D, textureDecalNight.getTextureID());
    		glActiveTexture(GL_TEXTURE2);
    		glBindTexture(GL_TEXTURE_2D, textureGloss.getTextureID());
    		glActiveTexture(GL_TEXTURE3);
    		glBindTexture(GL_TEXTURE_2D, textureNormal.getTextureID());
    		globeMesh.draw();
            shaderProgram.unbind();
            //GL11.glDisable(0);
            glfwSwapBuffers(window.getWindowHandle()); // swap the color buffers
 
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    
    public static void main(String[] args) {
        new Globe().run();
    }

	public void rotate(float val, int axis, MatrixType matrixType) {
		// TODO Auto-generated method stub
		//model.setRotationMatrix(axis, val*3.14f/180);
		Matrix4f rot = Matrix4f.createRotationMatrix4f(val/(float)180*3.14f, axis);
		Matrix4f trans, invTrans;
		if ( matrixType == MatrixType.ModelMatrix ) {
			trans = Matrix4f.createTranslationMatrix(model);
			invTrans = Matrix4f.createInverseTranslationMatrix(model);	
			rot = trans.multiply(rot).multiply(invTrans);
			model = rot.multiply(model);
			modelWindow.update(model);
		} else if ( matrixType == MatrixType.ViewMatrix ) {
			trans = Matrix4f.createTranslationMatrix(view);
			invTrans = Matrix4f.createInverseTranslationMatrix(view);
			rot = trans.multiply(rot).multiply(invTrans);
			view = rot.multiply(view);
			viewWindow.update(view);
		} else return;
	}

	public void scale(float oldScaleValue, float newScaleValue, MatrixType matrixType)
	{
		float rescale = 1 / oldScaleValue;
		Matrix4f reScaleMatrix = Matrix4f.scale(rescale, rescale, rescale);
		Matrix4f newScaleMatrix = Matrix4f.scale(newScaleValue, newScaleValue, newScaleValue);
		
		if ( matrixType == MatrixType.ModelMatrix ) {
			Matrix4f trans = Matrix4f.createTranslationMatrix(model);
			Matrix4f invTrans = Matrix4f.createInverseTranslationMatrix(model);
			reScaleMatrix = trans.multiply(reScaleMatrix).multiply(invTrans);
			
			newScaleMatrix = trans.multiply(newScaleMatrix).multiply(invTrans);
			
			model = reScaleMatrix.multiply(model);
			model = newScaleMatrix.multiply(model);
			modelWindow.update(model);
		} else if ( matrixType == MatrixType.ViewMatrix ) {
			Matrix4f trans = Matrix4f.createTranslationMatrix(view);
			Matrix4f invTrans = Matrix4f.createInverseTranslationMatrix(view);
			reScaleMatrix = trans.multiply(reScaleMatrix).multiply(invTrans);
			
			newScaleMatrix = trans.multiply(newScaleMatrix).multiply(invTrans);
			
			view = reScaleMatrix.multiply(view);
			view = newScaleMatrix.multiply(view);
			viewWindow.update(view);
		} else return;
	}

	public void translate(float value, int axis, MatrixType matrixType) {
		// TODO Auto-generated method stub
		
		if ( matrixType == MatrixType.ModelMatrix ) {
			if ( axis == 0 ) {
				model.translateX(value);	
			} else if ( axis == 1 ) {
				model.translateY(value);	
			} else {
				model.translateZ(value);	
			}
			modelWindow.update(model);
		} else if ( matrixType == MatrixType.ViewMatrix ) {
			if ( axis == 0 ) {
				view.translateX(value);	
			} else if ( axis == 1 ) {
				view.translateY(value);	
			} else {
				view.translateZ(value);	
			}
			viewWindow.update(view);
		} else return;
	}

	public void changePerspective(float value, int axis) {
		// TODO Auto-generated method stub
		
		if ( axis == 0 ) {
			projection.setPerspectiveValueX(value);	
		} else if ( axis == 1 ) {
			projection.setPerspectiveValueY(value);
		} else {
			projection.setPerspectiveValuez(value);
		}
		perspectiveWindow.update(projection);
	}

	public void setAlphaVal(float value) {
		// TODO Auto-generated method stub
		alphaVal = value;
	}

	public void changeSrcBlendFunction(int function) {
		// TODO Auto-generated method stub
		srcBlendFunction = function;
	}

	public void changeDstBlendFunction(int function) {
		// TODO Auto-generated method stub
		dstBlendFunction = function;
	}
}

enum MatrixType {
	ModelMatrix,
	ViewMatrix,
	PerspectiveMatrix
};
