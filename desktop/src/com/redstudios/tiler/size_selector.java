package com.redstudios.tiler;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class size_selector extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField largo;
	private JTextField alto;
	private JButton OKButton;
	
	public int get_width_int(){
		return Integer.parseInt(largo.getText());
	}
	public int get_height_int(){
		return Integer.parseInt(alto.getText());
	}
	
	public size_selector(MouseListener list) {
		setResizable(false);
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Tamaño de baldosa");
			lblNewLabel.setBounds(168, 11, 93, 14);
			contentPanel.add(lblNewLabel);
		}
		
		largo = new JTextField();
		largo.setText("32");
		largo.addKeyListener(onlyNumbers());
		largo.setBounds(106, 36, 86, 20);
		contentPanel.add(largo);
		largo.setColumns(10);
		alto = new JTextField();
		alto.setText("32");
		alto.addKeyListener(onlyNumbers());
		alto.setBounds(225, 36, 86, 20);
		contentPanel.add(alto);
		alto.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("x");
		lblNewLabel_1.setBounds(205, 36, 39, 14);
		contentPanel.add(lblNewLabel_1);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				OKButton = new JButton("OK");
				OKButton.setActionCommand("OK");
				OKButton.addMouseListener(list);
				buttonPane.add(OKButton);
				getRootPane().setDefaultButton(OKButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addMouseListener(new MouseClickListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						dispose();
						
					}});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private static KeyAdapter onlyNumbers() {
		return new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		        char c = e.getKeyChar();
		        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
		            e.consume();  // if it's not a number, ignore the event
		        }
		     }
		};
	}
}
