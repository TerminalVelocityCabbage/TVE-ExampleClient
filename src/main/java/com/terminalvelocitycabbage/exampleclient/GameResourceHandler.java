package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.client.resources.ClassLoaderResourceManager;
import com.terminalvelocitycabbage.client.resources.ResourceManager;

public class GameResourceHandler {

	public static final ResourceManager ASSETS_ROOT_RESOURCE_MANAGER = new ClassLoaderResourceManager(ClassLoader.getSystemClassLoader(), "assets/");
}
