package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.client.renderer.ui.*;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.FRAGMENT;
import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.VERTEX;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PERCENT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PIXELS;
import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.SHADER;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private final ShaderHandler shaderHandler = new ShaderHandler();
	private GameInputHandler inputHandler = new GameInputHandler();

	//TODO create a scene handler
	private ExampleScene exampleScene = new ExampleScene();

	private UICanvas testCanvas = new UICanvas(getWindow());

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
				.setColor(0.3f, 0.4f, 1 ,0.25f)
				.setBorderRadius(15)
				.setBorderColor(1, 1, 1, 1)
				.setBorderThickness(4);
		testCanvas.addContainer(new UIContainer(new UIDimension(100, PIXELS), new UIDimension(100, PIXELS), new UIAnchor(AnchorPoint.TOP_MIDDLE, AnchorDirection.RIGHT_DOWN), new UIStyle().setColor(1, 1, 0, 1)));
		testCanvas.addContainer(new UIContainer(new UIDimension(400, PIXELS), new UIDimension(50, PIXELS), new UIAnchor(AnchorPoint.TOP_MIDDLE, AnchorDirection.LEFT_DOWN), new UIStyle().setColor(1, 0, 0, 1).marginRight(10, PERCENT)));
		testCanvas.addContainer(new UIContainer(new UIDimension(400, PIXELS), new UIDimension(40, PERCENT), new UIAnchor(AnchorPoint.BOTTOM_MIDDLE, AnchorDirection.UP), new UIStyle().setColor(1, 0, 1, 1).marginBottom(10, PIXELS)));
		testCanvas.queueUpdate();

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

		//Init the scene
		exampleScene.init();

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

		//Update the view Matrix with the current camera position
		//This has to happen before game items are updated
		viewMatrix.identity().set(camera.getViewMatrix());

		//renderNormalsDebug(camera, viewMatrix, shaderHandler.get("normals"));
		renderDefault(camera, viewMatrix, shaderHandler.get("default"));
		renderHud(shaderHandler.get("hud"));

		//Since the text rendering is so awful I'm just going to use the window title for now
		getWindow().setTitle("FPS: " + String.valueOf(getFramerate()).split("\\.")[0] + " (" + getFrameTimeAverageMillis() + "ms)");

		//Update the scene
		exampleScene.update(getDeltaTime());

		//Send the frame
		push();
	}

	@Override
	public void destroy() {
		super.destroy();
		shaderHandler.cleanup();
		testCanvas.getContainers().forEach(UIRenderableElement::destroy);
		testCanvas.destroy();
		exampleScene.destroy();
	}

	private void renderNormalsDebug(Camera camera, Matrix4f viewMatrix, ShaderProgram shaderProgram) {
		//Point light
		shaderProgram.enable();

		//Render the current object
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("normalTransformationMatrix");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : exampleScene.getObjectsOfType(ModeledGameObject.class)) {
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
		List<PointLight> pointLights = exampleScene.getObjectsOfType(PointLight.class);
		pointLights.forEach(light -> light.update(viewMatrix));
		List<SpotLight> spotLights = exampleScene.getObjectsOfType(SpotLight.class);
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
		for (ModeledGameObject gameObject : exampleScene.getObjectsOfType(ModeledGameObject.class)) {

			gameObject.update();

			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(viewMatrix));
			shaderProgram.setUniform("normalTransformationMatrix", gameObject.getTransformationMatrix());
			//Lighting
			shaderProgram.setUniform("ambientLight", new Vector3f(0.3f, 0.3f, 0.3f));
			shaderProgram.setUniform("specularPower", 10.0f); //Reflected light intensity
			pointLights.forEach(light -> shaderProgram.setUniform("pointLights", light, pointLights.indexOf(light)));
			spotLights.forEach(light -> shaderProgram.setUniform("spotLights", light, spotLights.indexOf(light)));
			shaderProgram.setUniform("directionalLight", exampleScene.objectHandler.getObject("sun"));
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

		renderHudElement(testCanvas, shaderProgram);
		testCanvas.getContainers().forEach(container -> renderHudElement(container, shaderProgram));

	}

	public void renderHudElement(UIRenderableElement element, ShaderProgram shaderProgram) {

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
