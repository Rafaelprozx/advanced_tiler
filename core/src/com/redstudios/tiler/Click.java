package com.redstudios.tiler;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

public interface Click<T> {
	void click(final InputEvent p0, final float p1, final float p2, @Null final T p3);
	public static class To{
		public static ClickListener Listener(@SuppressWarnings("rawtypes") final Click e,final Object herencia) {
			return new ClickListener(){
				@SuppressWarnings("unchecked")
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
			        e.click(event, x, y, herencia);
			    }};}
		public static ClickListener Listener(final Click<Object> e) {
			return new ClickListener(){
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
			        e.click(event, x, y, null);
			    }};}
		public static ClickListener RightListener(@SuppressWarnings("rawtypes") final Click e,final Object herencia) {
			return new ClickListener(Buttons.RIGHT){
				@SuppressWarnings("unchecked")
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
			        e.click(event, x, y, herencia);
			    }};}
		public static ClickListener RightListener(final Click<Object> e) {
			return new ClickListener(Buttons.RIGHT){
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
			        e.click(event, x, y, null);
			    }};}
	}
}
