package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.shader.ShaderHandler;
import com.terminalvelocitycabbage.exampleclient.shapes.Rectangle;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.joml.Vector3f;
import org.joml.Vector4i;
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
		rectangle.bind();
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
			//Render the triangle
			rectangle.draw(vaoID);

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
