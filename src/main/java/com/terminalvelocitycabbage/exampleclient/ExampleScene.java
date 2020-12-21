package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.model.AnimatedModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.client.renderer.scenes.Scene;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import net.dumbcode.studio.animation.events.AnimationEventRegister;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ExampleScene extends Scene {

	@Override
	public void init() {
		//Load trex model to a Model object from dcm file
		AnimatedModel trexModel = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "trex.dcm"));
		trexModel.addAnimation("roar", ANIMATION, new Identifier(GameClient.ID, "roar.dca")).setLoopStartTime(25F);
		trexModel.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "trex.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		objectHandler.add("trex", ModeledGameObject.builder().setModel(trexModel).build());
		objectHandler.getObject("trex").move(-50F, 0F, -30F);
		trexModel.startAnimation("roar", true);

		//The animation v7 test model
		AnimatedModel v7test = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "v7test.dcm"));
		v7test.addAnimation("bump", ANIMATION, new Identifier(GameClient.ID, "v7test.dca"));
		v7test.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "v7test.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		objectHandler.add("v7test", ModeledGameObject.builder().setModel(v7test).build());
		objectHandler.getObject("v7test").move(30F, 0, -20F);
		v7test.startAnimation("bump", true);

		//Load gerald model to a Model object from dcm file
		AnimatedModel robotModel = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "Gerald.dcm"));
		robotModel.addAnimation("wave", ANIMATION, new Identifier(GameClient.ID, "wave.dca"));
		robotModel.setMaterial(Material.builder().texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "gerald_base.png"))).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		ModeledGameObject robot = objectHandler.add("robot", ModeledGameObject.builder().setModel(robotModel).build());
		objectHandler.getObject("robot").move(0F, 0F, -30F);
		robotModel.startAnimation("wave", true);

		robotModel.handler.setSrc(robot);
		AnimationEventRegister.registerEvent("foo", (data, src) -> System.out.println(data + ", " + src));

		//Do it again so we have two objects with different models and different textures
		AnimatedModel wormModel = AnimatedModelLoader.load(MODEL, new Identifier(GameClient.ID, "Worm.dcm"));
		wormModel.setMaterial(Material.builder()
				.texture(new Texture(TEXTURE, new Identifier(GameClient.ID, "worm.png")))
				.build());
		objectHandler.add("worm", ModeledGameObject.builder().setModel(wormModel).build());
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
	public void update(float deltaTime) {

		//Move around the point lights
		objectHandler.getObject("blueLight").move(0, (float)Math.sin(glfwGetTime())/10, 0);
		objectHandler.getObject("whiteLight").move(0, (float)Math.cos(glfwGetTime())/8, 0);

		//Animate the model
		ModeledGameObject trex = objectHandler.getObject("trex");
		ModelAnimationHandler trexHandler = ((AnimatedModel) trex.getModel()).handler;
		//TODO animation smoothness (don't hard code in 20)
		trexHandler.animate(deltaTime / 50F);
		//Tell the engine that the game object needs to be re-rendered
		trex.queueUpdate();

		ModeledGameObject robot = objectHandler.getObject("robot");
		ModelAnimationHandler robotHandler = ((AnimatedModel) robot.getModel()).handler;
		//TODO animation smoothness (don't hard code in 20)
		robotHandler.animate(deltaTime / 50F);
		//Tell the engine that the game object needs to be re-rendered
		robot.queueUpdate();

		ModeledGameObject v7Test = objectHandler.getObject("v7test");
		ModelAnimationHandler v7TestModel = ((AnimatedModel) v7Test.getModel()).handler;
		//TODO animation smoothness (don't hard code in 20)
		v7TestModel.animate(deltaTime / 50F);
		//Tell the engine that the game object needs to be re-rendered
		v7Test.queueUpdate();
	}

	@Override
	public void destroy() {
		//Cleanup
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.destroy();
		}
	}
}
