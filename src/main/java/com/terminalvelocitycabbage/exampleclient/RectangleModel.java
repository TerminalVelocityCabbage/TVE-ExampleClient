package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An example java model for demonstration purposes.
 * In most cases this would never exist and instead if would be replaced with a model loader
 * a model loader would read a model from a file and convert it into a real model part tree.
 */
public class RectangleModel extends Model {

	public RectangleModel(TexturedRectangle rectangle) {
		super(new ArrayList<Part>(Arrays.asList(new Model.Part(rectangle, new ArrayList<>()))));
	}
}
