package Cube;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import Listener.SliderColorListener;
import Listener.SliderCoordDepthListener;
import Listener.SliderCoordListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import helper.Mesh;
import helper.ShaderProgram;
import helper.Testat;
import math.Matrix4f;
 
public class Cube implements Testat{
	
	final int WIDTH = 860;
    final int HEIGHT = 640;
 
    // We need to strongly reference callback instances.
    private GLFWErrorCallback 	errorCallback;
    private GLFWKeyCallback   	keyCallback;
    
    private Matrix4f projection;
    
    private static final float FOV = 60.0f;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final int vaoId;
    
    private Mesh triangleMesh;
	private ShaderProgram shaderProgram;
        
    private boolean needsUpdate;
    
    // The window handle
    private long window;
    
    public Cube()
    {
    	System.out.println("Hello LWJGL " + Version.getVersion() + "!");
    	 
    	initWindow();
    	System.out.println(glGetString(GL_VERSION));
    	initProjectionMatrix();
        initMesh();
        initShader();
    	initSwingControls();
    	
    	vaoId = glGenVertexArrays();
    }

	public void run() {
        try {   
            loop();
 
            // Destroy window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            // Terminate GLFW and free the GLFWErrorCallback
            glfwTerminate();
            errorCallback.free();
        }
    }
    
    private void initProjectionMatrix() {
		// TODO Auto-generated method stub
	
    	float aspectRatio = (float) WIDTH / (float) HEIGHT;
    	projection = Matrix4f.perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
	}

	private void initSwingControls() {

		JPanel controlPane = new JPanel();
		initSlider(controlPane);

		JFrame sliderFrame = new JFrame("Control");
		sliderFrame.setLayout(new BorderLayout());
		sliderFrame.add(controlPane, BorderLayout.CENTER);
		sliderFrame.pack();
		sliderFrame.setVisible(true);
	}

    private void initSlider(JPanel controlPane) {
		// TODO Auto-generated method stub
		JPanel vertex, controller, coord, colors, color;
		controlPane.setLayout(new GridLayout(0,3));
		
		SliderCoordListener coordListener = new SliderCoordListener(this);
		SliderColorListener colListener = new SliderColorListener(this);
		SliderCoordDepthListener coordDepthListener = new SliderCoordDepthListener(this);
		
		for(int i = 0; i < 3; i++)
		{
			controller = new JPanel();
			controller.setBorder(BorderFactory.createTitledBorder("Koordinaten für Punkt " + i));
			controller.setLayout(new GridLayout(3,0));
			
			vertex = new JPanel();
			vertex.setLayout(new GridLayout(2,0));
			
			colors = new JPanel();
			colors.setBorder(BorderFactory.createTitledBorder("Farbe"));
			colors.setLayout(new GridLayout(3,0));
			
			for ( int j = 0; j < 3; j++)
			{
				String axis = "X", col = "Rot";
				if ( j == 1)
				{
					axis = "Y";
					col = "Grün";
				} else if ( j == 2 )
				{
					axis = "Z";
					col = "Blau";
				}
				coord = new JPanel();
				coord.setBorder(BorderFactory.createTitledBorder(axis));
				
				color = new JPanel();
				color.setBorder(BorderFactory.createTitledBorder(col));
				
				JSlider coordSlider = new JSlider(0,100,50);
				coordSlider.setName(i + " " + j);
				if ( j == 2 ) 
				{
					coordSlider.addChangeListener(coordDepthListener);
				} else 
				{
					coordSlider.addChangeListener(coordListener);
				}
				coord.add(coordSlider);
				controller.add(coord);
				
				JSlider colSlider = new JSlider(0,255,255);
				colSlider.setName(i + " " + j);
				colSlider.addChangeListener(colListener);
				color.add(colSlider);
				colors.add(color);
			}
			vertex.add(controller);
			vertex.add(colors);
			controlPane.add(vertex);
		}
	}
    
    private void initShader()
    {
    	try {
			shaderProgram = new ShaderProgram();
			shaderProgram.createVertexShader(
					"#version 330 \n"+
					"layout (location =0) in vec3 position;\n" + 
					"layout (location =1) in vec3 inColor;\n" +
					"out vec3 exCol;\n" +
					"uniform mat4 projectionMatrix;\n" +
					"void main() {\n" +
					"   gl_Position = projectionMatrix * vec4(position, 1.0);\n" + 
					"	exCol = inColor;}"					
					);
			shaderProgram.createFragmentShader(
					"#version 330 \n" + 
					"in vec3 exCol;\n" +
					"out vec4 fragColor;\n" + 
					"void main() {\n" +
					"   fragColor = vec4(exCol, 1.0);}\n" 
					);
			shaderProgram.link(); 
			
			shaderProgram.createUniformParameters("projectionMatrix");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void initMesh()
    {
    	float[] positions = new float[]{
                0.0f,  0.5f, -1.0f,
                -0.5f, -0.5f, -1.0f,
                0.5f,  -0.5f, -1.0f
            };
    	
    	float[] colors = new float[]{
                1.0f,  0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f,  0.0f, 1.0f
            };
    	
    	int[] indices = new int[]{
    			0, 1, 2
    		};
    	
    	triangleMesh = new Mesh(positions, colors, indices, vaoId);
    }

    private void initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
 
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
 
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Cube!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
            }
        });
 
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (vidmode.width() - WIDTH) / 2,
            (vidmode.height() - HEIGHT) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(window);
        
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
    }
 
    private void loop() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
 
        	if ( needsUpdate)
        	{
        		triangleMesh.update();
        		needsUpdate = false;
        	}
        	
    		shaderProgram.bind();
    		shaderProgram.setUniform("projectionMatrix", projection);
            triangleMesh.draw();
            shaderProgram.unbind();
            
            glfwSwapBuffers(window); // swap the color buffers
 
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    
    public static void main(String[] args) {
        new Cube().run();
    }

	public void updateCoords(String name, float val) {
		// TODO Auto-generated method stub
		
		String[] args = name.split(" ");
		Integer arg1 = Integer.parseInt(args[0]);
		Integer arg2 = Integer.parseInt(args[1]);
		triangleMesh.setVertexPosition(arg1*3 + arg2, val);
		
		needsUpdate = true;
	}

	public void updateColors(String name, float val) {
		// TODO Auto-generated method stub
		String[] args = name.split(" ");
		Integer arg1 = Integer.parseInt(args[0]);
		Integer arg2 = Integer.parseInt(args[1]);
		triangleMesh.setVertexColor(arg1*3 + arg2, val);
		
		needsUpdate = true;
	}

	@Override
	public void updateRotation(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScale(float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTranslation(String name, float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePerspective(String name, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBlendFunction(String name, int function) {
		// TODO Auto-generated method stub
		
	}
}
