package helper;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL20;

import math.Matrix4f;

public class ShaderProgram {
	private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        uniforms = new HashMap<>();
        
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
        System.out.println("Vertex-Shader created successfully");
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
        System.out.println("Fragment-Shader created successfully");
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Code: " + shaderId);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        //GL20.glGetProgramiv(programId, GL_LINK_STATUS, );
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
        	System.out.println(glGetShaderInfoLog(programId, 1024));
            throw new Exception("Error linking Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw new Exception("Error validating Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }
            glDeleteProgram(programId);
        }
    }

	public void createUniformParameters(String uniformName) throws Exception {
		// TODO Auto-generated method stub
		int uniformLocation = glGetUniformLocation(programId,uniformName);
	    if (uniformLocation < 0) {
	        throw new Exception("Could not find uniform:" + uniformName);
	    }
	    uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
	    // Dump the matrix into a float buffer
	    glUniformMatrix4fv(uniforms.get(uniformName), false, value.getBuffer());
	}

	public void setUniform(String uniformName, int value) {
		// TODO Auto-generated method stub
		glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value) {
		// TODO Auto-generated method stub
		glUniform1f(uniforms.get(uniformName), value);
	}
}
