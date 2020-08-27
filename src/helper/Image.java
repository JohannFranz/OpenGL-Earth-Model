package helper;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;

public class Image {

	private final int vaoId;
	private int textureID;
	
	public int getTextureID() {
		return textureID;
	}

	private ByteBuffer image;

	private final int w;
	private final int h;
	private final int comp;
	
	public Image(int vaoID, String path)
	{
		try {
			image = ioResourceToByteBuffer(path, 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		// Use info to read image metadata without decoding the entire image.
		// We don't need this for this demo, just testing the API.
		//if ( stbi_info_from_memory(image, w, h, comp) == 0 )
		if ( stbi_info_from_memory(image, w, h, comp) == false)
			throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

		System.out.println("Image width: " + w.get(0));
		System.out.println("Image height: " + h.get(0));
		System.out.println("Image components: " + comp.get(0));
		System.out.println("Image HDR: " + (stbi_is_hdr_from_memory(image) == true));

		// Decode the image
		image = stbi_load_from_memory(image, w, h, comp, 0);
		if ( image == null )
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

		this.w = w.get(0);
		this.h = h.get(0);
		this.comp = comp.get(0);
		
		vaoId = vaoID;
		
		// Create a new OpenGL texture 
    	textureID = glGenTextures();
    	// Bind the texture
    	glBindTexture(GL_TEXTURE_2D, textureID);
    	
    	if ( this.comp == 3 ) {
			if ( (this.w & 3) != 0 )
				glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (this.w & 1));
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.w, this.h, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.w, this.h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}
	
	private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}
	
	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource   the resource to read
	 * @param bufferSize the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException if an IO error occurs
	 */
	private ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		Path path = Paths.get(resource);
		if ( Files.isReadable(path) ) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				image = BufferUtils.createByteBuffer((int)fc.size() + 1);
				while ( fc.read(image) != -1 ) ;
			}
		} else {
			try (
				InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
				ReadableByteChannel rbc = Channels.newChannel(source)
			) {
				image = BufferUtils.createByteBuffer(bufferSize);

				while ( true ) {
					int bytes = rbc.read(image);
					if ( bytes == -1 )
						break;
					if ( image.remaining() == 0 )
						image = resizeBuffer(image, image.capacity() * 2);
				}
			}
		}

		image.flip();
		return image;
	}
}
