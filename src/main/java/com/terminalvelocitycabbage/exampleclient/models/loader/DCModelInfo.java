package com.terminalvelocitycabbage.exampleclient.models.loader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public class DCModelInfo {

	protected float version;
	protected String author;
	protected int textureWidth;
	protected int textureHeight;
	protected ArrayList<DCMCube> children;

	public List<DCMCube> getChildren() {
		return children;
	}


	public DCMCube getCube(String name) {
		DCMCube potentialCube;
		for (DCMCube cube : children) {
			if (cube.name.equals(name)) {
				return cube;
			}
			potentialCube = cube.getCube(name);
			if (potentialCube != null && potentialCube.name.equals(name)) {
				return potentialCube;
			}
		}
		throw new RuntimeException("Could not get cube by name " + name);
	}

	private String printCubes(DCMCube cube) {
		StringBuilder stringBuilder = new StringBuilder();
		for (DCMCube cubeChild : cube.children) {
			stringBuilder.append("\t");
			stringBuilder.append(cubeChild.toString());
			stringBuilder.append("\n\t").append(printCubes(cubeChild));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (DCMCube cube : children) {
			stringBuilder.append("\n").append(cube.toString());
			stringBuilder.append(printCubes(cube));
		}
		return "DCMModel{" +
				"version=" + version +
				", author='" + author + '\'' +
				", textureWidth=" + textureWidth +
				", textureHeight=" + textureHeight +
				", children=" + stringBuilder.toString() +
				'}';
	}

	public static class Loader {

		private static ArrayList<DCMCube> readCubes(DataInputStream data, DCModelInfo model) throws IOException {
			ArrayList<DCMCube> cubes = new ArrayList<>();
			int amount = (int) data.readFloat();
			for (int i = 0; i < amount; i++) {
				cubes.add(new DCMCube(
						data.readUTF(),
						new Vector3i((int) data.readFloat(), (int) data.readFloat(), (int) data.readFloat()), //Dimensions
						new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Rotation Point
						new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Offset
						new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Rotation
						new Vector2i((int) data.readFloat(), (int) data.readFloat()), //Texture Offset
						data.readBoolean(), //Texture Mirrored
						new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Cube Grow
						readCubes(data, model), //Children
						model));
				//Create the array for this child's location
			}
			return cubes;
		}

		public static DCModelInfo load(ResourceManager resourceManager, Identifier identifier) {
			Optional<DataInputStream> optData = resourceManager.getResource(identifier).flatMap(Resource::asDataStream);
			DCModelInfo model = new DCModelInfo();
			if (optData.isPresent()) {
				try {
					DataInputStream data = optData.get();
					model.version = data.readFloat();
					model.author = data.readUTF();
					model.textureWidth = (int) data.readFloat();
					model.textureHeight = (int) data.readFloat();
					model.children = readCubes(data, model);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Log.error("No resource found for model " + identifier.toString());
			}
			return model;
		}
	}

	public static class DCMCube {

		public String name;
		public Vector3i dimensions;
		public Vector3f rotationPoint;
		public Vector3f offset;
		public Vector3f rotation;
		public Vector2i textureOffset;
		public boolean textureMirrored;
		public Vector3f scale;
		public ArrayList<DCMCube> children;
		public DCModelInfo model;
		public float[][] uvs;

		public DCMCube(String name, Vector3i dimensions, Vector3f rotationPoint, Vector3f offset, Vector3f rotation,
					   Vector2i textureOffset, boolean textureMirrored, Vector3f scale, ArrayList<DCMCube> children,
					   DCModelInfo model) {
			this.name = name;
			this.dimensions = dimensions;
			this.rotationPoint = rotationPoint;
			this.offset = offset;
			this.rotation = rotation;
			this.textureOffset = textureOffset;
			this.textureMirrored = textureMirrored;
			this.scale = scale;
			this.children = children;
			this.model = model;
			this.uvs = setUVs();
		}

		private DCMCube getCube(String name) {
			DCMCube potentialCube;
			for (DCMCube cube : children) {
				if (cube.name.equals(name)) {
					return cube;
				}
				potentialCube = cube.getCube(name);
				if (potentialCube != null && potentialCube.name.equals(name)) {
					return potentialCube;
				}
			}
			return null;
		}

		private float[][] setUVs() {

			float[][] pixelUvs = new float[6][4];
			//South
			pixelUvs[0][0] = textureOffset.x + (2 * dimensions.z) + dimensions.x;
			pixelUvs[0][1] = textureOffset.y + dimensions.z;
			pixelUvs[0][2] = textureOffset.x + (2 * dimensions.z) + (2 * dimensions.x);
			pixelUvs[0][3] = textureOffset.y + dimensions.z + dimensions.y;
			//East
			pixelUvs[1][0] = textureOffset.x + dimensions.z + dimensions.x;
			pixelUvs[1][1] = textureOffset.y + dimensions.z;
			pixelUvs[1][2] = textureOffset.x + (2 * dimensions.z) + dimensions.x;
			pixelUvs[1][3] = textureOffset.y + dimensions.z + dimensions.y;
			//North
			pixelUvs[2][0] = textureOffset.x + dimensions.z;
			pixelUvs[2][1] = textureOffset.y + dimensions.z;
			pixelUvs[2][2] = textureOffset.x + dimensions.z + dimensions.x;
			pixelUvs[2][3] = textureOffset.y + dimensions.z + dimensions.y;
			//West
			pixelUvs[3][0] = textureOffset.x;
			pixelUvs[3][1] = textureOffset.y + dimensions.z;
			pixelUvs[3][2] = textureOffset.x + dimensions.z;
			pixelUvs[3][3] = textureOffset.y + dimensions.z + dimensions.y;
			//Top
			pixelUvs[4][0] = textureOffset.x + dimensions.z;
			pixelUvs[4][1] = textureOffset.y;
			pixelUvs[4][2] = textureOffset.x + dimensions.z + dimensions.x;
			pixelUvs[4][3] = textureOffset.y + dimensions.z;
			//Bottom
			pixelUvs[5][0] = textureOffset.x + dimensions.z + dimensions.x;
			pixelUvs[5][1] = textureOffset.y;
			pixelUvs[5][2] = textureOffset.x + (2 * dimensions.z) + dimensions.x;
			pixelUvs[5][3] = textureOffset.y + dimensions.z;

			//clamp the values from 0 to 1
			for (int i = 0; i < pixelUvs.length; i++) {
				pixelUvs[i][0] /= model.textureWidth;
				pixelUvs[i][1] /= model.textureHeight;
				pixelUvs[i][2] /= model.textureWidth;
				pixelUvs[i][3] /= model.textureHeight;
			}

			return pixelUvs;
		}

		public String getName() {
			return name;
		}

		public ArrayList<DCMCube> getChildren() {
			return children;
		}

		@Override
		public String toString() {
			return "DCMCube{" +
					"name='" + name + '\'' +
					", dimensions=" + dimensions +
					", rotationPoint=" + rotationPoint +
					", offset=" + offset +
					", rotation=" + rotation +
					", textureOffset=" + textureOffset +
					", textureMirrored=" + textureMirrored +
					", scale=" + scale +
					'}';
		}
	}
}
