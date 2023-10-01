package com.redstudios.tiler;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.io.File;
import com.badlogic.gdx.ApplicationAdapter;

public class TilerClass extends ApplicationAdapter implements InputProcessor{

	File from;
    File to;
    int largo;
    int alto;
    int actual;
    int limit;
    int cuadro;
    Stage st;
    Label text;
    Label counter;
    Pixmap map;
    Pixmap result;
    Pixmap empty;
    Pixmap transparent;
    Pixmap[] container;
    Table buttons_base;
    Table buttons_map;
    Table menu_map;
    ScrollPane panel1;
    ScrollPane panel2;
    PixmapButton[] buttons_a;
    PixmapButton[] buttons_b;
    TextButton save;
    TextButton edit;
    TextButton exit;
    Image separator;
    Image separator2;
    Image saver;
    Container<Table> cs;
    boolean editando;
    boolean created;
    Rectangle panel1r,panel2r;
    
    public TilerClass(final File a, final File b, final int c, final int d) {
        this.editando = false;
        this.created = false;
        this.from = a;
        this.to = b;
        this.largo = c;
        this.alto = d;
    }
    
    public void create() {
        actual = 0;
        limit = 1;
        st = new Stage();
        Label.LabelStyle styleText = new Label.LabelStyle(new BitmapFont(),null);
        text = new Label("Manejando textura...", styleText);
        counter = new Label("", styleText);
        save = new TextButton("guardar", buttonStyle(false));
        exit = new TextButton("Salir", buttonStyle(false));
        edit = new TextButton("Editar", buttonStyle(true));
        buttons_base = new Table();
        buttons_map = new Table();
        menu_map = new Table();
        panel1 = new ScrollPane(buttons_base, scrollPanelStyle());
        panel2 = new ScrollPane(buttons_map, scrollPanelStyle());
        cs = new Container<Table>(menu_map);
        cs.setBackground(draw("ui/saver.png"));
        separator = new Image(draw("ui/separator.png"));
        separator2 = new Image(draw("ui/hseparator.png"));
        button_manage();
        int padRight = 20;
        menu_map.add(save).padRight(padRight);
        menu_map.add(edit).padRight(padRight);
        menu_map.add(exit).padRight(padRight);
        st.addActor(text);
        st.addActor(panel1);
        st.addActor(panel2);
        st.addActor(separator2);
        st.addActor(cs);
        st.addActor(separator);
        st.addActor(counter);
        counter.setPosition(400.0f, 10.0f);
        counter.setVisible(false);
        cs.setBounds(0.0f, 420.0f, 800.0f, 50.0f);
        separator.setBounds(398.0f, 20.0f, 4.0f, 401.0f);
        separator2.setBounds(0.0f, 17.0f, 800.0f, 4.0f);
        panel1.setBounds(0.0f, 20.0f, 398.0f, 400.0f);
        panel1r = new Rectangle(0, 40, 398, 400);
        panel2.setBounds(402.0f, 20.0f, 398.0f, 400.0f);
        panel2r = new Rectangle(402, 40, 398, 400);
        panel2.setVisible(false);
        
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(this);
        multi.addProcessor(st);
        Gdx.input.setInputProcessor(multi);
        Gdx.app.postRunnable(TextureManage());
    }
    
    public void button_manage() {
        this.save.addListener(Click.To.Listener(new Click<Object>(){

			@Override
			public void click(InputEvent p0, float p1, float p2, Object p3) {
				if (!TilerClass.this.editando) {
		            PixmapIO.writePNG(new FileHandle(to), result);
		            text.setText("Guardado");
		        }
		        else {
		            TilerClass.this.text.setText("Guardando...");
		            Gdx.app.postRunnable(save_Map_and_Reload());
		        }}}));
        this.exit.addListener((EventListener)Click.To.Listener(new Click<Object>() {
			@Override
			public void click(InputEvent p0, float p1, float p2, Object p3) {
				Gdx.app.exit();	
			}}));
        this.edit.addListener((EventListener)Click.To.Listener(new Click<Object>(){
			@Override
			public void click(InputEvent p0, float p1, float p2, Object p3) {
				editando = !editando;
		        if (editando) {
		            counter.setVisible(true);
		            counterSet();
		            text.setText((CharSequence)"Edicion Habilitada");
		            if (!created) {
		                Gdx.app.postRunnable(MapButtonMaker(false));
		            }
		            panel2.setVisible(true);
		            panel2.setLayoutEnabled(true);
		        }
		        else {
		            text.setText((CharSequence)"Edicion deshabilitada");
		            panel2.setVisible(false);
		            panel2.setLayoutEnabled(false);
		            counter.setVisible(false);
		        }}}));
    }
    
