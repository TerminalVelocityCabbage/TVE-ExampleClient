package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {
	@Override
	public void processInput(long window) {

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		//TODO Setup a key listener system to dynamically register key listeners
		glfwSetKeyCallback(Renderer.getWindow(), (win, key, scancode, action, mods) -> {

			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(win, true); // We will detect this in the rendering loop
			}
		});
	}
}
