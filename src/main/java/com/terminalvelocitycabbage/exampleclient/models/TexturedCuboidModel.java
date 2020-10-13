package com.terminalvelocitycabbage.exampleclient.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedCuboid;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;

public class TexturedCuboidModel extends Model {

	public TexturedCuboidModel(TexturedCuboid cuboid) {
		super(new ArrayList<>(Collections.singletonList(new Part(cuboid, new Vector3f(1, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new ArrayList<>()))));
	}
}
