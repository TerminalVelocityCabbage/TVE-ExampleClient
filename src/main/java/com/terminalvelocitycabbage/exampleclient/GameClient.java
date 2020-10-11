package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;

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

		//TODO figure out why this doesn't work
		//ModelLoaderDCM.load(ASSETS_ROOT_RESOURCE_MANAGER, new Identifier(ID, "model/testmodel.dcm"));
	}

	@Override
	public void start() {
		//super.start();
		clientRenderer.run();
	}
}
