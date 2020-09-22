package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.renderutils.ShaderHandler;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class GameClientRenderer extends Renderer {

	@Override
	public void loop() {
		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		//Background Color stuff
		double frame = 0.0;
		float valueR, valueG, valueB;

		//The long awaited triangle shader stuff
		//Create the shader program
		int shaderProgram = glCreateProgram();

		//Create Shaders
		ShaderHandler.createShader(GL_VERTEX_SHADER, shaderProgram, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.vert"));
		ShaderHandler.createShader(GL_FRAGMENT_SHADER, shaderProgram, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.frag"));

		//link shaders
		glLinkProgram(shaderProgram);

		//Setup and configure vertex data
		var triangle = new Triangle(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 0.5f, 0.0f));
		int vbo = glGenBuffers();
		int vao = glGenVertexArrays();
		//Bind the VAO
		glBindVertexArray(vao);
		//Bind the VBO
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, triangle.getVertices(), GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);

		//For wireframe mode
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(Renderer.getWindow()) ) {
			//Setup the frame for drawing
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Actual Logic

			//Background randomised coloration
			valueR = (float)((Math.sin(frame/10) + 1)/2);
			valueG = (float)((Math.cos(frame/20) + 1)/2);
			valueB = (float)((Math.sin(frame/40) + 1)/2);
			glClearColor(valueR, valueG, valueB, 1.0f);
			if (frame == Float.MAX_VALUE) frame = 1.0;
			else frame++;

			//Triangle
			glUseProgram(shaderProgram);
			glBindVertexArray(vao);
			glDrawArrays(GL_TRIANGLES, 0, 3);

			//Send the frame
			glfwSwapBuffers(Renderer.getWindow());
			glfwPollEvents();
		}

		//Clean up stuff (technically optional but good to know)
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteProgram(shaderProgram);
	}
}
