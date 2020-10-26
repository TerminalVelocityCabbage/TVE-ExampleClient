package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.components.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.entity.ModeledGameObject;
import com.terminalvelocitycabbage.exampleclient.models.DCModel;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private ArrayList<ModeledGameObject> gameObjects = new ArrayList<>();

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title, new GameInputHandler());
	}

	@Override
	public void loop() {
		//Create the controllable camera
		Camera camera = new Camera(60, 0.01f, 1000.0f);

		//Load a model to a Model object from dcm file
		DCModel model = DCModel.Loader.load(ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "model/Gerald.dcm"), new Identifier(GameClient.ID, "textures/gerald_base.png"));
		//Create a game object from the model loaded
		ModeledGameObject object = ModeledGameObject.builder().setModel(model).build();
		//Expose the head model part from the model so it can be animated in the game loop
		Model.Part head = model.getPart("head").orElseThrow();
		//Add the game object to the list of active objects
		gameObjects.add(object);

		//bind all Game Objects
		for (ModeledGameObject gameObject : gameObjects) {
			gameObject.bind();
		}

		//Create Shaders
		//Create default shader that is used for textured elements
		ShaderProgram defaultShaderProgram = new ShaderProgram();
		defaultShaderProgram.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/default.vert"));
		defaultShaderProgram.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/default.frag"));
		defaultShaderProgram.build();

		//Create shader program for debugging normals directions
		ShaderProgram normalShaderProgram = new ShaderProgram();
		normalShaderProgram.queueShader(GL_VERTEX_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/default.vert"));
		normalShaderProgram.queueShader(GL_FRAGMENT_SHADER, ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(GameClient.ID, "shaders/normalonly.frag"));
		normalShaderProgram.build();

		//Init viewMatrix var
		Matrix4f viewMatrix;

		//Store InputHandler
		GameInputHandler inputHandler = (GameInputHandler) getWindow().getInputHandler();

		//Store camera increment vectors
		Vector3f moveVector = inputHandler.getCameraPositionMoveVector();
		Vector2f rotationVector = inputHandler.getDisplayVector();

		//Create a point light
		Attenuation attenuation = new Attenuation(0.0f, 0.0f, 1.0f);
		PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), new Vector3f(0,0,1), 1.0f, attenuation);

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
			if (!inputHandler.isRightButtonPressed()) inputHandler.resetDisplayVector();
			camera.rotate(
					rotationVector.x * 0.4f,
					rotationVector.y * 0.4f,
					inputHandler.getCameraRollVector() * 1.5f
			);

			//Animate the head
			head.rotation.add(0, 1, 0);
			//Tell the engine that the game object needs to be re-rendered
			object.queueUpdate();

			//This is a temp fix for the camera rotation sliding. I would like for this to happen automatically.
			inputHandler.resetDisplayVector();

			//Update the view Matrix with the current camera position
			//This has to happen before game items are updated
			viewMatrix = camera.getViewMatrix();

			//renderNormalsDebug(camera, viewMatrix, normalShaderProgram);
			renderDefault(camera, viewMatrix, defaultShaderProgram, pointLight, head.getMaterial());

			//Send the frame
			push();
		}

		//Cleanup
		for (ModeledGameObject gameObject : gameObjects) {
			gameObject.destroy();
		}
		defaultShaderProgram.delete();
	}

	private void renderNormalsDebug(Camera camera, Matrix4f viewMatrix, ShaderProgram shaderProgram) {
		//Point light
		shaderProgram.enable();

		//Render the current object
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : gameObjects) {
			gameObject.update();
			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(viewMatrix));
			gameObject.render();
		}
	}

	private void renderDefault(Camera camera, Matrix4f viewMatrix, ShaderProgram shaderProgram, PointLight pointLight, Material material) {
		//Reserve memory for light position
		Vector4f newPos;

		//Point light
		shaderProgram.enable();
		shaderProgram.createPointLightUniform("pointLight");
		//Translate the point light to view coordinates
		newPos = new Vector4f(pointLight.getPosition(), 0.0f);

		//Render the current object
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		//Mesh materials this should probably be handled by some sort background system
		shaderProgram.createMaterialUniform("material");
		//Lighting stuff
		shaderProgram.createUniform("specularPower");
		shaderProgram.createUniform("ambientLight");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : gameObjects) {

			gameObject.update();

			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			//The color of light all objects will receive without a light present
			shaderProgram.setUniform("ambientLight", new Vector3f(0.3f, 0.3f, 0.3f));
			//How intense the reflected light is
			shaderProgram.setUniform("specularPower", 10.0f);
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(viewMatrix));
			shaderProgram.setUniform("pointLight", new PointLight(pointLight, newPos.mul(viewMatrix)));
			//Material stuff
			//TODO stop rendering models recursively without passing a material from each mesh
			shaderProgram.setUniform("material", material);

			gameObject.render();
		}
	}
}
