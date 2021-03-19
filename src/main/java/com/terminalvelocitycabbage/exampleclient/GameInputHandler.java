package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.debug.Log;

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

	public static KeyBind FINISH_LOOPING;

	public static KeyBind CRASH_THE_GAME;

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
		FINISH_LOOPING = new KeyBind(GLFW_KEY_L);
		CRASH_THE_GAME = new KeyBind(GLFW_KEY_C);
	}

	@Override
	public void processInput(KeyBind keyBind) {

		if (CRASH_THE_GAME.isKeyPressed()) {
			Log.crash("The game was forced to crash manually.", new RuntimeException("Force Crash"));
		}

		//Escape closes the program by telling glfw that it should close
		if (keyBind.equalsKeyAndAction(CLOSE)) {
			setFocus(false);
			glfwSetWindowShouldClose(keyBind.getWindow(), true);
			return;
		}

		//Process movement inputs
		moveForward = FORWARD.isKeyPressed();
		moveBackward = BACKWARDS.isKeyPressed();
		moveLeft = LEFT.isKeyPressed();
		moveRight = RIGHT.isKeyPressed();
		moveDown = DOWN.isKeyPressed();
		moveUp = UP.isKeyPressed();
		rotateLeft = ROT_LEFT.isKeyPressed();
		rotateRight = ROT_RIGHT.isKeyPressed();

		if (TOGGLE_MENU.isKeyPressed()) {
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
