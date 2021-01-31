package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {

	public static KeyBind CLOSE;

	public static KeyBind FORWARD;
	private boolean moveForward;
	public static KeyBind BACKWARDS;
	private boolean moveBackward;
	public static KeyBind LEFT;
	private boolean moveLeft;
	public static KeyBind RIGHT;
	private boolean moveRight;
	public static KeyBind UP;
	private boolean moveUp;
	public static KeyBind DOWN;
	private boolean moveDown;

	public static KeyBind ROT_LEFT;
	private boolean rotateRight;
	public static KeyBind ROT_RIGHT;
	private boolean rotateLeft;

	public static KeyBind TOGGLE_MENU;

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
		ROT_LEFT = new KeyBind(GLFW_KEY_Q);
		ROT_RIGHT = new KeyBind(GLFW_KEY_E);
		TOGGLE_MENU = new KeyBind(GLFW_KEY_TAB);
	}

	@Override
	public void processInput(KeyBind keyBind) {

		//Escape closes the program by telling glfw that it should close
		if (keyBind.equalsKeyAndAction(CLOSE)) {
			setFocus(false);
			glfwSetWindowShouldClose(keyBind.getWindow(), true);
			return;
		}

		//Process movement inputs
		moveForward = keyBind.isKeyPressed(FORWARD);
		moveBackward = keyBind.isKeyPressed(BACKWARDS);
		moveLeft = keyBind.isKeyPressed(LEFT);
		moveRight = keyBind.isKeyPressed(RIGHT);
		moveDown = keyBind.isKeyPressed(DOWN);
		moveUp = keyBind.isKeyPressed(UP);
		rotateLeft = keyBind.isKeyPressed(ROT_LEFT);
		rotateRight = keyBind.isKeyPressed(ROT_RIGHT);

		if (keyBind.isKeyPressed(TOGGLE_MENU)) {
			if (GameClient.getInstance().stateHandler.isActive("example")) {
				GameClient.getInstance().stateHandler.resetState();
			} else {
				GameClient.getInstance().stateHandler.setState("example");
			}
		}
	}

	public boolean moveForward() {
		return moveForward;
	}

	public boolean moveBackward() {
		return moveBackward;
	}

	public boolean moveLeft() {
		return moveLeft;
	}

	public boolean moveRight() {
		return moveRight;
	}

	public boolean moveUp() {
		return moveUp;
	}

	public boolean moveDown() {
		return moveDown;
	}

	public boolean rotateRight() {
		return rotateRight;
	}

	public boolean rotateLeft() {
		return rotateLeft;
	}
}
