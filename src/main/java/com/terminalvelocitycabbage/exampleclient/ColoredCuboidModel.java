package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.ColoredCuboid;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An example java model for demonstration purposes.
 * In most cases this would never exist and instead if would be replaced with a model loader
 * a model loader would read a model from a file and convert it into a real model part tree.
 */
public class ColoredCuboidModel extends Model {

	public ColoredCuboidModel(ColoredCuboid cuboid) {
		super(new ArrayList<>(Collections.singletonList(new Part(cuboid, new ArrayList<>()))));
	}
}
