package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Canvas;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Container;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Element;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;
import com.terminalvelocitycabbage.engine.debug.Log;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PERCENT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PIXELS;

public class ExampleCanvas extends Canvas {

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
				.onDoubleClick((short)10, () -> Log.info("double")));
		this.addContainer(new Container(new UIDimension(400, PIXELS), new UIDimension(50, PIXELS),
				new Anchor(AnchorPoint.LEFT_MIDDLE, AnchorDirection.RIGHT),
				new Style()
						.setColor(1, 0, 0, 1)
						.marginLeft(10, PIXELS))
				.onClick(() -> GameClient.getInstance().stateHandler.resetState())
				.onRightClick(() -> Log.info("right")));
		this.addContainer(new Container(new UIDimension(400, PIXELS), new UIDimension(40, PERCENT),
				new Anchor(AnchorPoint.BOTTOM_MIDDLE, AnchorDirection.UP),
				new Style()
						.setColor(1, 0, 1, 1)
						.marginBottom(10, PIXELS)
						.setBorderThickness(3)
						.setBorderColor(1, 0, 0, 1))
				.onHover(() -> Log.info("hover"))
				.verticalAlignment(Alignment.Vertical.BOTTOM)
				.alignmentDirection(Alignment.Direction.VERTICAL)
				.addElement(new Element("Some element Text",
						new UIDimension(80, PERCENT), new UIDimension(30, PIXELS),
						new Style()
								.setColor(0, 0, 1, 1)))
				.addElement(new Element("Some element Text also",
						new UIDimension(80, PERCENT), new UIDimension(30, PIXELS),
						new Style()
								.setColor(0, 1, 1, 1))));
	}
}
