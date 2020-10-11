package com.terminalvelocitycabbage.exampleclient.models.loader;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ModelLoaderDCM {

	public static Model load(ResourceManager resourceManager, Identifier identifier) {
		ArrayList<Model.Part> modelParts = new ArrayList<>();

		Optional<DataInputStream> optData = resourceManager.getResource(identifier).flatMap(Resource::asDataStream);
		if (optData.isPresent()) {
			DataInputStream data = optData.get();
			try {
				Log.info(data.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.error("No resource found for model " + identifier.toString());
		}

		return new Model(modelParts);
	}

}
