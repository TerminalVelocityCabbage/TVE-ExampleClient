package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.Resource;
import com.terminalvelocitycabbage.engine.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.util.PNGDecoder;
import com.terminalvelocitycabbage.exampleclient.shapes.TexturedVertex;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Optional;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static com.terminalvelocitycabbage.exampleclient.shapes.TexturedVertex.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class GameClientRenderer extends Renderer {

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title);
	}

	@Override
	public void loop() {
		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();

		//The long awaited triangle shader stuff

		// We'll define our quad using 4 vertices of the custom TexturedVertex class
		TexturedVertex v0 = new TexturedVertex().setXYZ(-0.5f, 0.5f, 0).setRGB(1, 0, 0).setUv(0, 0);
		TexturedVertex v1 = new TexturedVertex().setXYZ(-0.5f, -0.5f, 0).setRGB(0, 1, 0).setUv(0, 1);
		TexturedVertex v2 = new TexturedVertex().setXYZ(0.5f, -0.5f, 0).setRGB(0, 0, 1).setUv(1, 1);
		TexturedVertex v3 = new TexturedVertex().setXYZ(0.5f, 0.5f, 0).setRGB(1, 1, 1).setUv(1, 0);

		TexturedVertex[] vertices = new TexturedVertex[] {v0, v1, v2, v3};
		// Put each 'Vertex' in one FloatBuffer
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * TexturedVertex.ELEMENT_COUNT);
		for (int i = 0; i < vertices.length; i++) {
			// Add position, color and texture floats to the buffer
			verticesBuffer.put(vertices[i].getElements());
		}
		verticesBuffer.flip();
		// OpenGL expects to draw vertices in counter clockwise order by default
		byte[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		int indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		int vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		int vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		// Put the position coordinates in attribute list 0
		glVertexAttribPointer(0, POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, POSITION_OFFSET);
		// Put the color components in attribute list 1
		glVertexAttribPointer(1, COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, COLOR_OFFSET);
		// Put the texture coordinates in attribute list 2
		glVertexAttribPointer(2, TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, TEXTURE_OFFSET);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		int eboId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


		//Create Shaders
		ShaderHandler defaultShaderHandler = new ShaderHandler();

		defaultShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.vert"));
		defaultShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.frag"));

		//Cleanup shaders and bind the program
		defaultShaderHandler.setupShaders();

		//Load Textures
		int kyleID = loadPNGTexture(new Identifier(GameClient.ID, "textures/kyle.png"), GL_TEXTURE0);

		//For wireframe mode
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		// Run the rendering loop until the user has attempted to close the window
		while (!glfwWindowShouldClose(getWindow())) {
			//Setup the frame for drawing
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Init shader
			defaultShaderHandler.use();

			//Actual Logic
			//Render the triangle
			//rectangle.draw(vaoID);

			// Bind the texture
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, 0);

			// Bind to the VAO that has all the information about the vertices
			glBindVertexArray(vaoId);
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			glEnableVertexAttribArray(2);

			// Bind to the index VBO/EBO that has all the information about the order of the vertices
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);

			// Draw the vertices
			glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_BYTE, 0);

			// Put everything back to default (deselect)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glDisableVertexAttribArray(2);
			glBindVertexArray(0);

			glUseProgram(0);

			//Send the frame
			glfwSwapBuffers(getWindow());
			glfwPollEvents();
		}

		//Clean up stuff (technically optional but good to know)
		glDeleteVertexArrays(0);
		glDeleteBuffers(0);
		defaultShaderHandler.delete();
	}

	private int loadPNGTexture(Identifier identifier, int textureUnit) {
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		try {
			// Open the PNG file as an InputStream
			InputStream in = null;
			Optional<Resource> file = ASSETS_ROOT_RESOURCE_MANAGER.getResource(identifier);
			if (file.isPresent()) {
				in = file.get().openStream();
			}
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();

			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(Float.BYTES * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * Float.BYTES, PNGDecoder.Format.RGBA);
			buf.flip();

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = glGenTextures();
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, tWidth, tHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);

		// Setup the UV/ST coordinate system
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		// Setup what to do when the texture has to be scaled
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

		return texId;
	}
}
