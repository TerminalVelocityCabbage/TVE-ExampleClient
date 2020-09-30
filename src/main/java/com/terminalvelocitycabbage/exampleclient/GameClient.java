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
		clientRenderer = new GameClientRenderer(800, 600, "TerminalVelocityEngine Test Game!");
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
	}

	@Override
	public void start() {
		//super.start();
		clientRenderer.run();
	}
}
