package helper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import helper.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	
	private final int vaoId;

	private int vboPositionId;
    private int vboColorId;
    private int vboNormalId;
    private int vboTangentId;
    private int vboIndexId;
    private int vboUVCoordId;

    private final int vertexCount;
    
    private float[] positions;
    private FloatBuffer posBuffer;

    private float[] colors;
    private FloatBuffer colorBuffer;

    private float[] tangents;
    private FloatBuffer tangentBuffer;

    private float[] normals;
    private FloatBuffer normalBuffer;
    
    private float[] uvCoords;
    private FloatBuffer uvCoordBuffer;
    
    private int[] indices;
    private IntBuffer indexBuffer;
    
    public Mesh(float[] positions, float[] colors, int[] indices, int vaoID) {
    	this.positions = positions;
    	this.colors = colors;
    	this.indices = indices;
    	
    	vertexCount = indices.length;

        //vaoId = glGenVertexArrays();
    	vaoId = vaoID;
    	glBindVertexArray(vaoId);

        initPositions();
        initColors();
        initIndices();
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        
    }
    
    public Mesh(float[] positions, float[] colors, float[] normals, float[] tangents, 
    			int[] indices, float[] uv, int vaoID) {
    	this.positions = positions;
    	this.colors = colors;
    	this.indices = indices;
    	this.uvCoords = uv;
    	this.normals = normals;
    	this.tangents = tangents;
    	
    	vertexCount = indices.length;

        //vaoId = glGenVertexArrays();
    	vaoId = vaoID;
    	glBindVertexArray(vaoId);

        initPositions();
        initColors();
        initIndices();
        initUVCoords();
        initNormals();
        initTangents();
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    private void initTangents() {
		// TODO Auto-generated method stub
    	// Position VBO
        vboTangentId = glGenBuffers();
        tangentBuffer = BufferUtils.createFloatBuffer(tangents.length);
        tangentBuffer.put(tangents).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboTangentId);
        glBufferData(GL_ARRAY_BUFFER, tangentBuffer, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
	}

	private void initNormals() {
		// TODO Auto-generated method stub
		// Position VBO
        vboNormalId = glGenBuffers();
        normalBuffer = BufferUtils.createFloatBuffer(normals.length);
        normalBuffer.put(normals).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalId);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
	}

	private void initPositions()
    {
    	// Position VBO
        vboPositionId = glGenBuffers();
        posBuffer = BufferUtils.createFloatBuffer(positions.length);
        posBuffer.put(positions).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboPositionId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }
    
    private void initColors()
    {
    	// Colour VBO
        vboColorId = glGenBuffers();
        colorBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorBuffer.put(colors).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboColorId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    }
    
    private void initIndices()
    {
    	// Index VBO
        vboIndexId = glGenBuffers();
        indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    }
    
    private void initUVCoords()
    {
    	// Colour VBO
    	vboUVCoordId = glGenBuffers();
        uvCoordBuffer = BufferUtils.createFloatBuffer(uvCoords.length);
        uvCoordBuffer.put(uvCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboUVCoordId);
        glBufferData(GL_ARRAY_BUFFER, uvCoordBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
    }
   
    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboPositionId);
        glDeleteBuffers(vboColorId);
        glDeleteBuffers(vboIndexId);
        glDeleteBuffers(vboUVCoordId);
        glDeleteBuffers(vboNormalId);
        glDeleteBuffers(vboTangentId);
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

	public int[] getVertexIndices() {
		return indices;
	}

	public float[] getVertexColors() {
		return colors;
	}

	public float[] getVertexPositions() {
		return positions;
	}

	public FloatBuffer getPosBuffer() {
		// TODO Auto-generated method stub
		return posBuffer;
	}

	public FloatBuffer getColorBuffer() {
		// TODO Auto-generated method stub
		return colorBuffer;
	}

	public void setVertexPosition(int i, float val) {
		// TODO Auto-generated method stub
		positions[i] = val;
	}

	public void setVertexColor(int i, float val) {
		// TODO Auto-generated method stub
		colors[i] = val;
	}

	public void update() {
		// TODO Auto-generated method stub
		glBindBuffer(GL_ARRAY_BUFFER, vboPositionId);
	    glBufferSubData(GL_ARRAY_BUFFER, 0, Utils.updateFlippedBuffer(posBuffer, positions));      //replace data in VBO with new data
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    
	    glBindBuffer(GL_ARRAY_BUFFER, vboColorId);
	    glBufferSubData(GL_ARRAY_BUFFER, 0, Utils.updateFlippedBuffer(colorBuffer, colors));      //replace data in VBO with new data
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void draw() {
		// TODO Auto-generated method stub

        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
        // Draw the vertices
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
        glBindVertexArray(0);
	}
}
