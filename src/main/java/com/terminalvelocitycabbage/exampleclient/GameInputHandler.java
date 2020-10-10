package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {

	public static final KeyBind CLOSE = new KeyBind(getWindow(), GLFW_KEY_ESCAPE, KeyBind.ANY, GLFW_RELEASE, KeyBind.NONE);

	public static final KeyBind FORWARD = new KeyBind(getWindow(), GLFW_KEY_W);
	public static final KeyBind BACKWARDS = new KeyBind(getWindow(), GLFW_KEY_S);
	public static final KeyBind LEFT = new KeyBind(getWindow(), GLFW_KEY_A);
	public static final KeyBind RIGHT = new KeyBind(getWindow(), GLFW_KEY_D);
	public static final KeyBind UP = new KeyBind(getWindow(), GLFW_KEY_SPACE);
	public static final KeyBind DOWN = new KeyBind(getWindow(), GLFW_KEY_LEFT_SHIFT);

	Vector3f cameraIncrementVector = new Vector3f();

	@Override
	public void init(Window window) {
		super.init(window);
	}

	@Override
	public void processInput(KeyBind keyBind) {

		//Escape closes the program by telling glfw that it should close
		if (keyBind.equalsKeyAndAction(CLOSE)) {
			setFocus(false);
			glfwSetWindowShouldClose(keyBind.getWindow(), true);
		}

		//Process movement inputs
		cameraIncrementVector.set(0, 0, 0);
		if (keyBind.isKeyPressed(FORWARD)) {
			cameraIncrementVector.z--;
		}
		if (keyBind.isKeyPressed(BACKWARDS)) {
			cameraIncrementVector.z++;
		}
		if (keyBind.isKeyPressed(LEFT)) {
			cameraIncrementVector.x--;
		}
		if (keyBind.isKeyPressed(RIGHT)) {
			cameraIncrementVector.x++;
		}
		if (keyBind.isKeyPressed(DOWN)) {
			cameraIncrementVector.y--;
		}
		if (keyBind.isKeyPressed(UP)) {
			cameraIncrementVector.y++;
		}
	}

	public Vector3f getCameraIncrementVector() {
		return cameraIncrementVector;
	}
}
