package io.semillita.virus.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Editor extends JFrame {

	public Editor() {
		super();
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setSize(new Dimension(600, 600));
		
		Container c = super.getContentPane();
		c.setLayout(null);
		
		JTextField field1 = new InputField();
		
		super.setVisible(true);
	}
	
}
