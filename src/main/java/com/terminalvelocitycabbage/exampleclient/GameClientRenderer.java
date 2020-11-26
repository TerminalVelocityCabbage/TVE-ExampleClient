package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.TextGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.client.renderer.ui.elements.UICanvas;
import com.terminalvelocitycabbage.engine.client.renderer.ui.constraints.CenterConstraint;
import com.terminalvelocitycabbage.engine.client.renderer.ui.constraints.SizeConstraint;
import com.terminalvelocitycabbage.engine.client.renderer.ui.elements.BoxElement;
import com.terminalvelocitycabbage.engine.client.renderer.util.GameObjectHandler;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.exampleclient.models.DCModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.FRAGMENT;
import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.VERTEX;
import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private GameObjectHandler gameObjectHandler = new GameObjectHandler();
	private final ShaderHandler shaderHandler = new ShaderHandler();
	private GameInputHandler inputHandler = new GameInputHandler();

	private GameClientHud hud;

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title, new GameInputHandler());
	}

	@Override
	public void init() {
		super.init();
		hud = new GameClientHud(getWindow(), "This is some text rendered dynamically!");


		hud.create("boxTest", new UICanvas()
				.addChild(new BoxElement(new Vector3f(1, 1, 1))
						.addConstraint(new SizeConstraint(SizeConstraint.Type.WIDTH, 10, SizeConstraint.Unit.PERCENT))
						.addConstraint(new SizeConstraint(SizeConstraint.Type.HEIGHT, 10, SizeConstraint.Unit.PERCENT))
						.addConstraint(new CenterConstraint(CenterConstraint.Direction.BOTH)))
		);

		hud.show("boxTest");


		//Create the controllable camera
		camera = new Camera(60, 0.01f, 1000.0f);

		//Load a model to a Model object from dcm file
		DCModel robotModel = DCModel.load(MODEL, new Identifier(GameClient.ID, "Gerald.dcm"));
		robotModel.setMaterial(Material.builder()
				.reflectivity(new Texture(TEXTURE, new Identifier(GameClient.ID, "gerald_reflectivity.png")))
				.texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "gerald_base.png")))
				.build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		gameObjectHandler.add("robot", ModeledGameObject.builder().setModel(robotModel).build());

		//Do it again so we have two objects with different models and different textures
		DCModel wormModel = DCModel.load(MODEL, new Identifier(GameClient.ID, "Worm.dcm"));
		wormModel.setMaterial(Material.builder()
				.texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "worm.png")))
				.build());
		gameObjectHandler.add("worm", ModeledGameObject.builder().setModel(wormModel).build());
		gameObjectHandler.getObject("worm").move(0, 0, 10);

		//bind all Game Objects
		for (ModeledGameObject gameObject : gameObjectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.bind();
		}
		for (TextGameObject text : hud.getTextGameObjects()) {
			text.bind();
		}

		//Create Shaders
		//Create default shader that is used for textured elements
		shaderHandler.newProgram("default");
		shaderHandler.queueShader("default", VERTEX, SHADER, new Identifier(GameClient.ID, "default.vert"));
		shaderHandler.queueShader("default", FRAGMENT, SHADER, new Identifier(GameClient.ID, "default.frag"));
		shaderHandler.build("default");

		//Create shader program for debugging normals directions
		shaderHandler.newProgram("normals");
		shaderHandler.queueShader("normals", VERTEX, SHADER, new Identifier(GameClient.ID, "default.vert"));
		shaderHandler.queueShader("normals", FRAGMENT, SHADER, new Identifier(GameClient.ID, "normalonly.frag"));
		shaderHandler.build("normals");

		//Create a shader program for hud rendering
		shaderHandler.newProgram("hud");
		shaderHandler.queueShader("hud", VERTEX, SHADER, new Identifier(GameClient.ID, "hud_default.vert"));
		shaderHandler.queueShader("hud", FRAGMENT, SHADER, new Identifier(GameClient.ID, "hud_default.frag"));
		shaderHandler.build("hud");

		//Store InputHandler
		inputHandler = (GameInputHandler) getWindow().getInputHandler();

		//Create some light
		Attenuation plAttenuation = new Attenuation(0.0f, 0.0f, 1.0f);
		gameObjectHandler.add("blueLight", new PointLight(new Vector3f(0, 2, -0.5f), new Vector3f(0,0,1), 1.0f, plAttenuation));
		gameObjectHandler.add("whiteLight", new PointLight(new Vector3f(0, 4, -0.5f), new Vector3f(1,1,1), 1.0f, plAttenuation));
		Attenuation slAttenuation = new Attenuation(0.0f, 0.0f, 0.02f);
		gameObjectHandler.add("redSpotLight", new SpotLight(new Vector3f(0, 2, 0), new Vector3f(1, 0, 0), 1.0f, slAttenuation, new Vector3f(0, 1, 0), 140));
		gameObjectHandler.add("sun", new DirectionalLight(new Vector3f(-1f, 0f, 0f), new Vector4f(1, 1, 0.5f, 1), 1.0f));

		//For wireframe mode
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	@Override
	public void loop() {
		super.loop();
		//Setup the frame for drawing
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//Update the camera position
		camera.move(inputHandler.getCameraPositionMoveVector(), 0.05f);
		//Only allow looking around when right click is held
		if (!inputHandler.isRightButtonPressed()) inputHandler.resetDisplayVector();
		//Update camera rotation
		camera.rotate(inputHandler.getDisplayVector().mul(0.4f), inputHandler.getCameraRollVector() * 0.05f);

		//Move around the point lights
		gameObjectHandler.getObject("blueLight").move(0, (float)Math.sin(glfwGetTime())/10, 0);
		gameObjectHandler.getObject("whiteLight").move(0, (float)Math.cos(glfwGetTime())/8, 0);

		//Animate the head
		ModeledGameObject robot = gameObjectHandler.getObject("robot");
		((DCModel)robot.getModel()).getPart("head").orElseThrow().rotation.add(0, 1, 0);
		//Tell the engine that the game object needs to be re-rendered
		robot.queueUpdate();

		//Update the view Matrix with the current camera position
		//This has to happen before game items are updated
		viewMatrix.identity().set(camera.getViewMatrix());

		//renderNormalsDebug(camera, viewMatrix, normalShaderProgram);
		renderDefault(camera, viewMatrix, shaderHandler.get("default"));
		hud.setText(0, "FPS: " + this.getFramerate());
		hud.getTextGameObjects().forEach(EmptyGameObject::queueUpdate);
		renderHud(shaderHandler.get("hud"));

		//Send the frame
		push();
	}

	@Override
	public void destroy() {
		super.destroy();
		//Cleanup
		for (ModeledGameObject gameObject : gameObjectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.destroy();
		}
		for (TextGameObject text : hud.getTextGameObjects()) {
			text.destroy();
		}
		shaderHandler.cleanup();
		hud.cleanup();
	}

	private void renderNormalsDebug(Camera camera, Matrix4f viewMatrix, ShaderProgram shaderProgram) {
		//Point light
		shaderProgram.enable();

		//Render the current object
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("normalTransformationMatrix");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : gameObjectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.update();
			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(viewMatrix));
			shaderProgram.setUniform("normalTransformationMatrix", gameObject.getTransformationMatrix());
			gameObject.render();
		}
	}

	private void renderDefault(Camera camera, Matrix4f viewMatrix, ShaderProgram shaderProgram) {

		shaderProgram.enable();

		//Update positions of concerned lights in view space (point and spot lights)
		List<PointLight> pointLights = gameObjectHandler.getAllOfType(PointLight.class);
		pointLights.forEach(light -> light.update(viewMatrix));
		List<SpotLight> spotLights = gameObjectHandler.getAllOfType(SpotLight.class);
		spotLights.forEach(light -> light.update(viewMatrix));

		//Render the current object
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("normalTransformationMatrix");
		//Lighting stuff
		shaderProgram.createUniform("specularPower");
		shaderProgram.createUniform("ambientLight");
		shaderProgram.createPointLightUniforms("pointLights", pointLights.size());
		shaderProgram.createSpotLightUniforms("spotLights", spotLights.size());
		shaderProgram.createDirectionalLightUniform("directionalLight");
		//Mesh materials - this should probably be handled by some sort background system
		shaderProgram.createMaterialUniform("material");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : gameObjectHandler.getAllOfType(ModeledGameObject.class)) {

			gameObject.update();

			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(viewMatrix));
			shaderProgram.setUniform("normalTransformationMatrix", gameObject.getTransformationMatrix());
			//Lighting
			shaderProgram.setUniform("ambientLight", new Vector3f(0.3f, 0.3f, 0.3f));
			shaderProgram.setUniform("specularPower", 10.0f); //Reflected light intensity
			pointLights.forEach(light -> shaderProgram.setUniform("pointLights", light, pointLights.indexOf(light)));
			spotLights.forEach(light -> shaderProgram.setUniform("spotLights", light, spotLights.indexOf(light)));
			shaderProgram.setUniform("directionalLight", gameObjectHandler.getObject("sun"));
			//Material stuff
			shaderProgram.setUniform("material", gameObject.getModel().getMaterial());

			gameObject.render();
		}
	}

	private void renderHud(ShaderProgram shaderProgram) {

		shaderProgram.enable();

		shaderProgram.createUniform("projModelMatrix");
		shaderProgram.createUniform("color");

		Matrix4f ortho = getWindow().getOrthoProjectionMatrix();
		for (TextGameObject text : hud.getTextGameObjects()) {

			text.update();

			shaderProgram.setUniform("projModelMatrix", text.getOrthoProjModelMatrix(ortho));
			shaderProgram.setUniform("color", new Vector4f(1, 1, 1, 1));

			text.render();
		}
		hud.render("boxTest");
	}
}
