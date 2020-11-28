package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.TextGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UISceneHandler;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;

import java.util.ArrayList;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.FONT;

public class GameClientHud extends UISceneHandler {

	@Deprecated
	private TextGameObject testTextObject;
	@Deprecated
	private ArrayList<TextGameObject> textGameObjects = new ArrayList<>();

	@Deprecated
	public GameClientHud(Window window, String statusText) {
		super(window);
		try {
			//this.testTextObject = new TextGameObject(statusText, new FontTexture(new Font("Roboto", Font.PLAIN, 20), "ISO-8859-1"));
			this.testTextObject = new TextGameObject(statusText, new FontTexture(FONT, new Identifier(GameClient.ID, "SourceSansPro-Bold.ttf"), 18f));
			textGameObjects.add(testTextObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public void updateSize(Window window) {
		this.testTextObject.setPosition(10, window.height() - 50f, 0);
	}

	public ArrayList<TextGameObject> getTextGameObjects() {
		return textGameObjects;
	}

	@Deprecated
	public void setText(int index, String text) {
		textGameObjects.get(index).setText(text);
	}
}
