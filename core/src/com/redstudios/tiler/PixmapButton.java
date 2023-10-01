package com.redstudios.tiler;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class PixmapButton extends ImageButton {

	private boolean hasValue;
    private int value;
    private int iposX;
    private int iposY;
    private boolean locked;
    private boolean untouchable;
    private ImageButton.ImageButtonStyle old;
    
    public PixmapButton(final int number, final ImageButton.ImageButtonStyle style) {
        super(style);
        this.hasValue = false;
        this.value = 0;
        this.iposX = 0;
        this.iposY = 0;
        this.locked = false;
        this.untouchable = false;
        this.value = number;
        this.old = style;
    }
    
    public PixmapButton conected() {
        this.hasValue = true;
        return this;
    }
    
    public PixmapButton untouchable() {
    	this.untouchable = true;
    	lock();
    	return this;
    }
    
    public static PixmapButton from(final int value, final Pixmap p) {
        final TextureRegionDrawable a = toDraw(p);
        final ImageButton.ImageButtonStyle stl = new ImageButton.ImageButtonStyle((Drawable)a, a.tint(new Color(127)), a.tint(new Color(-129)), (Drawable)null, (Drawable)null, (Drawable)null);
        return new PixmapButton(value, stl);
    }
    
    public static PixmapButton from(final Pixmap p) {
        final TextureRegionDrawable a = toDraw(p);
        final ImageButton.ImageButtonStyle stl = new ImageButton.ImageButtonStyle((Drawable)a, a.tint(new Color(127)), (Drawable)a, (Drawable)null, (Drawable)null, (Drawable)null);
        return new PixmapButton(0, stl);
    }
    
    public void setTextureCheck(final Pixmap p) {
        this.getStyle().checked = (Drawable)toDraw(p);
    }
    
    public void tint_color(final int color) {
        final TextureRegionDrawable a = (TextureRegionDrawable)this.old.up;
        final Color up = new Color(color - 48);
        final Color down = new Color(color - 96);
        final Color check = new Color(color - 144);
        final ImageButton.ImageButtonStyle stl = new ImageButton.ImageButtonStyle(a.tint(up), a.tint(down), a.tint(check), (Drawable)null, (Drawable)null, (Drawable)null);
        this.setStyle((Button.ButtonStyle)stl);
    }
    
    public void tint_in_red() {
        this.tint_color(-16776961);
    }
    
    public void untint() {
        this.setStyle((Button.ButtonStyle)this.old);
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public void lock() {
        this.tint_in_red();
        this.locked = true;
    }
    
    public void unlock() {
    	if(!untouchable) {
        this.untint();
        this.locked = false;}
    }
    
    public boolean hasValue() {
        return this.hasValue;
    }
    
    public void unsetValue() {
        this.setChecked(false);
        this.hasValue = false;
        this.value = 0;
        this.untint();
    }
    
    public PixmapButton setInternalPosition(final int x, final int y) {
        this.iposX = x;
        this.iposY = y;
        return this;
    }
    
    public int getInternalPositionX() {
        return this.iposX;
    }
    
    public int getInternalPositionY() {
        return this.iposY;
    }
    
    private static TextureRegionDrawable toDraw(final Pixmap e) {
        return new TextureRegionDrawable(new Texture(e));
    }
    
    public int getNumber() {
        return this.hasValue ? this.value : 0;
    }
    
    public void setNumber(final int number) {
        this.value = number;
        this.hasValue = true;
    }
	
}
