package com.terminalvelocitycabbage.exampleclient.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An example java model for demonstration purposes.
 * In most cases this would never exist and instead if would be replaced with a model loader
 * a model loader would read a model from a file and convert it into a real model part tree.
 */
public class RectangleModel extends Model {

	public RectangleModel(TexturedRectangle rectangle) {
		super(new ArrayList<>(Collections.singletonList(new Part(rectangle, new ArrayList<>()))));
	}
}
