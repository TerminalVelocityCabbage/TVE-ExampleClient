package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.shader.ShaderHandler;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

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
			glBindVertexArray(vao);
			glDrawArrays(GL_TRIANGLES, 0, 3);

			//Send the frame
			glfwSwapBuffers(getWindow());
			glfwPollEvents();
		}

		//Clean up stuff (technically optional but good to know)
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		defaultShaderHandler.delete();
	}
}
