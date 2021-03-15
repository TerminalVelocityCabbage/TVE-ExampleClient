package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.state.State;
import com.terminalvelocitycabbage.engine.client.state.StateHandler;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.debug.Logger;
import com.terminalvelocitycabbage.engine.debug.SystemInfo;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class GameClient extends ClientBase {

	public static final String ID = "testclient";

	private static GameClientRenderer clientRenderer;
	public StateHandler stateHandler;

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient() {
		super(new Logger(ID));
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
		super.init();

		//Add an example state, we will use this to determine in the renderer whether the ExampleCanvas should be shown
		stateHandler.addState(new State("example"));
		clientRenderer.init();

		//Print the system information
		Log.debug("[System Information][CPU]    " + SystemInfo.getAvailableProcessors() + " logical processors");
		Log.debug("[System Information][MEMORY] " + Math.round(SystemInfo.getFreeMemory()/256.0/102.4) / 10d + "GB free of "
				+ Math.round(SystemInfo.getMaxMemory()/256.0/102.4) /10d + "GB system memory. limit: "
				+ SystemInfo.getAllocatedMemory() + "GB");
		Log.debug("[System Information][ARCH]   " + SystemInfo.getArchitecture());
		Log.debug("[System Information][OS]     " + SystemInfo.getOSName() + " version "
				+ SystemInfo.getOSVersion());
		Log.debug("[System Information][GPU]    " + SystemInfo.getGpuVendor() + " model "
				+ SystemInfo.getGpuModel() + " version");
		Log.debug("[System Information][GPU]    " + SystemInfo.getGpuVersion());
	}

	@Override
	public void start() {
		super.start();
		clientRenderer.run();
	}
}
