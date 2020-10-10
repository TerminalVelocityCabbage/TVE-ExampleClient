package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {

	public static KeyBind CLOSE;

	public static KeyBind FORWARD;
	public static KeyBind BACKWARDS;
	public static KeyBind LEFT;
	public static KeyBind RIGHT;
	public static KeyBind UP;
	public static KeyBind DOWN;

	Vector3f cameraIncrementVector = new Vector3f();

	@Override
	public void init(Window window) {
		super.init(window);
		CLOSE = new KeyBind(GLFW_KEY_ESCAPE, KeyBind.ANY, GLFW_RELEASE, KeyBind.NONE);
		FORWARD = new KeyBind(GLFW_KEY_W);
		BACKWARDS = new KeyBind(GLFW_KEY_S);
		LEFT = new KeyBind(GLFW_KEY_A);
		RIGHT = new KeyBind(GLFW_KEY_D);
		UP = new KeyBind(GLFW_KEY_SPACE);
		DOWN = new KeyBind(GLFW_KEY_LEFT_SHIFT);
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
