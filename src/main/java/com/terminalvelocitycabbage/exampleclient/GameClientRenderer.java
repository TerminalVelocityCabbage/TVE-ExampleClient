package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.RendererBase;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.exampleclient.shapes.TexturedRectangle;
import com.terminalvelocitycabbage.exampleclient.shapes.TexturedVertex;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static com.terminalvelocitycabbage.exampleclient.shapes.TexturedRectangle.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends RendererBase {

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title, new GameInputHandler());
	}

	@Override
	public void loop() {
		Camera camera = new Camera((float)Math.toRadians(60.0f), 0.01f, 1000.0f, getWindow().width(), getWindow().height());

		//Create a rectangle with a texture
		TexturedRectangle rectangle = new TexturedRectangle(
				new TexturedVertex().setXYZ(-0.5f, 0.5f, 0f).setRGB(255, 0, 0).setUv(0, 0),
				new TexturedVertex().setXYZ(-0.5f, -0.5f, 0f).setRGB(0, 255, 0).setUv(0, 1),
				new TexturedVertex().setXYZ(0.5f, -0.5f, 0f).setRGB(0, 0, 255).setUv(1, 1),
				new TexturedVertex().setXYZ(0.5f, 0.5f, 0f).setRGB(255, 255, 255).setUv(1, 0),
				new Identifier(GameClient.ID, "textures/kyle.png")
		);
		rectangle.bind();

		//Create Shaders
		ShaderHandler defaultShaderHandler = new ShaderHandler();
		defaultShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.vert"));
		defaultShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.frag"));
		defaultShaderHandler.bindAll();

		//Create uniform for projectionMatrix
		defaultShaderHandler.createUniform("projectionMatrix");
		defaultShaderHandler.setUniformMat4f("projectionMatrix", camera.getProjectionMatrix());

		//For wireframe mode
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		// Run the rendering loop until the user has attempted to close the window
		while (!glfwWindowShouldClose(getWindowID())) {
			//Setup the frame for drawing
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Init shader
			defaultShaderHandler.use();

			//Make rectangle go further away every frame
			//Add the offsets to each vertex
			rectangle.getVertex(TOP_LEFT).addXYZW(0f, 0f, -0.005f, 0f);
			rectangle.getVertex(TOP_RIGHT).addXYZW(0f, 0f, -0.005f, 0f);
			rectangle.getVertex(BOTTOM_LEFT).addXYZW(0f, 0f, -0.005f, 0f);
			rectangle.getVertex(BOTTOM_RIGHT).addXYZW(0f, 0f, -0.005f, 0f);

			Log.info("" + rectangle.getVertex(0).getXYZW()[2]);

			//Push the changes to openGL
			rectangle.update();

			//Draw whatever changes were pushed
			rectangle.draw();

			//Send the frame
			push();
		}

		//Cleanup
		rectangle.destroy();
		defaultShaderHandler.delete();
	}
}
