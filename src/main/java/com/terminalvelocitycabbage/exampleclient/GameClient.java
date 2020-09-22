package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.terminalvelocityrenderer.InputHandler;

public class GameClient extends ClientBase {

	public static GameClient instance;
	public static ClientRenderer clientRenderer;
	public static InputHandler inputHandler;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		instance = this;
		clientRenderer = new ClientRenderer();
		inputHandler = new GameInputHandler();
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

	public static ClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	@Override
	public void init() {
		//super.init();
		clientRenderer.init(inputHandler);
	}

	@Override
	public void start() {
		//super.start();
		clientRenderer.run();
	}
}
