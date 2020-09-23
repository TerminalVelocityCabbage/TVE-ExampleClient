package com.terminalvelocitycabbage.exampleclient;

import org.joml.Vector3f;

public class Primitives {

	public static class Triangle {
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

	public static class ColoredTriangle {
		float[] vertices;
		float[] colors;

		public ColoredTriangle(Vector3f corner1, Vector3f corner2, Vector3f corner3,
							   Vector3f corner1Color, Vector3f corner2Color, Vector3f corner3Color) {
			vertices = new float[]{
					corner1.x, corner1.y, corner1.z,
					corner2.x, corner2.y, corner2.z,
					corner3.x, corner3.y, corner3.z
			};
			colors = new float[]{
					corner1Color.x, corner1Color.y, corner1Color.z,
					corner2Color.x, corner2Color.y, corner2Color.z,
					corner3Color.x, corner3Color.y, corner3Color.z
			};
		}

		public float[] getVertices() {
			float[] organizedVertexes = new float[18];

			organizedVertexes[0] = vertices[0];
			organizedVertexes[1] = vertices[1];
			organizedVertexes[2] = vertices[2];
			organizedVertexes[3] = colors[0];
			organizedVertexes[4] = colors[1];
			organizedVertexes[5] = colors[2];
			organizedVertexes[6] = vertices[3];
			organizedVertexes[7] = vertices[4];
			organizedVertexes[8] = vertices[5];
			organizedVertexes[9] = colors[3];
			organizedVertexes[10] = colors[4];
			organizedVertexes[11] = colors[5];
			organizedVertexes[12] = vertices[6];
			organizedVertexes[13] = vertices[7];
			organizedVertexes[14] = vertices[8];
			organizedVertexes[15] = colors[6];
			organizedVertexes[16] = colors[7];
			organizedVertexes[17] = colors[8];

			return organizedVertexes;
		}
	}
}
