package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.state.State;
import com.terminalvelocitycabbage.engine.client.state.StateHandler;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class GameClient extends ClientBase {

	public static final String ID = "testclient";

	private static GameClientRenderer clientRenderer;
	public StateHandler stateHandler;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		instance = this;
		clientRenderer = new GameClientRenderer(1900, 1000, "TerminalVelocityEngine Test Game!", 20f);
		this.stateHandler = new StateHandler();
		addEventHandler(EventContext.CLIENT, new GameEventHandler());
		init();
		start();
	}

	public static void main(String[] args) {
		new GameClient();
	}

	public static GameClient getInstance() {
		return (GameClient)instance;
	}

	public static GameClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	@Override
	public void init() {
		//super.init();

		//Add an example state, we will use this to determine in the renderer whether the ExampleCanvas should be shown
		stateHandler.addState(new State("example"));
		clientRenderer.init();
	}

	@Override
	public void start() {
		//super.start();
		clientRenderer.run();
	}
}
