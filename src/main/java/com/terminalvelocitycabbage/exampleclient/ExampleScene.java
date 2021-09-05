package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.components.FirstPersonCamera;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.model.AnimatedModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.client.renderer.scenes.Scene;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ExampleScene extends Scene {

	public ExampleScene() {
		super(new FirstPersonCamera(60, 0.1f, 16000f), new GameInputHandler());
	}

	@Override
	public void init(Window window) {

		//Load trex model to a Model object from dcm file
		AnimatedModel trexModel = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "trex.dcm"));
		trexModel.addAnimation("roar", ANIMATION, new Identifier(GameClient.ID, "roar.dca")).setLoopingStart(1.25F);
		trexModel.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "trex.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		/* T-rex stress test code
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				objectHandler.add("trexx"+x+"y"+y, new ModeledGameObject(trexModel));
				objectHandler.getObject("trexx"+x+"y"+y).move(x * 20, y * 20, 0);
			}
		}
		 */
		objectHandler.add("trex", new ModeledGameObject(trexModel));
		objectHandler.getObject("trex").move(-50F, 0F, -30F);
		trexModel.startAnimation("roar").loopForever();

		//The animation v7 test model
		AnimatedModel v7test = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "v7test.dcm"));
		v7test.addAnimation("bump", ANIMATION, new Identifier(GameClient.ID, "v7test.dca"));
		v7test.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "v7test.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		objectHandler.add("v7test", new ModeledGameObject(v7test));
		objectHandler.getObject("v7test").move(30F, 0, -20F);
		v7test.startAnimation("bump").loopForever();

		//The animation loop test model
		AnimatedModel loopTest = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "looptest.dcm"));
		loopTest.addAnimation("test", ANIMATION, new Identifier(GameClient.ID, "looptest.dca"));
		loopTest.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "kyle.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		objectHandler.add("loopTest", new ModeledGameObject(loopTest));
		objectHandler.getObject("loopTest").move(0, 0, -20F);
		loopTest.startAnimation("test").loopUntil(() -> GameInputHandler.FINISH_LOOPING.isKeyPressed());

		//Load gerald model to a Model object from dcm file
		AnimatedModel robotModel = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "Gerald.dcm"));
		robotModel.addAnimation("wave", ANIMATION, new Identifier(GameClient.ID, "wave.dca"));
		robotModel.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "gerald_base.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		ModeledGameObject robot = objectHandler.add("robot", new ModeledGameObject(robotModel));
		objectHandler.getObject("robot").move(0F, 0F, -30F);
		robotModel.startAnimation("wave").loopForever();
		//Add animation event listener here
		robotModel.handler.setSrc(robot);
		//disable this for now because it's annoying me in console
		//AnimationEventRegister.registerEvent("foo", (data, src) -> Log.info(data + ", " + src));

		//Do it again so we have two objects with different models and different textures
		AnimatedModel wormModel = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "Worm.dcm"));
		wormModel.setMaterial(Material.builder()
			.texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "worm.png")))
			.build());
		objectHandler.add("worm", new ModeledGameObject(wormModel));
		objectHandler.getObject("worm").move(0, 0, 10);

		//bind all Game Objects
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.bind();
		}

		//Create some light
		Attenuation plAttenuation = new Attenuation(0.0f, 0.0f, 1.0f);
		objectHandler.add("blueLight", new PointLight(new Vector3f(0, 2, -0.5f), new Vector3f(0,0,1), 1.0f, plAttenuation));
		objectHandler.add("whiteLight", new PointLight(new Vector3f(0, 4, -0.5f), new Vector3f(1,1,1), 1.0f, plAttenuation));
		Attenuation slAttenuation = new Attenuation(0.0f, 0.0f, 0.02f);
		objectHandler.add("redSpotLight", new SpotLight(new Vector3f(0, 2, 0), new Vector3f(1, 0, 0), 1.0f, slAttenuation, new Vector3f(0, 1, 0), 140));
		objectHandler.add("sun", new DirectionalLight(new Vector3f(-0.68f, 0.55f, 0.42f), new Vector4f(1, 1, 0.5f, 1), 1.0f));
	}

	@Override
	public void tick(float deltaTime) {

		//Move around the point lights
		objectHandler.getObject("blueLight").move(0, (float)Math.sin(glfwGetTime())/10, 0);
		objectHandler.getObject("whiteLight").move(0, (float)Math.cos(glfwGetTime())/8, 0);

		//Animate the model
		ModeledGameObject trex = objectHandler.getObject("trex");
		((AnimatedModel)trex.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		trex.queueUpdate();

		ModeledGameObject looptest = objectHandler.getObject("loopTest");
		((AnimatedModel)looptest.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		looptest.queueUpdate();

		ModeledGameObject robot = objectHandler.getObject("robot");
		((AnimatedModel)robot.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		robot.queueUpdate();

		ModeledGameObject v7Test = objectHandler.getObject("v7test");
		((AnimatedModel)v7Test.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		v7Test.queueUpdate();

		//Update Inputs
		var firstPersonCamera = (FirstPersonCamera)getCamera();
		firstPersonCamera.resetDeltas();
		GameInputHandler inputHandler = (GameInputHandler) getInputHandler();
		if (inputHandler.moveForward()) firstPersonCamera.queueMove(0, 0, -1);
		if (inputHandler.moveBackward()) firstPersonCamera.queueMove(0, 0, 1);
		if (inputHandler.moveRight()) firstPersonCamera.queueMove(1, 0, 0);
		if (inputHandler.moveLeft()) firstPersonCamera.queueMove(-1, 0, 0);
		if (inputHandler.moveUp()) firstPersonCamera.queueMove(0, 1, 0);
		if (inputHandler.moveDown()) firstPersonCamera.queueMove(0, -1, 0);
		if (inputHandler.isRightButtonPressed()) {
			firstPersonCamera.queueRotate(inputHandler.getMouseDeltaX(), inputHandler.getMouseDeltaY());
		}
		firstPersonCamera.update(deltaTime / 1000F);
		inputHandler.resetDeltas();
	}

	@Override
	public void destroy() {
		//Cleanup
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.destroy();
		}
	}
}
