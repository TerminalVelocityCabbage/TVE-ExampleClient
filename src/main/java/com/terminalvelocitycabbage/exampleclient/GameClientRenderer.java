package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.RendererBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedVertex;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.entity.GameObject;

import java.util.ArrayList;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends RendererBase {

	private ArrayList<GameObject> gameObjects = new ArrayList<>();

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title, new GameInputHandler());
	}

	@Override
	public void loop() {
		Camera camera = new Camera((float)Math.toRadians(60.0f), 0.01f, 1000.0f, getWindow().width(), getWindow().height());

		//Create a rectangle with a texture
		RectangleModel rectangleModel = new RectangleModel(new TexturedRectangle(
				new TexturedVertex().setXYZ(-0.5f, 0.5f, 0f).setRGB(255, 0, 0).setUv(0, 0),
				new TexturedVertex().setXYZ(-0.5f, -0.5f, 0f).setRGB(0, 255, 0).setUv(0, 1),
				new TexturedVertex().setXYZ(0.5f, -0.5f, 0f).setRGB(0, 0, 255).setUv(1, 1),
				new TexturedVertex().setXYZ(0.5f, 0.5f, 0f).setRGB(255, 255, 255).setUv(1, 0),
				ASSETS_ROOT_RESOURCE_MANAGER,
				new Identifier(GameClient.ID, "textures/kyle.png")
		));
		gameObjects.add(GameObject.builder().setModel(rectangleModel).build());

		//bind all Game Objects
		for (GameObject gameObject : gameObjects) {
			gameObject.bind();
		}

		//Create Shaders
		ShaderProgram defaultShaderHandler = new ShaderProgram();
		defaultShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.vert"));
		defaultShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/tri.frag"));
		defaultShaderHandler.bindAll();

		//Init shader
		defaultShaderHandler.use();

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

			//Make rectangle go further away every frame
			//Add the offsets to each vertex
			gameObjects.get(0).move(0f, 0f, -0.005f);

			//Draw whatever changes were pushed
			//render all Game Objects
			for (GameObject gameObject : gameObjects) {
				gameObject.render();
			}

			//Send the frame
			push();
		}

		//Cleanup
		for (GameObject gameObject : gameObjects) {
			gameObject.destroy();
		}
		defaultShaderHandler.delete();
	}
}
