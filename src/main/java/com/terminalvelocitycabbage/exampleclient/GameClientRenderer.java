package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.shader.ShaderHandler;
import com.terminalvelocitycabbage.exampleclient.shapes.Rectangle;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.joml.Vector3f;
import org.joml.Vector4i;
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

		Rectangle rectangle = new Rectangle(
				new Vector3f(-0.8f, 	0.8f, 	0.0f),	//0: top left
				new Vector3f(0.8f, 	0.8f, 	0.0f),	//1: top right
				new Vector3f(-0.8f, 	-0.8f, 	0.0f),	//2: bottom left
				new Vector3f(0.8f, 	-0.8f, 	0.0f),	//3: bottom right
				new Vector4i(255, 0,   0,   255),		//0: top left
				new Vector4i(0,   255, 0,   255),		//1: top right
				new Vector4i(0,   0,   255, 255),		//2: bottom left
				new Vector4i(255, 255, 255, 255)	//3: bottom right
		);

		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		//Create vertices buffer I guess
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(rectangle.getVertices().length);
		verticesBuffer.put(rectangle.getVertices()).flip();

		int vboVertID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);

		//Create Color buffer I guess
		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(rectangle.getColors().length);
		colorsBuffer.put(rectangle.getColors()).flip();

		int vboColID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColID);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);

		ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(rectangle.getIndexes().length);
		indicesBuffer.put(rectangle.getIndexes()).flip();

		//Create element buffer I guess
		int eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, rectangle.getIndexes(), GL_STATIC_DRAW);

		//Reset to default VAO
		glBindVertexArray(0);

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

			//Render the triangle
			glBindVertexArray(vaoID);
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
