package com.redstudios.tiler;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class panelListener extends InputListener {

	private ScrollPane panel;
	
	public panelListener(ScrollPane from) {
		panel = from;
	}
	
	@Override
	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		if(pointer == 0) {
			System.out.println("x : "+x+" ,y : "+y);
			panel.getChild(0).moveBy(x, y);
			panel.updateVisualScroll();
		}
	
	}
	
	@Override
	public boolean scrolled (InputEvent event, float x, float y, float amountX, float amountY) {
		panel.getChild(0).setScale(amountX, amountY);
		return false;
	}
	
	
}
