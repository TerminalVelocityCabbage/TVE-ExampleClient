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
import java.util.ArrayList;
import java.util.Optional;

public class DCMModel {

	protected float version;
	protected String author;
	protected int textureWidth;
	protected int textureHeight;
	protected ArrayList<DCMCube> children;

	public static DCMModel loadDCMModel(ResourceManager resourceManager, Identifier identifier) {
		Optional<DataInputStream> optData = resourceManager.getResource(identifier).flatMap(Resource::asDataStream);
		DCMModel model = new DCMModel();
		if (optData.isPresent()) {
			try {
				DataInputStream data = optData.get();
				model.version = data.readFloat();
				model.author = data.readUTF();
				model.textureWidth = data.readInt();
				model.textureHeight = data.readInt();
				model.children = readCubes(data, model);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.error("No resource found for model " + identifier.toString());
		}
		return model;
	}

	private static ArrayList<DCMCube> readCubes(DataInputStream data, DCMModel model) throws IOException {
		ArrayList<DCMCube> cubes = new ArrayList<>();
		int amount = data.readInt();
		for (int i = 0; i < amount; i++) {
			cubes.add(new DCMCube(
					data.readUTF(), //Name
					new Vector3i(data.readInt(), data.readInt(), data.readInt()), //Dimensions
					new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Rotation Point
					new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Offset
					new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Rotation
					new Vector2i(data.readInt(), data.readInt()), //Texture Offset
					data.readBoolean(), //Texture Mirrored
					new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Cube Grow
					readCubes(data, model), //Children
					model
			));
		}
		return cubes;
	}

	private static class DCMCube {

		String name;
		Vector3i dimensions;
		Vector3f rotationPoint;
		Vector3f offset;
		Vector3f rotation;
		Vector2i textureOffset;
		boolean textureMirrored;
		Vector3f scale;
		ArrayList<DCMCube> children;
		DCMModel model;

		public DCMCube(String name, Vector3i dimensions, Vector3f rotationPoint, Vector3f offset, Vector3f rotation,
					   Vector2i textureOffset, boolean textureMirrored, Vector3f scale, ArrayList<DCMCube> children,
					   DCMModel model) {
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
		}
	}
}
