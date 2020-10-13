package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.ColoredCuboid;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedCuboid;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.entity.GameObject;
import com.terminalvelocitycabbage.exampleclient.models.ColoredCuboidModel;
import com.terminalvelocitycabbage.exampleclient.models.RectangleModel;
import com.terminalvelocitycabbage.exampleclient.models.TexturedCuboidModel;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private ArrayList<GameObject> gameObjects = new ArrayList<>();

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title, new GameInputHandler());
	}

	@Override
	public void loop() {
		Camera camera = new Camera(60, 0.01f, 1000.0f);

		//Create a rectangle with a texture
		RectangleModel rectangleModel = new RectangleModel(new TexturedRectangle(
				new Vertex().setXYZ(-0.5f, 0.5f, 0f).setRGB(255, 0, 0).setUv(0, 0),
				new Vertex().setXYZ(-0.5f, -0.5f, 0f).setRGB(0, 255, 0).setUv(0, 1),
				new Vertex().setXYZ(0.5f, -0.5f, 0f).setRGB(0, 0, 255).setUv(1, 1),
				new Vertex().setXYZ(0.5f, 0.5f, 0f).setRGB(255, 255, 255).setUv(1, 0),
				ASSETS_ROOT_RESOURCE_MANAGER,
				new Identifier(GameClient.ID, "textures/kyle.png")
		));
		//gameObjects.add(GameObject.builder().setModel(rectangleModel).build());

		ColoredCuboidModel cuboidModel = new ColoredCuboidModel(new ColoredCuboid(
				new Vertex().setXYZ(-0.5f, 0.5f, 0f).setRGB(255, 0, 0),
				new Vertex().setXYZ(-0.5f, -0.5f, 0f).setRGB(0, 255, 0),
				new Vertex().setXYZ(0.5f, -0.5f, 0f).setRGB(0, 0, 255),
				new Vertex().setXYZ(0.5f, 0.5f, 0f).setRGB(255, 255, 255),
				new Vertex().setXYZ(-0.5f, 0.5f, -1f).setRGB(255, 0, 0),
				new Vertex().setXYZ(-0.5f, -0.5f, -1f).setRGB(0, 255, 0),
				new Vertex().setXYZ(0.5f, -0.5f, -1f).setRGB(0, 0, 255),
				new Vertex().setXYZ(0.5f, 0.5f, -1f).setRGB(255, 255, 255)
		));
		//gameObjects.add(GameObject.builder().setModel(cuboidModel, false).build());

		TexturedCuboidModel texturedCuboid = new TexturedCuboidModel(new TexturedCuboid(
				new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 0, 0).setUv(0, 0),
				new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(0, 255, 0).setUv(0, 1),
				new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(0, 0, 255).setUv(1, 1),
				new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(1, 0),

				new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(0, 0),
				new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(0, 0, 255).setUv(0, 1),
				new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 0, 0).setUv(1, 1),
				new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(0, 255, 0).setUv(1, 0),

				new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(0, 255, 0).setUv(0, 0),
				new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 0, 0).setUv(0, 1),
				new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(1, 1),
				new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(0, 0, 255).setUv(1, 0),

				new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(0, 0, 255).setUv(0, 0),
				new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(0, 1),
				new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(0, 255, 0).setUv(1, 1),
				new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 0, 0).setUv(1, 0),

				new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(0, 0, 255).setUv(0, 0),
				new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 0, 0).setUv(0, 1),
				new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(1, 1),
				new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(0, 255, 0).setUv(1, 0),

				new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(0, 0, 255).setUv(0, 0),
				new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 0, 0).setUv(0, 1),
				new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(1, 1),
				new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(0, 255, 0).setUv(1, 0),

				ASSETS_ROOT_RESOURCE_MANAGER,
				new Identifier(GameClient.ID, "textures/kyle.png")
		));
		gameObjects.add(GameObject.builder().setModel(texturedCuboid).build());

		//bind all Game Objects
		for (GameObject gameObject : gameObjects) {
			gameObject.bind();
		}

		//Create Shaders
		//Create default shader that is used for textured elements
		ShaderProgram defaultShaderHandler = new ShaderProgram();
		defaultShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/textured.vert"));
		defaultShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/textured.frag"));
		defaultShaderHandler.bindAll();

		//Create shader for elements without textures but containing color data in the vertex
		ShaderProgram coloredShaderHandler = new ShaderProgram();
		coloredShaderHandler.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/colored.vert"));
		coloredShaderHandler.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/colored.frag"));
		coloredShaderHandler.bindAll();

		//Init viewMatrix var
		Matrix4f viewMatrix;

		//Store InputHandler
		GameInputHandler inputHandler = (GameInputHandler) getWindow().getInputHandler();

		//Store camera increment vectors
		Vector3f moveVector = inputHandler.getCameraPositionMoveVector();
		Vector2f rotationVector = inputHandler.getDisplayVector();

		//For wireframe mode
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		// Run the rendering loop until the user has attempted to close the window
		while (!glfwWindowShouldClose(getWindow().getID())) {
			//Setup the frame for drawing
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Update the camera position
			camera.move(
					moveVector.x * 0.05f,
					moveVector.y * 0.05f,
					moveVector.z * 0.05f
			);
			camera.rotate(
					rotationVector.x * 0.4f,
					rotationVector.y * 0.4f,
					inputHandler.getCameraRollVector() * 1.5f
			);
			//This is a temp fix for the camera rotation sliding. I would like for this to happen automatically.
			inputHandler.resetDisplayVector();

			//Update the view Matrix with the current camera position
			//This has to happen before game items are updated
			viewMatrix = camera.getViewMatrix();

			//Draw whatever changes were pushed
			for (GameObject gameObject : gameObjects) {
				//Render the current object
				if (gameObject.isTextured()) {
					coloredShaderHandler.disable();
					defaultShaderHandler.enable();
				} else {
					defaultShaderHandler.disable();
					coloredShaderHandler.enable();
				}
				defaultShaderHandler.createUniform("projectionMatrix");
				defaultShaderHandler.createUniform("modelViewMatrix");
				gameObject.update();
				defaultShaderHandler.setUniformMat4f("modelViewMatrix", gameObject.getModelViewMatrix(viewMatrix));
				defaultShaderHandler.setUniformMat4f("projectionMatrix", camera.getProjectionMatrix());
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