    private static TextButton.TextButtonStyle buttonStyle(final boolean checkable) {
        final TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(draw("ui/button1.png"), draw("ui/button2.png"), (checkable ? draw("ui/button2.png") : null), new BitmapFont());
        return tbs;
    }
    
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1.0f);
        st.act();
        st.draw();
    }
    
    private Runnable TextureManage() {
        return new Runnable(){
        	@Override
            public void run() {
                print("Preparando...");
                FileHandle ubi = new FileHandle(from);
                Texture texture = new Texture(ubi);
                texture.getTextureData().prepare();
                map = texture.getTextureData().consumePixmap();
                final boolean check = map.getWidth() % largo == 0 && map.getHeight() % alto == 0;
                if (check) {
                    Gdx.app.postRunnable(Map_Creator());
                }
                else {
                    text.setText((CharSequence)"El tamaño de baldosa no coincide con la imagen");
                }
            }
        };
    }
    
    private Runnable Map_Creator() {
        return new Runnable() {
        	@Override
            public void run() {
                Pixmap[][] tileset = new Pixmap[map.getHeight() / alto][map.getWidth() / largo];
                print("Columnas : " + map.getHeight() / alto);
                print("Lineas : " + map.getWidth() / largo);
                print("Cantidad de carga : " + map.getHeight() / alto * (map.getWidth() / largo));
                for (int za = 0; za < tileset.length; ++za) {
                    for (int zb = 0; zb < tileset[0].length; ++zb) {
                        tileset[za][zb] = new Pixmap(largo, alto, map.getFormat());
                    }
                }
                print("Mapa limpio creado");
                for (int za = 0; za < tileset.length; ++za) {
                    for (int zb = 0; zb < tileset[0].length; ++zb) {
                        tileset[za][zb].drawPixmap(map, 0, 0, zb * largo, za * alto, map.getWidth(), map.getHeight());
                    }
                }
                print("mapa cargado");
                TilerClass.this.container = new Pixmap[tileset.length * tileset[0].length];
                for (int za = 0; za < tileset.length; ++za) {
                    for (int zb = 0; zb < tileset[0].length; ++zb) {
                        TilerClass.this.container[za * tileset[0].length + zb] = tileset[za][zb];
                    }
                }
                empty = empty_pixmap();
                transparent = Transparent_pixmap(largo, alto);
                Gdx.app.postRunnable(Map_alineator(false));
            }
        };
    }
    
    private Runnable Map_alineator(final boolean from_result) {
        return new Runnable() {
        	@Override
            public void run() {
                if (from_result) {
                    final Pixmap[][] tileset = new Pixmap[result.getHeight() / alto][result.getWidth() / largo];
                    print("tileset creado");
                    for (int za = 0; za < tileset.length; ++za) {
                        for (int zb = 0; zb < tileset[0].length; ++zb) {
                            (tileset[za][zb] = new Pixmap(largo, alto, map.getFormat())).drawPixmap(result, 0, 0, zb * largo, za * alto, result.getWidth(), result.getHeight());
                        }
                    }
                    print("tileset dividido");
                    container = new Pixmap[cuadro * cuadro];
                    for (int y = 0; y < cuadro; ++y) {
                        for (int x = 0; x < cuadro; ++x) {
                            container[y * cuadro + x] = tileset[y][x];
                        }
                    }
                    print("tileset cargado para alinearse");
                }
                print("Total de Texturas cargadas antes de limpiar : " + TilerClass.this.container.length);
                Array<Pixmap> perfectedMap = new Array<Pixmap>();
                int z1 = 0;
                if (!from_result) {
                    perfectedMap.add(container[0]);
                    for (int x = 0; x < container.length; ++x) {
                        boolean add = true;
                        for (int y2 = 0; y2 < perfectedMap.size; ++y2) {
                            if (ComparePixmap(TilerClass.this.container[x], (Pixmap)perfectedMap.get(y2))) {
                                add = false;
                                ++z1;
                            }
                        }
                        if (add) {
                            perfectedMap.add(container[x]);
                        }
                    }
                }
                else {
                    for (int i = 0; i < container.length; i++) {
                        perfectedMap.add(container[i]);
                    }
                }
                print("bucles en comparacion : " + z1);
                print("Total de texturas : " + perfectedMap.size);
                cuadro = from_result ? cuadro : cuadro_de_tileset(perfectedMap.size);
                print("creando cuadro de : " + cuadro + " * " + cuadro);
                result = new Pixmap(largo * cuadro, alto * cuadro, map.getFormat());
                int z2 = 0;
                for (int x2 = 0; x2 < cuadro; ++x2) {
                    for (int y2 = 0; y2 < cuadro && z2 < perfectedMap.size; ++y2) {
                        result.drawPixmap(perfectedMap.get(z2++), y2 * largo, x2 * alto);
                    }
                }
                limit = z2;
                text.setText("Textura Cargada");
                print("Carga finalizada");
                container = perfectedMap.toArray(Pixmap.class);
                Gdx.app.postRunnable(ButtonMaker(from_result));
            }
        };
    }
    
    private Runnable ButtonMaker(final boolean next) {
        return new Runnable(){
        	@Override
            public void run() {
                if (next) {
                    buttons_base.clearChildren();
                }
                int z = 0;
                buttons_a = new PixmapButton[container.length];
                for (int y = 0; y < cuadro && z < container.length; y++) {
                    for (int x = 0; x < TilerClass.this.cuadro && z < container.length; x++) {
                        buttons_a[z] = PixmapButton.from(z + 1, container[z]).setInternalPosition(x, y).conected();
                        buttons_a[z].setSize(alto,largo);
                        buttons_base.add(buttons_a[z].conected());
                        if(ComparePixmap(container[z], transparent)){
                        	buttons_a[z].untouchable();
                        }
                        z++;
                    }
                    buttons_base.row();
                }
                for (int a = 0; a < buttons_a.length; ++a) {
                    buttons_a[a].addListener(Click.To.Listener(buttonClick(), a));
                    buttons_a[a].addListener(Click.To.RightListener(new Click<Integer>(){

						@Override
						public void click(InputEvent p0, float p1, float p2, Integer herencia) {
							if (buttons_a[herencia].isLocked()) {
					            for (int i = 0; i < buttons_b.length; ++i) {
					                if (buttons_b[i].getNumber() == herencia + 1) {
					                	buttons_b[i].unsetValue();
					                    unset_button(herencia+1);
					                    break;
					                }
					            }
					        }
							counterSet();
						}}, a));
                }
                TilerClass.this.buttons_a[0].setChecked(true);
                TilerClass.this.actual = 1;
                if (next) {
                    Gdx.app.postRunnable(MapButtonMaker(next));
                }
            }
        };
    }
    
    private Runnable MapButtonMaker(final boolean next) {
        return new Runnable() {
        	@Override
            public void run() {
                if (next) {
                    buttons_map.clearChildren();
                }
                int z = 0;
                buttons_b = new PixmapButton[cuadro * cuadro];
                for (int y = 0; y < cuadro; ++y) {
                    for (int x = 0; x < cuadro; ++x) {
                        (buttons_b[z] = PixmapButton.from(empty).setInternalPosition(x, y)).setSize(alto,largo);
                        buttons_b[z].addListener(Click.To.Listener(new Click<Integer>(){

							@Override
							public void click(InputEvent p0, float p1, float p2, Integer herencia) {
								if (editando) {
						            buttons_b[herencia].setChecked(true);
						            if (!buttons_a[actual - 1].isLocked()) {
						                if (buttons_b[herencia].hasValue() && buttons_b[herencia].getNumber() != actual) {
						                    unset_button(buttons_b[herencia].getNumber());
						                }
						                buttons_b[herencia].setTextureCheck(container[actual - 1]);
						                buttons_b[herencia].setNumber(actual);
						                buttons_a[actual - 1].lock();
						            }
						            else if (buttons_b[herencia].hasValue() && buttons_a[actual - 1].getNumber() == buttons_b[herencia].getNumber()) {
						                buttons_a[actual - 1].unlock();
						                buttons_b[herencia].unsetValue();
						                buttons_b[herencia].setTextureCheck(empty);
						            }
						        }
								counterSet();
							}}, z));
                        buttons_b[z].addListener(Click.To.RightListener(new Click<Integer>(){

							@Override
							public void click(InputEvent p0, float p1, float p2, Integer herencia) {
								if (buttons_b[herencia].hasValue()) {
						            unset_button(buttons_b[herencia].getNumber());
						            buttons_b[herencia].unsetValue();
						            buttons_b[herencia].setTextureCheck(empty);
						        }
								counterSet();
							}}, z));
                        buttons_map.add(buttons_b[z++]);
                    }
                    buttons_map.row();
                }
                created = true;
            }
        };
    }
    
    private Runnable save_Map_and_Reload() {
        return new Runnable() {
        	@Override
            public void run() {
                Pixmap temporal = new Pixmap(largo * cuadro, alto * cuadro, map.getFormat());
                int z = 0;
                for (int x = 0; x < cuadro; x++) {
                    for (int y = 0; y < cuadro && z < buttons_b.length; y++) {
                        temporal.drawPixmap((buttons_b[z].getNumber() != 0) ? container[buttons_b[z].getNumber() - 1] : transparent, y * largo, x * alto);
                        z++;
                    }
                }
                print("bucles de pixmap : "+z);
                result = temporal;
                PixmapIO.writePNG(new FileHandle(to), result);
                text.setText((CharSequence)"Guardado");
                Gdx.app.postRunnable(Map_alineator(true));
            }
        };
    }
    
    private Click<Integer> buttonClick(){
    	return new Click<Integer>() {

    		 @Override
             public void click(final InputEvent event, final float x, final float y, final Integer herencia) {
                 for (int length = buttons_a.length, i = 0; i < length; ++i) {
                     final PixmapButton p = buttons_a[i];
                     if (herencia + 1 != p.getNumber()) {
                         p.setChecked(false);
                     }
                 }
                 buttons_a[herencia].setChecked(true);
                 actual = herencia + 1;
                 counterSet();
             }
    };}
    
    private void counterSet() {
    	counter.setText("Tiles disponibles : " + get_actual_tiles());
    }
    
    private int get_actual_tiles() {
        int count = this.buttons_a.length;
        PixmapButton[] buttons_a;
        for (int length = (buttons_a = this.buttons_a).length, i = 0; i < length; ++i) {
            final PixmapButton b = buttons_a[i];
            count = (b.isLocked() ? (count - 1) : count);
        }
        return count;
    }
    
    private void unset_button(final int x) {
        PixmapButton[] buttons_a;
        for (int length = (buttons_a = this.buttons_a).length, i = 0; i < length; ++i) {
            final PixmapButton a = buttons_a[i];
            if (a.getNumber() == x) {
                a.setChecked(false);
                a.unlock();
                break;
            }
        }
    }
    
    private boolean ComparePixmap(final Pixmap a, final Pixmap b) {
        for (int x = 0; x < a.getHeight(); ++x) {
            for (int y = 0; y < a.getWidth(); ++y) {
                if (a.getPixel(x, y) != b.getPixel(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private int cuadro_de_tileset(int total_texturas) {
        for (int x = 2; x < total_texturas; ++x) {
            if (x * x >= total_texturas) {
                return x;
            }
        }
        return 1;
    }
    
    private static Pixmap empty_pixmap() {
        final Texture t = new Texture(Gdx.files.internal("ui/empty_button.png"));
        t.getTextureData().prepare();
        return t.getTextureData().consumePixmap();
    }
    
    private static Pixmap Transparent_pixmap(final int largo, final int alto) {
        return new Pixmap(largo, alto, Pixmap.Format.Alpha);
    }
    
    public void dispose() {
    }
    
    private static ScrollPane.ScrollPaneStyle scrollPanelStyle() {
        return new ScrollPane.ScrollPaneStyle(draw("ui/panel.png"), draw("ui/hscroll.png"), draw("ui/hscrollKnob.png"), draw("ui/vscroll.png"), draw("ui/vscrollKnob.png"));
    }
    
    private static TextureRegionDrawable draw(final String s) {
        return new TextureRegionDrawable(new Texture(Gdx.files.internal(s)));
    }
    
    private static void print(final String line) {
        System.out.println(line);
    }

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}
	
	private int actualUp() {
		int linealpos = actual % cuadro;
		int limitCuadrand = (limit / cuadro) * cuadro;
		if(actual > cuadro) {
			return actual - cuadro - 1;
		}else {
			int pointer = limitCuadrand + linealpos;
			if(pointer > limit) {
				return limitCuadrand - (cuadro - linealpos) - 1;
			}else {
				return pointer-1;
			}
		}
	}
	
	private int actualDown() {
		int pointer = (actual + cuadro) - 1;
		if(pointer < limit) {
			return pointer;
		}else {
			int linealpos = actual % cuadro;
			return linealpos == 0?cuadro-1:linealpos-1;
		}
	}
	
	private int actualLeft() {
		int pointer = actual-2;
		if(pointer < 0) {
			return limit-1;
		}
		return pointer;
	}
	
	private int actualRight() {
		int pointer = actual;
		if(pointer > limit-1) {
			return 0;
		}
		return pointer;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.W || keycode == Keys.UP) {
			buttonClick().click(null, 0, 0, actualUp());
		}
		if(keycode == Keys.S || keycode == Keys.DOWN) {
			buttonClick().click(null, 0, 0, actualDown());
		}
		if(keycode == Keys.A || keycode == Keys.LEFT) {
			buttonClick().click(null, 0, 0, actualLeft());
		}
		if(keycode == Keys.D || keycode == Keys.RIGHT) {
			buttonClick().click(null, 0, 0, actualRight());
		}
		if(keycode == Keys.PERIOD) {
			print("key : "+actual);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private float scrollpoint = 1;

	@Override
	public boolean scrolled(float amountX, float amountY) {
		scrollpoint = Math.max(0.5f, Math.min(scrollpoint + (amountY / 10), 3));
		resize_from_scroll(scrollpoint);
		print("scrolldata = "+scrollpoint);
		return false;
	}
	
	private void resize_from_scroll(float amount) {
		for(PixmapButton a:buttons_a) {
			a.setSize(largo * scrollpoint, alto * scrollpoint);
		}
	}
	
    
}
