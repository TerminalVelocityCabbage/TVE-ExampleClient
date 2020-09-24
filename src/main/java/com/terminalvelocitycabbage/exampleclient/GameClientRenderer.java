package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.shader.ShaderHandler;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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
		//Create Shaders
		ShaderHandler defaultShaderHandler = new ShaderHandler(glCreateProgram());

		defaultShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.vert"));
		defaultShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.frag"));

		//Cleanup shaders and bind the program
		defaultShaderHandler.setupShaders();

		float[] rectVertices = new float[] {
				-0.8f, 	0.8f,	//0: top left
				0.8f, 	0.8f,	//1: top right
				-0.8f, 	-0.8f,	//2: btm left
				0.8f, 	-0.8f,	//3: btm right
		};
		float[] colors = new float[] {
				1, 0, 0, 1,
				0, 1, 0, 1,
				0, 0, 1, 1,
				1, 1, 1, 1
		};
		short[] indices = new short[] {
				0, 1, 2,	//left tri
				1, 2, 3		//right tri
		};

		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		//Create vertices buffer I guess
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(rectVertices.length);
		verticesBuffer.put(rectVertices).flip();

		int vboVertID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

		//Create Color buffer I guess
		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorsBuffer.put(colors).flip();

		int vboColID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColID);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

		ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indices.length);
		indicesBuffer.put(indices).flip();

		//Create element buffer I guess
		int eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		//Enable attribute locations
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindVertexArray(0);

		/*
		//Bind VAO
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		//Bind VBO
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		var triangle = new Primitives.Triangle(
				new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 0.5f, 0.0f),
				new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f));
		glBufferData(GL_ARRAY_BUFFER, triangle.getVertices(), GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 6, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, Float.BYTES * 6, Float.BYTES * 3);
		glEnableVertexAttribArray(1);
		 */

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
			//TODO add something real
			glBindVertexArray(vaoID);

			//Render the triangle
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);

			glBindVertexArray(0);

			//Send the frame
			glfwSwapBuffers(getWindow());
			glfwPollEvents();
		}

		//Clean up stuff (technically optional but good to know)
		glDeleteVertexArrays(0);
		glDeleteBuffers(0);
		defaultShaderHandler.delete();
	}
}
