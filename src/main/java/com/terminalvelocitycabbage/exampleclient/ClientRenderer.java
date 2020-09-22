package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.terminalvelocityrenderer.Renderer;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class ClientRenderer extends Renderer {

	@Override
	public void loop() {
		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		double frame = 0.0;
		float valueR = 0.0f;
		float valueG = 0.0f;
		float valueB = 0.0f;

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(Renderer.getWindow()) ) {
			//Setup the frame for drawing
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Actual Logic
			valueR = (float)((Math.sin(frame/10) + 1)/2);
			valueG = (float)((Math.cos(frame/20) + 1)/2);
			valueB = (float)((Math.sin(frame/40) + 1)/2);
			glClearColor(valueR, valueG, valueB, 1.0f);
			if (frame == Float.MAX_VALUE) frame = 1.0;
			else frame++;

			//Send the frame
			glfwSwapBuffers(Renderer.getWindow());
			glfwPollEvents();
		}
	}
}
