package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.state.State;
import com.terminalvelocitycabbage.engine.client.state.StateHandler;
import com.terminalvelocitycabbage.engine.debug.Logger;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class GameClient extends ClientBase {

	public static final String ID = "testclient";

	public StateHandler stateHandler;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		super(new Logger(ID), new GameClientRenderer(1900, 1000, "TerminalVelocityEngine Test Game!", 50f));
		instance = this;
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

	public static GameClientRenderer getRenderer() {
		return (GameClientRenderer) ClientBase.getRenderer();
	}

	@Override
	public void init() {
		super.init();

		//Add an example state, we will use this to determine in the renderer whether the ExampleCanvas should be shown
		stateHandler.addState(new State("example"));
		stateHandler.addState(new State("normals"));
		getRenderer().init();
	}

	@Override
	public void start() {
		super.start();
		getRenderer().run();
		getLogger().createLog(false);
	}
}
