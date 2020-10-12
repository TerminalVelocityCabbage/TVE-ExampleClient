package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.exampleclient.models.loader.DCModel;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;

public class GameClient extends ClientBase {

	public static final String ID = "testclient";

	private static GameClient instance;
	private static GameClientRenderer clientRenderer;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		instance = this;
		clientRenderer = new GameClientRenderer(1900, 1000, "TerminalVelocityEngine Test Game!");
		addEventHandler(new GameEventHandler());
		init();
		start();
	}

	public static void main(String[] args) {
		new GameClient();
	}

	public static GameClient getInstance() {
		return instance;
	}

	public static GameClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	@Override
	public void init() {
		//super.init();
		clientRenderer.init();

		DCModel model = DCModel.Loader.load(ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(ID, "model/Gerald.dcm"));
		Log.warn(model.getCube("forearm_R").getName());
		Log.info(model.toString());
	}

	@Override
	public void start() {
		//super.start();
		clientRenderer.run();
	}
}
