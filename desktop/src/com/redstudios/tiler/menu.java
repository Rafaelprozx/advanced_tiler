package com.redstudios.tiler;

import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class menu {

	private JFrame frame;
	private File archivo;
	private size_selector sec;
	
	public JFrame getFrame() {
		return frame;
	}
	
	public File archivo() {
		return archivo;
	}
	
	public size_selector size(){
		return sec;
	}
	
	public menu(MouseClickListener list) {
		initialize(list);
	}

	
	private void initialize(MouseClickListener action) {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 160);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Extractor de Tileset");
		lblNewLabel.setBounds(156, 11, 108, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JButton select = new JButton("Elegir fondo");
		select.setBounds(156, 46, 89, 23);
		select.addMouseListener(new MouseClickListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = file_selector();
				if(chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION){
					archivo = chooser.getSelectedFile();
					sec = new size_selector(action);
					sec.setVisible(true);
				}
			}});
		frame.getContentPane().add(select);
		
		JButton exit = new JButton("Salir");
		exit.setBounds(156, 80, 89, 23);
		exit.addMouseListener(new MouseClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}});
		frame.getContentPane().add(exit);
	}

	private JFileChooser file_selector() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Archive","jpg","gif","png","jpeg");
		chooser.setFileFilter(filter);
		return chooser;
	}
}
