package com.redstudios.tiler;

import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.redstudios.tiler.TilerClass;

@SuppressWarnings("unused")
public class DesktopLauncher {
	
	static int width=32,height=32;
	static menu frame;
	static File savepos;
	static JFileChooser saver;
	
	public static void main (String[] arg) {
		frame = new menu(new MouseClickListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				width = frame.size().get_width_int();
				height = frame.size().get_height_int();
				frame.size().dispose();
				saver = save(frame.archivo());
				if(saver.showSaveDialog(frame.getFrame()) == JFileChooser.APPROVE_OPTION) {
					frame.getFrame().dispose();
					change(frame.archivo(),saver.getSelectedFile(),width,height);
				}
			}});
		frame.getFrame().setVisible(true);
	}
	
	public static Lwjgl3Application change(File fromFile, File toFile, int a, int b){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800, 470);
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.setTitle("BgTiler");
		return new Lwjgl3Application(new TilerClass(fromFile,toFile,a,b), config);
	}
	
	public static JFileChooser save(File original){
		JFileChooser fc = new JFileChooser();
		File nw = new File(original.getParent() + "/tileset_" + original.getName());
		fc.setSelectedFile(nw);
		fc.setFileFilter(ImageFilter()); 
		return fc;	
	}
	
	public static FileFilter ImageFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return false;
				}else{
					String[] accepts = new String[]{".png"};
					
					for(String ext:accepts){
						if(f.getName().toLowerCase().endsWith(ext)){
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public String getDescription() {
				return "Archivo PNG *.png";
			}};
	}
}

