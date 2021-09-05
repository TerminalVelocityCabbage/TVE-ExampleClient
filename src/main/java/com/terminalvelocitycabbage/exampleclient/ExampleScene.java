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
		AnimatedModel trexModel = loadTypical("trex", "roar");
		objectHandler.getObject("trex").move(-50F, 0F, -30F);
		trexModel.startAnimation("roar").loopForever();

		//The animation v7 test model
		AnimatedModel v7test = loadTypical("v7test", "v7test");
		objectHandler.getObject("v7test").move(30F, 0, -20F);
		v7test.startAnimation("v7test").loopForever();

		//The animation loop test model
		AnimatedModel loopText = loadTypical("looptest", "looptest");
		objectHandler.getObject("looptest").move(0, 0, -20F);
		//TODO
		//loopTest.startAnimation("test").loopUntil(() -> GameInputHandler.FINISH_LOOPING.isKeyPressed());

		//Load gerald model to a Model object from dcm file
		AnimatedModel robotModel = loadTypical("gerald", "wave");
		objectHandler.getObject("gerald").move(0F, 0F, -30F);
		robotModel.startAnimation("wave").loopForever();
		//Add animation event listener here
		//robotModel.handler.setSrc(robot);
		//disable this for now because it's annoying me in console
		//AnimationEventRegister.registerEvent("foo", (data, src) -> Log.info(data + ", " + src));

		//bind all Game Objects
		objectHandler.getAllOfType(ModeledGameObject.class).forEach(ModeledGameObject::bind);

		//Create some light
		Attenuation plAttenuation = new Attenuation(0.0f, 0.0f, 1.0f);
		objectHandler.add("blueLight", new PointLight(new Vector3f(0, 2, -0.5f), new Vector3f(0,0,1), 1.0f, plAttenuation));
		objectHandler.add("whiteLight", new PointLight(new Vector3f(0, 4, -0.5f), new Vector3f(1,1,1), 1.0f, plAttenuation));
		Attenuation slAttenuation = new Attenuation(0.0f, 0.0f, 0.02f);
		objectHandler.add("redSpotLight", new SpotLight(new Vector3f(0, 2, 0), new Vector3f(1, 0, 0), 1.0f, slAttenuation, new Vector3f(0, 1, 0), 140));
		objectHandler.add("sun", new DirectionalLight(new Vector3f(-0.68f, 0.55f, 0.42f), new Vector4f(1, 1, 0.5f, 1), 1.0f));
	}

	@Override
	public void update(float deltaTime) {

		//Move around the point lights
		objectHandler.getObject("blueLight").move(0, (float)Math.sin(glfwGetTime())/10, 0);
		objectHandler.getObject("whiteLight").move(0, (float)Math.cos(glfwGetTime())/8, 0);

		//Animate the model
		ModeledGameObject trex = objectHandler.getObject("trex");
		((AnimatedModel)trex.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		trex.queueUpdate();

		ModeledGameObject looptest = objectHandler.getObject("looptest");
		((AnimatedModel)looptest.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		looptest.queueUpdate();

		ModeledGameObject robot = objectHandler.getObject("gerald");
		((AnimatedModel)robot.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		robot.queueUpdate();

		ModeledGameObject v7Test = objectHandler.getObject("v7test");
		((AnimatedModel)v7Test.getModel()).animate(deltaTime / 1000F);
		//Tell the engine that the game object needs to be re-rendered
		v7Test.queueUpdate();
	}

	@Override
	public void destroy() {
		objectHandler.getAllOfType(ModeledGameObject.class).forEach(ModeledGameObject::destroy);
	}

	private AnimatedModel loadTypical(String name, String... animations) {
		AnimatedModel model = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, name + ".dcm"));
		model.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, name + ".png"))).build());
		for (String animation : animations) {
			model.addAnimation(animation, ANIMATION, new Identifier(GameClient.ID, animation + ".dca"));
		}
		objectHandler.add(name, new ModeledGameObject(model));
		return model;
	}
}
