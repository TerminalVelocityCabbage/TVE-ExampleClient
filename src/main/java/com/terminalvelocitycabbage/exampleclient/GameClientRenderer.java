package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.shader.ShaderHandler;
import com.terminalvelocitycabbage.exampleclient.shapes.TexturedRectangle;
import com.terminalvelocitycabbage.exampleclient.shapes.TexturedVertex;
import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.lwjgl.opengl.GL;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static com.terminalvelocitycabbage.exampleclient.shapes.TexturedRectangle.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title);
	}

	@Override
	public void loop() {
		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();

		//Create a rectangle with a texture
		TexturedRectangle rectangle = new TexturedRectangle(
				new TexturedVertex().setXYZ(-0.5f, 0.5f, 0).setRGB(255, 0, 0).setUv(0, 0),
				new TexturedVertex().setXYZ(-0.5f, -0.5f, 0).setRGB(0, 255, 0).setUv(0, 1),
				new TexturedVertex().setXYZ(0.5f, -0.5f, 0).setRGB(0, 0, 255).setUv(1, 1),
				new TexturedVertex().setXYZ(0.5f, 0.5f, 0).setRGB(255, 255, 255).setUv(1, 0),
				new Identifier(GameClient.ID, "textures/kyle.png")
		);
		rectangle.bind();

		//Create Shaders
		ShaderHandler defaultShaderHandler = new ShaderHandler();
		defaultShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.vert"));
		defaultShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.frag"));
		defaultShaderHandler.setupShaders();

		//For wireframe mode
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		// Run the rendering loop until the user has attempted to close the window
		while (!glfwWindowShouldClose(getWindow())) {
			//Setup the frame for drawing
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Init shader
			defaultShaderHandler.use();

			//Randomize rectangle positions
			// Define offset
			float offsetX = (float) (Math.cos(Math.PI * glfwGetTime()) * 0.1);
			float offsetY = (float) (Math.sin(Math.PI * glfwGetTime()) * 0.1);
			//Add the offsets to each vertex
			rectangle.getVertex(TOP_LEFT).addXYZW(offsetX, offsetY, 0f, 0f);
			rectangle.getVertex(TOP_RIGHT).addXYZW(-offsetX, -offsetY, 0f, 0f);
			rectangle.getVertex(BOTTOM_LEFT).addXYZW(offsetX, offsetY, 0f, 0f);
			rectangle.getVertex(BOTTOM_RIGHT).addXYZW(-offsetX, -offsetY, 0f, 0f);

			//Push the changes to openGL
			rectangle.update();

			//Reset the positions so it doesnt run away BUT dont push them to openGL, just the rectangle object
			rectangle.getVertex(TOP_LEFT).addXYZW(-offsetX, -offsetY, 0f, 0f);
			rectangle.getVertex(TOP_RIGHT).addXYZW(offsetX, offsetY, 0f, 0f);
			rectangle.getVertex(BOTTOM_LEFT).addXYZW(-offsetX, -offsetY, 0f, 0f);
			rectangle.getVertex(BOTTOM_RIGHT).addXYZW(offsetX, offsetY, 0f, 0f);

			//Draw whatever changes were pushed
			rectangle.draw();

			//Send the frame
			glfwSwapBuffers(getWindow());
			glfwPollEvents();
		}

		//Cleanup
		rectangle.destroy();
		defaultShaderHandler.delete();
	}
}
