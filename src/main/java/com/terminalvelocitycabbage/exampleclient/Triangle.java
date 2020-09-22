package com.terminalvelocitycabbage.exampleclient;

import org.joml.Vector3f;

public class Triangle {

	float[] vertices;

	public Triangle(Vector3f corner1, Vector3f corner2, Vector3f corner3) {
		vertices = new float[]{
				corner1.x, corner1.y, corner1.z,
				corner2.x, corner2.y, corner2.z,
				corner3.x, corner3.y, corner3.z
		};
	}

	public float[] getVertices() {
		return vertices;
	}
}
