package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UICanvas;
import com.terminalvelocitycabbage.engine.client.renderer.util.GameObjectHandler;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.exampleclient.models.DCModel;
import net.dumbcode.studio.animation.events.AnimationEventRegister;
import net.dumbcode.studio.animation.info.AnimationInfo;
import net.dumbcode.studio.animation.info.AnimationLoader;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;
import org.joml.*;

import java.io.IOException;
import java.lang.Math;
import java.util.List;
import java.util.UUID;

import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.FRAGMENT;
import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.VERTEX;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PERCENT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PIXELS;
import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private GameObjectHandler gameObjectHandler = new GameObjectHandler();
	private final ShaderHandler shaderHandler = new ShaderHandler();
	private GameInputHandler inputHandler = new GameInputHandler();

	private UICanvas testCanvas = new UICanvas(getWindow());

	private UUID roarAnimationUUID;
	private AnimationInfo roarAnimation;
	private UUID waveAnimationUUID;
	private AnimationInfo waveAnimation;

	public GameClientRenderer(int width, int height, String title) {
		super(width, height, title, new GameInputHandler());
		getWindow().setvSync(true);
	}

	@Override
	public void init() {
		super.init();

		//Create the controllable camera
		camera = new Camera(60, 0.01f, 1000.0f);

		//Configure the canvas
		testCanvas.bind();
		testCanvas.style
				.marginLeft(100, PIXELS)
				.marginRight(40, PERCENT)
				.marginTop(10, PIXELS)
				.marginBottom(10, PIXELS)
				.setBackgroundColor(0.3f, 0.4f, 1 ,0.25f)
				.setBorderRadius(15)
				.setBorderColor(1, 1, 1, 1)
				.setBorderThickness(4);
		testCanvas.queueUpdate();

		//Load trex model to a Model object from dcm file
		DCModel trexModel = DCModel.load(MODEL, new Identifier(GameClient.ID, "trex.dcm"));
		trexModel.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "trex.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		gameObjectHandler.add("trex", ModeledGameObject.builder().setModel(trexModel).build());
		gameObjectHandler.getObject("trex").move(-50F, 0F, -30F);
		try {
			this.roarAnimation = AnimationLoader.loadAnimation(ANIMATION.getResource(new Identifier(GameClient.ID, "roar.dca")).orElseThrow().openStream());
			this.roarAnimationUUID = trexModel.handler.startAnimation(roarAnimation);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		//Load gerald model to a Model object from dcm file
		DCModel robotModel = DCModel.load(MODEL, new Identifier(GameClient.ID, "Gerald.dcm"));
		robotModel.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "gerald_base.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		gameObjectHandler.add("robot", ModeledGameObject.builder().setModel(robotModel).build());
		gameObjectHandler.getObject("robot").move(0F, 0F, -30F);
		try {
			this.waveAnimation = AnimationLoader.loadAnimation(ANIMATION.getResource(new Identifier(GameClient.ID, "wave.dca")).orElseThrow().openStream());
			this.waveAnimationUUID = robotModel.handler.startAnimation(waveAnimation);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		AnimationEventRegister.registerEvent("foo", (data, src) -> System.out.println(data + ", " + src));

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

		//Create shader program for debugging normals directions
		shaderHandler.newProgram("hud");
		shaderHandler.queueShader("hud", VERTEX, SHADER, new Identifier(GameClient.ID, "hud.vert"));
		shaderHandler.queueShader("hud", FRAGMENT, SHADER, new Identifier(GameClient.ID, "hud.frag"));
		shaderHandler.build("hud");

		//Store InputHandler
		inputHandler = (GameInputHandler) getWindow().getInputHandler();

		//Create some light
		Attenuation plAttenuation = new Attenuation(0.0f, 0.0f, 1.0f);
		gameObjectHandler.add("blueLight", new PointLight(new Vector3f(0, 2, -0.5f), new Vector3f(0,0,1), 1.0f, plAttenuation));
		gameObjectHandler.add("whiteLight", new PointLight(new Vector3f(0, 4, -0.5f), new Vector3f(1,1,1), 1.0f, plAttenuation));
		Attenuation slAttenuation = new Attenuation(0.0f, 0.0f, 0.02f);
		gameObjectHandler.add("redSpotLight", new SpotLight(new Vector3f(0, 2, 0), new Vector3f(1, 0, 0), 1.0f, slAttenuation, new Vector3f(0, 1, 0), 140));
		gameObjectHandler.add("sun", new DirectionalLight(new Vector3f(-0.68f, 0.55f, 0.42f), new Vector4f(1, 1, 0.5f, 1), 1.0f));

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
		camera.move(inputHandler.getCameraPositionMoveVector(), 1f);
		//Only allow looking around when right click is held
		if (!inputHandler.isRightButtonPressed()) inputHandler.resetDisplayVector();
		//Update camera rotation
		camera.rotate(inputHandler.getDisplayVector().mul(0.4f), inputHandler.getCameraRollVector() * 0.05f);

		//Move around the point lights
		gameObjectHandler.getObject("blueLight").move(0, (float)Math.sin(glfwGetTime())/10, 0);
		gameObjectHandler.getObject("whiteLight").move(0, (float)Math.cos(glfwGetTime())/8, 0);

		//Animate the model
		ModeledGameObject trex = gameObjectHandler.getObject("trex");
		ModelAnimationHandler trexHandler = ((DCModel) trex.getModel()).handler;
		trexHandler.animate(this.getAnimationDeltaTime());
		if(!trexHandler.isPlaying(this.roarAnimationUUID)) {
			this.roarAnimationUUID = trexHandler.startAnimation(this.roarAnimation);
		}
		//Tell the engine that the game object needs to be re-rendered
		trex.queueUpdate();
		ModeledGameObject robot = gameObjectHandler.getObject("robot");
		ModelAnimationHandler robotHandler = ((DCModel) robot.getModel()).handler;
		robotHandler.animate(this.getAnimationDeltaTime());
		if(!robotHandler.isPlaying(this.waveAnimationUUID)) {
			this.waveAnimationUUID = robotHandler.startAnimation(this.waveAnimation);
		}
		//Tell the engine that the game object needs to be re-rendered
		robot.queueUpdate();

		//Update the view Matrix with the current camera position
		//This has to happen before game items are updated
		viewMatrix.identity().set(camera.getViewMatrix());

		//renderNormalsDebug(camera, viewMatrix, shaderHandler.get("normals"));
		renderDefault(camera, viewMatrix, shaderHandler.get("default"));
		renderHud(shaderHandler.get("hud"));

		//Since the text rendering is so awful I'm just going to use the window title for now
		getWindow().setTitle("FPS: " + String.valueOf(getFramerate()).split("\\.")[0] + " (" + getFrameTimeAverageMillis() + "ms)");

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
		shaderHandler.cleanup();
		testCanvas.destroy();
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

		shaderProgram.createUniform("color");
		shaderProgram.createUniform("screenRes");
		shaderProgram.createUniform("cornerStuff");
		shaderProgram.createUniform("borderColor");
		shaderProgram.createUniform("borderThickness");

		testCanvas.update();

		shaderProgram.setUniform("color", testCanvas.getStyle().getBackgroundColor());
		shaderProgram.setUniform("screenRes", new Vector2f(getWindow().width(), getWindow().height()));
		Rectangle rectangle = testCanvas.getRectangle();
		shaderProgram.setUniform("cornerStuff", new Matrix3f(
				rectangle.vertices[0].getX(), rectangle.vertices[0].getY(), rectangle.vertices[1].getX(),
				rectangle.vertices[1].getY(), rectangle.vertices[2].getX(), rectangle.vertices[2].getY(),
				rectangle.vertices[3].getX(), rectangle.vertices[3].getY(), testCanvas.style.getBorderRadius()
		));
		shaderProgram.setUniform("borderColor", testCanvas.style.getBorderColor());
		shaderProgram.setUniform("borderThickness", testCanvas.style.getBorderThickness());

		testCanvas.render();

	}
}
