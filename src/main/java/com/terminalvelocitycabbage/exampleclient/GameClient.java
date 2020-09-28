package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputHandler;

public class GameClient extends ClientBase {

	public static final String ID = "testclient";

	public static GameClient instance;
	public static GameClientRenderer clientRenderer;
	public static InputHandler inputHandler;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		instance = this;
		clientRenderer = new GameClientRenderer(800, 600, "TerminalVelocityEngine Test Game!");
		inputHandler = new GameInputHandler();
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
		clientRenderer.init(inputHandler);
	}

	@Override
	public void start() {
		//super.start();
		clientRenderer.run();
	}
}
