package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;

public class GameClient extends ClientBase {

	public static GameClient instance;
	public static ClientRenderer clientRenderer;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		instance = this;
		clientRenderer = new ClientRenderer();
		addEventHandler(new EventHandler());
		init();
		start();
	}

	public static void main(String[] args) {
		new GameClient();
	}

	public static GameClient getInstance() {
		return instance;
	}

	@Override
	public void init() {
		super.init();
		clientRenderer.init();
	}

	@Override
	public void start() {
		super.start();
		clientRenderer.run();
	}
}
