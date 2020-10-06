package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {

	Vector3f cameraIncrementVector = new Vector3f();

	@Override
	public void init(Window window) {
		super.init(window);
	}

	@Override
	public void processInput(Window window) {

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		//TODO Setup a key listener system to dynamically register key listeners
		glfwSetKeyCallback(window.getID(), (win, key, scancode, action, mods) -> {

			//Make sure we're not supposed to close the game first
			//In the future this will be used for opening the escape menu too
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				setFocus(false);
				glfwSetWindowShouldClose(win, true); // We will detect this in the rendering loop
			}

			//Process key inputs
			cameraIncrementVector.set(0, 0, 0);
			if (isKeyPressed(window, GLFW_KEY_W)) {
				cameraIncrementVector.z--;
			}
			if (isKeyPressed(window, GLFW_KEY_S)) {
				cameraIncrementVector.z++;
			}
			if (isKeyPressed(window, GLFW_KEY_A)) {
				cameraIncrementVector.x--;
			}
			if (isKeyPressed(window, GLFW_KEY_D)) {
				cameraIncrementVector.x++;
			}
			if (isKeyPressed(window, GLFW_KEY_LEFT_SHIFT)) {
				cameraIncrementVector.y--;
			}
			if (isKeyPressed(window, GLFW_KEY_SPACE)) {
				cameraIncrementVector.y++;
			}
		});
	}

	public boolean isKeyPressed(Window window, int keyCode) {
		return glfwGetKey(window.getID(), keyCode) == GLFW_PRESS;
	}

	public Vector3f getCameraIncrementVector() {
		return cameraIncrementVector;
	}
}
