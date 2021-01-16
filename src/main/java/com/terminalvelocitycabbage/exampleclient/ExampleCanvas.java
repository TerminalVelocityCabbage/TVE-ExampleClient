package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontMeshPartStorage;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontTexture;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Canvas;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Container;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Element;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Text;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.debug.Log;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PERCENT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PIXELS;

public class ExampleCanvas extends Canvas {

	private static final FontMeshPartStorage DEFAULT_FONT = new FontMeshPartStorage(new FontTexture(GameResourceHandler.FONT, new Identifier(GameClient.ID, "SourceSansPro-Regular.ttf")));

	public ExampleCanvas(Window window) {
		super(window);
		this.style
				.marginLeft(100, PIXELS)
				.marginRight(40, PERCENT)
				.marginTop(10, PIXELS)
				.marginBottom(10, PIXELS)
				.setColor(0.3f, 0.4f, 1 ,0.25f)
				.setBorderRadius(15)
				.setBorderColor(1, 1, 1, 1)
				.setBorderThickness(4);
		this.addContainer(new Container(new UIDimension(100, PIXELS), new UIDimension(100, PIXELS),
				new Anchor(AnchorPoint.TOP_MIDDLE, AnchorDirection.RIGHT_DOWN),
				new Style()
						.setColor(1, 1, 0, 1))
				.onDoubleClick((short)10, uiRenderable -> Log.info("double")));
		this.addContainer(new Container(new UIDimension(400, PIXELS), new UIDimension(50, PIXELS),
				new Anchor(AnchorPoint.LEFT_MIDDLE, AnchorDirection.RIGHT),
				new Style()
						.setColor(1, 0, 0, 1)
						.marginLeft(10, PIXELS))
				.onClick(uiRenderable -> GameClient.getInstance().stateHandler.resetState())
				.onRightClick(uiRenderable -> Log.info("right")));
		this.addContainer(new Container(new UIDimension(40, PERCENT), new UIDimension(40, PERCENT),
				new Anchor(AnchorPoint.BOTTOM_MIDDLE, AnchorDirection.UP),
				new Style()
						.setColor(1, 0, 1, 1)
						.marginBottom(10, PIXELS)
						.setBorderThickness(3)
						.setBorderColor(1, 0, 0, 1))
				.onHover(uiRenderable -> uiRenderable.style.setBorderColor(0, 1, 1, 1))
				.onUnHover(uiRenderable -> uiRenderable.style.setBorderColor(1, 0, 0, 1))
				.horizontalAlignment(Alignment.Horizontal.CENTER)
				.verticalAlignment(Alignment.Vertical.CENTER)
				.alignmentDirection(Alignment.Direction.HORIZONTAL)
				.overflow(Overflow.HIDDEN)
				.addElement(new Element(new UIDimension(120, PIXELS), new UIDimension(60, PIXELS),
						new Style()
								.setColor(0, 0, 1, 1))
						.setInnerText(new Text("Some Text", DEFAULT_FONT))
						.onClick(uiRenderable -> ((Element)uiRenderable).updateTextString("Changed Text Test")))
				.addElement(new Element(new UIDimension(120, PIXELS), new UIDimension(60, PIXELS),
						new Style()
								.setColor(0, 1, 1, 1)))
				.addElement(new Element(new UIDimension(120, PIXELS), new UIDimension(60, PIXELS),
						new Style()
								.setColor(1, 0, 0.5f, 1)))
				.addElement(new Element(new UIDimension(120, PIXELS), new UIDimension(60, PIXELS),
						new Style()
								.setColor(0, 1, 0, 1)))
				.addElement(new Element(new UIDimension(120, PIXELS), new UIDimension(60, PIXELS),
						new Style()
								.setColor(1, 1, 0, 1))));

	}
}
