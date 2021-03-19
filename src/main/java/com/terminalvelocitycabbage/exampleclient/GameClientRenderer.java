package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.components.FirstPersonCamera;
import com.terminalvelocitycabbage.engine.client.renderer.components.FreeCamera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Canvas;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.FRAGMENT;
import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.VERTEX;
import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.SHADER;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private final ShaderHandler shaderHandler = new ShaderHandler();
	private GameInputHandler inputHandler;

	public GameClientRenderer(int width, int height, String title, float tickRate) {
		super(width, height, title, new GameInputHandler(), tickRate);
		getWindow().setvSync(true);
	}

	@Override
	public void init() {
		super.init();

		//Create a ui screen ExampleCanvas
		canvasHandler.addCanvas("example", new ExampleCanvas(getWindow()));

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

		//Create shader program for text
		shaderHandler.newProgram("text");
		shaderHandler.queueShader("text", VERTEX, SHADER, new Identifier(GameClient.ID, "text.vert"));
		shaderHandler.queueShader("text", FRAGMENT, SHADER, new Identifier(GameClient.ID, "text.frag"));
		shaderHandler.build("text");

		//Store InputHandler
		inputHandler = (GameInputHandler) getWindow().getInputHandler();

		//Create Scenes
		sceneHandler.addScene("example", new ExampleScene(new FirstPersonCamera(60, 0.01f, 1000.0f)));

		//Init the scene
		sceneHandler.loadScene("example");

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
		//((FirstPersonCamera)sceneHandler.getActiveScene().getCamera()).move(inputHandler.getCameraPositionMoveVector(), 1f); //1f * (getDeltaTime() / 16
		var camera = sceneHandler.getActiveScene().getCamera();
		if (camera instanceof FreeCamera) {
			var freeCamera = (FreeCamera)camera;
			freeCamera.linearAcceleration.zero();
			freeCamera.angularAcceleration.zero();
			if (inputHandler.moveForward()) freeCamera.accelerateForward();
			if (inputHandler.moveBackward()) freeCamera.accelerateBackwards();
			if (inputHandler.moveRight()) freeCamera.accelerateRight();
			if (inputHandler.moveLeft()) freeCamera.accelerateLeft();
			if (inputHandler.moveUp()) freeCamera.accelerateUp();
			if (inputHandler.moveDown()) freeCamera.accelerateDown();
			if (inputHandler.rotateRight()) freeCamera.accelerateTwistRight();
			if (inputHandler.rotateLeft()) freeCamera.accelerateTwistLeft();
			if (inputHandler.isRightButtonPressed()) {
				freeCamera.rotate(inputHandler.getMouseDeltaX(), inputHandler.getMouseDeltaY(), freeCamera.rotateZ);
			}
			freeCamera.update(getDeltaTimeInSeconds());
		}
		if (camera instanceof FirstPersonCamera) {
			var firstPersonCamera = (FirstPersonCamera)camera;
			firstPersonCamera.resetDeltas();
			if (inputHandler.moveForward()) firstPersonCamera.queueMove(0, 0, -1);
			if (inputHandler.moveBackward()) firstPersonCamera.queueMove(0, 0, 1);
			if (inputHandler.moveRight()) firstPersonCamera.queueMove(1, 0, 0);
			if (inputHandler.moveLeft()) firstPersonCamera.queueMove(-1, 0, 0);
			if (inputHandler.moveUp()) firstPersonCamera.queueMove(0, 1, 0);
			if (inputHandler.moveDown()) firstPersonCamera.queueMove(0, -1, 0);
			if (inputHandler.isRightButtonPressed()) {
				firstPersonCamera.queueRotate(inputHandler.getMouseDeltaX(), inputHandler.getMouseDeltaY());
			}
			firstPersonCamera.update(getDeltaTimeInSeconds());
		}
		inputHandler.resetDeltas();

		if (GameClient.getInstance().stateHandler.isActive("normals")){
			renderNormalsDebug(camera, shaderHandler.get("normals"));
		} else {
			renderDefault(camera, shaderHandler.get("default"));
		}

		if (GameClient.getInstance().stateHandler.isActive("example")) {
			canvasHandler.showCanvas("example");
			getWindow().showCursor();
			glDisable(GL_DEPTH_TEST);
			renderHud(shaderHandler.get("hud"));
			renderText(shaderHandler.get("text"));
			glEnable(GL_DEPTH_TEST);
		} else {
			getWindow().hideCursor();
			canvasHandler.hideCanvas("example");
		}

		//Since the text rendering is so awful I'm just going to use the window title for now
		getWindow().setTitle("FPS: " + String.valueOf(getFramerate()).split("\\.")[0] + " (" + getFrameTimeAverageMillis() + "ms)");

		//Update the scene
		sceneHandler.update(getDeltaTimeInMillis());

		//Send the frame
		push();
	}

	@Override
	public void destroy() {
		super.destroy();
		shaderHandler.cleanup();
		canvasHandler.cleanup();
		sceneHandler.cleanup();
	}

	private void renderNormalsDebug(Camera camera, ShaderProgram shaderProgram) {
		//Point light
		shaderProgram.enable();

		//Render the current object
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("normalTransformationMatrix");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : sceneHandler.getActiveScene().getObjectsOfType(ModeledGameObject.class)) {
			gameObject.update();
			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(camera.getViewMatrix()));
			shaderProgram.setUniform("normalTransformationMatrix", gameObject.getTransformationMatrix());
			gameObject.render();
		}
	}

	private void renderDefault(Camera camera, ShaderProgram shaderProgram) {

		shaderProgram.enable();

		//Update positions of concerned lights in view space (point and spot lights)
		List<PointLight> pointLights = sceneHandler.getActiveScene().getObjectsOfType(PointLight.class);
		pointLights.forEach(light -> light.update(camera.getViewMatrix()));
		List<SpotLight> spotLights = sceneHandler.getActiveScene().getObjectsOfType(SpotLight.class);
		spotLights.forEach(light -> light.update(camera.getViewMatrix()));

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
		for (ModeledGameObject gameObject : sceneHandler.getActiveScene().getObjectsOfType(ModeledGameObject.class)) {

			gameObject.update();

			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(camera.getViewMatrix()));
			shaderProgram.setUniform("normalTransformationMatrix", gameObject.getTransformationMatrix());
			//Lighting
			shaderProgram.setUniform("ambientLight", new Vector3f(0.3f, 0.3f, 0.3f));
			shaderProgram.setUniform("specularPower", 10.0f); //Reflected light intensity
			shaderProgram.setUniform("pointLightsNum", pointLights.size());
			pointLights.forEach(light -> shaderProgram.setUniform("pointLights", light, pointLights.indexOf(light)));
			shaderProgram.setUniform("spotLightsNum", spotLights.size());
			spotLights.forEach(light -> shaderProgram.setUniform("spotLights", light, spotLights.indexOf(light)));
			shaderProgram.setUniform("directionalLight", sceneHandler.getActiveScene().objectHandler.getObject("sun"));
			//Material stuff
			shaderProgram.setUniform("material", gameObject.getModel().getMaterial());

			gameObject.render();
		}
	}

	private void renderText(ShaderProgram shaderProgram) {
		shaderProgram.enable();

		for (Canvas canvas : canvasHandler.getCanvases()) {
			canvas.renderText();
			for (UIRenderable child : canvas.getAllChildren()) {
				child.renderText();
			}
		}
	}

	private void renderHud(ShaderProgram shaderProgram) {

		shaderProgram.enable();

		shaderProgram.createUniform("color");
		shaderProgram.createUniform("screenRes");
		shaderProgram.createUniform("cornerStuff");
		shaderProgram.createUniform("borderColor");
		shaderProgram.createUniform("borderThickness");

		canvasHandler.getActiveCanvases().forEach(canvas -> renderHudElement(canvas, shaderProgram));
		canvasHandler.getActiveCanvases().forEach(canvas -> canvas.getAllChildren().forEach(element -> renderHudElement(element, shaderProgram)));
	}

	public void renderHudElement(UIRenderable element, ShaderProgram shaderProgram) {
		element.update();

		shaderProgram.setUniform("color", element.style.getColor());
		shaderProgram.setUniform("screenRes", new Vector2f(getWindow().width(), getWindow().height()));
		Rectangle rectangle = element.getRectangle();
		shaderProgram.setUniform("cornerStuff", new Matrix3f(
			rectangle.vertices[0].getX(), rectangle.vertices[0].getY(), rectangle.vertices[1].getX(),
			rectangle.vertices[1].getY(), rectangle.vertices[2].getX(), rectangle.vertices[2].getY(),
			rectangle.vertices[3].getX(), rectangle.vertices[3].getY(), element.style.getBorderRadius()
		));
		shaderProgram.setUniform("borderColor", element.style.getBorderColor());
		shaderProgram.setUniform("borderThickness", element.style.getBorderThickness());

		element.render();
	}
}
