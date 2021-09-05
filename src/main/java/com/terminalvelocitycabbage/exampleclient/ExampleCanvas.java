package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.ui.*;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.FontInfo;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.FontMaterial;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.FontMeshPartStorage;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.debug.Log;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PERCENT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PIXELS;

public class ExampleCanvas extends Canvas {

	private static final FontMeshPartStorage DEFAULT_FONT =
			new FontMeshPartStorage(
					new FontMaterial(
							FontInfo.read(
									GameResourceHandler.FONT_INFO, new Identifier(GameClient.ID, "source_sans_pro_regular_32.tvfont")),
									GameResourceHandler.FONT_TEXTURE, new Identifier(GameClient.ID, "source_sans_pro_regular_32.png")));

	public ExampleCanvas(Window window) {
		super(window);
		this
				.marginLeft(new AnimatableUIValue(100), PIXELS)
				.marginRight(new AnimatableUIValue(40), PERCENT)
				.marginTop(new AnimatableUIValue(10), PIXELS)
				.marginBottom(new AnimatableUIValue(10), PIXELS)
				.color(0.3f, 0.4f, 1 ,0.25f)
				.borderRadius(15)
				.borderColor(1, 1, 1, 1)
				.borderThickness(4);
		this.addContainer(new Container(new UIDimension(new AnimatableUIValue(100), PIXELS), new UIDimension(new AnimatableUIValue(100), PIXELS),
				new Anchor(AnchorPoint.TOP_MIDDLE, AnchorDirection.RIGHT_DOWN))
				.color(1, 1, 0, 1)
				.onDoubleClick((short)10, uiRenderable -> Log.info("double")));
		this.addContainer(new Container(new UIDimension(new AnimatableUIValue(400), PIXELS), new UIDimension(new AnimatableUIValue(50), PIXELS),
				new Anchor(AnchorPoint.LEFT_MIDDLE, AnchorDirection.RIGHT))
				.color(1, 0, 0, 1)
				.marginLeft(new AnimatableUIValue(10), PIXELS)
				.onClick(uiRenderable -> GameClient.getInstance().stateHandler.resetState())
				.onRightClick(uiRenderable -> Log.info("right")));
		this.addContainer((Container) new Container(new UIDimension(new AnimatableUIValue(40), PERCENT), new UIDimension(new AnimatableUIValue(40), PERCENT),
				new Anchor(AnchorPoint.BOTTOM_MIDDLE, AnchorDirection.UP))
					.horizontalAlignment(Alignment.Horizontal.CENTER)
					.verticalAlignment(Alignment.Vertical.CENTER)
					.alignmentDirection(Alignment.Direction.HORIZONTAL)
				.addElement(new Element(new UIDimension(new AnimatableUIValue(120), PIXELS), new UIDimension(new AnimatableUIValue(60), PIXELS))
						.color(0, 0, 1, 1)
						.setInnerText(new Text("Some Text", DEFAULT_FONT))
						.onClick(uiRenderable -> ((Element)uiRenderable).updateTextString("Changed Text Test")))
				.addElement(new Element(new UIDimension(new AnimatableUIValue(120), PIXELS), new UIDimension(new AnimatableUIValue(60), PIXELS))
						.color(0, 1, 1, 1))
				.addElement(new Element(new UIDimension(new AnimatableUIValue(120), PIXELS), new UIDimension(new AnimatableUIValue(60), PIXELS))
						.color(1, 0, 0.5f, 1))
				.addElement(new Element(new UIDimension(new AnimatableUIValue(120), PIXELS), new UIDimension(new AnimatableUIValue(60), PIXELS))
						.color(0, 1, 0, 1))
				.addElement(new Element(new UIDimension(new AnimatableUIValue(120), PIXELS), new UIDimension(new AnimatableUIValue(60), PIXELS))
						.color(1, 1, 0, 1))
				.overflow(Overflow.HIDDEN)
				.color(1, 0, 1, 1)
				.marginBottom(new AnimatableUIValue(40), PIXELS)
				.borderThickness(3)
				.borderColor(1, 0, 0, 1)
				.onHover(uiRenderable -> uiRenderable.borderColor(0, 1, 1, 1))
				.onUnHover(uiRenderable -> uiRenderable.borderColor(1, 0, 0, 1)));
	}
}
