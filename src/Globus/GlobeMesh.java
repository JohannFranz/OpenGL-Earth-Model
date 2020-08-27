package Globus;

import helper.Mesh;
import math.Matrix3f;
import math.Vector3f;

public class GlobeMesh {
	
    private static final int LEVEL_COUNT = 50;
    private static final int COUNT_VERTICES_PER_LEVEL = 100;
    private static final float GLOBE_RADIUS = 0.5f;
    
    private Mesh mesh;

	public GlobeMesh(int vaoId)
	{
		float[] positions = new float[LEVEL_COUNT*COUNT_VERTICES_PER_LEVEL*3];
    	float[] uv = new float[LEVEL_COUNT*COUNT_VERTICES_PER_LEVEL*2];
    	int[] indices = new int[LEVEL_COUNT*COUNT_VERTICES_PER_LEVEL*6];
    	float[] colors = new float[LEVEL_COUNT*COUNT_VERTICES_PER_LEVEL*3];
    	float[] normals = new float[LEVEL_COUNT*COUNT_VERTICES_PER_LEVEL*3];
    	float[] tangents = new float[LEVEL_COUNT*COUNT_VERTICES_PER_LEVEL*3];
    	
    	Vector3f temp = new Vector3f(0,1,0);
    	Vector3f tangent = new Vector3f(1,0,0);
    	temp = temp.scale(GLOBE_RADIUS);
    	
    	float angleY = (float) (2 * Math.PI / (COUNT_VERTICES_PER_LEVEL-1));
    	float angleZ = (float) (Math.PI / (float)(LEVEL_COUNT-1));
    	//float angleY = 2 * 3.14f / (COUNT_VERTICES_PER_LEVEL-1);
    	//float angleZ = 3.14f / (float)(LEVEL_COUNT-1);
    	Matrix3f rotZ, rotY;
    	rotZ = Matrix3f.createRotationMatrix3f(angleZ, 2);
    	rotY = Matrix3f.createRotationMatrix3f(angleY, 1);
    	
    	for ( int i = 0; i < LEVEL_COUNT; i++ )
    	{
    		for ( int j = 0; j < COUNT_VERTICES_PER_LEVEL; j++ )
    		{
    			positions[i*3*COUNT_VERTICES_PER_LEVEL + j*3] = temp.x;
    			positions[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 1] = temp.y;
    			positions[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 2] = temp.z;
    			
    			normals[i*3*COUNT_VERTICES_PER_LEVEL + j*3] = temp.x;
    			normals[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 1] = temp.y;
    			normals[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 2] = temp.z;

    			tangents[i*3*COUNT_VERTICES_PER_LEVEL + j*3] = tangent.x;
    			tangents[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 1] = tangent.y;
    			tangents[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 2] = tangent.z;
    			
    			uv[i*2*COUNT_VERTICES_PER_LEVEL + j*2] = (float)j / (COUNT_VERTICES_PER_LEVEL-1);
    			uv[i*2*COUNT_VERTICES_PER_LEVEL + j*2 + 1] = (float)i / (LEVEL_COUNT-1);
    			
    			colors[i*3*COUNT_VERTICES_PER_LEVEL + j*3] = 1.0f;
    			colors[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 1] = 1.0f;
    			colors[i*3*COUNT_VERTICES_PER_LEVEL + j*3 + 2] = 1.0f;
    			
    			if ( j == COUNT_VERTICES_PER_LEVEL-1 ) continue;
    			
    			temp = rotY.multiply(temp);
    			tangent = rotY.multiply(tangent);
    		}
    		temp = rotZ.multiply(temp);
    		tangent = rotZ.multiply(tangent);
    	}
    	
    	for ( int i = 0; i < LEVEL_COUNT-1; i++ )
    	{
    		for ( int j = 0; j < COUNT_VERTICES_PER_LEVEL; j++ )
    		{
    			indices[i*6*COUNT_VERTICES_PER_LEVEL + j*6] = i*COUNT_VERTICES_PER_LEVEL + j;
    			indices[i*6*COUNT_VERTICES_PER_LEVEL + j*6 + 1] = (i+1)*COUNT_VERTICES_PER_LEVEL + j;
    			indices[i*6*COUNT_VERTICES_PER_LEVEL + j*6 + 2] = (i+1)*COUNT_VERTICES_PER_LEVEL + j + 1;
    			indices[i*6*COUNT_VERTICES_PER_LEVEL + j*6 + 3] = i*COUNT_VERTICES_PER_LEVEL + j;
    			indices[i*6*COUNT_VERTICES_PER_LEVEL + j*6 + 4] = (i+1)*COUNT_VERTICES_PER_LEVEL + j + 1;
    			indices[i*6*COUNT_VERTICES_PER_LEVEL + j*6 + 5] = i*COUNT_VERTICES_PER_LEVEL + j + 1;
    		}
    	}
    	
    	mesh = new Mesh(positions, colors, normals, tangents, indices, uv, vaoId);
	}

	public void update() 
	{
		// TODO Auto-generated method stub
		mesh.update();
	}
	
	public void draw()
	{
		mesh.draw();
	}
}
