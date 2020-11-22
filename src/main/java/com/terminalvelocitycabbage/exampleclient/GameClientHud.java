package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import com.terminalvelocitycabbage.engine.entity.TextGameObject;

import java.awt.*;
import java.util.ArrayList;

public class GameClientHud {

	private static final Font FONT = new Font("Roboto", Font.PLAIN, 20);
	private static final String CHARSET = "ISO-8859-1";

	private TextGameObject testTextObject;
	private ArrayList<TextGameObject> textGameObjects = new ArrayList<>();

	public GameClientHud(String statusText) {
		try {
			this.testTextObject = new TextGameObject(statusText, new FontTexture(FONT, CHARSET));
			textGameObjects.add(testTextObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSize(Window window) {
		this.testTextObject.setPosition(10, window.getHeight() - 50f, 0);
	}

	public ArrayList<TextGameObject> getTextGameObjects() {
		return textGameObjects;
	}

	public void setText(int index, String text) {
		textGameObjects.get(index).setText(text);
	}
}
