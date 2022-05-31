package io.semillita.virus.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import io.semillita.virus.Graph;
import io.semillita.virus.Stats;
import io.semillita.virus.world.World;

public class Editor extends JFrame {

	public Editor() {
		super();
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setSize(new Dimension(350, 740));
		super.setLocationRelativeTo(null);
		
		Container c = super.getContentPane();
		c.setLayout(null);
		
		JLabel titleLabel = new JLabel("Virus simulation");
		titleLabel.setBounds(50, 20, 400, 40);
		titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
		c.add(titleLabel);
		
//		JTextField field1 = new JTextField();
//		field1.setBounds(10, 100, 100, 30);
//		c.add(field1);
		
		JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(365, 1, 10_000, 5));
		spinner1.setBounds(10, 100, 100, 30);
		c.add(spinner1);
		
		JLabel label1 = new JLabel("Amount of days");
		label1.setBounds(120, 100, 200, 30);
		c.add(label1);
		
		JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(100, 1, 10_000, 1));
		spinner2.setBounds(10, 140, 100, 30);
		c.add(spinner2);
		
		JLabel label2 = new JLabel("Amount of houses");
		label2.setBounds(120, 140, 200, 30);
		c.add(label2);
		
		JSpinner spinner3 = new JSpinner(new SpinnerNumberModel(2, 1, 10_000, 1));
		spinner3.setBounds(10, 180, 100, 30);
		c.add(spinner3);
		
		JLabel label3 = new JLabel("Houses visited per day");
		label3.setBounds(120, 180, 200, 30);
		c.add(label3);
		
		JSpinner spinner4 = new JSpinner(new SpinnerNumberModel(7, 1, 10_000, 1));
		spinner4.setBounds(10, 220, 100, 30);
		c.add(spinner4);
		
		JLabel label4 = new JLabel("Days infected");
		label4.setBounds(120, 220, 200, 30);
		c.add(label4);
		
		JSpinner spinner5 = new JSpinner(new SpinnerNumberModel(7, 1, 10_000, 1));
		spinner5.setBounds(10, 260, 100, 30);
		c.add(spinner5);
		
		JLabel label5 = new JLabel("Days immune");
		label5.setBounds(120, 260, 200, 30);
		c.add(label5);
		
		JSpinner spinner6 = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		spinner6.setBounds(10, 300, 100, 30);
		c.add(spinner6);
		
		JLabel label6 = new JLabel("Risk of infection (%)");
		label6.setBounds(120, 300, 200, 30);
		c.add(label6);

		JSpinner spinner7 = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		spinner7.setBounds(10, 340, 100, 30);
		c.add(spinner7);
		
		JLabel label7 = new JLabel("Risk of death (%)");
		label7.setBounds(120, 340, 200, 30);
		c.add(label7);
		
		JSpinner spinner8 = new JSpinner(new SpinnerNumberModel(1, 1, 10_000, 1));
		spinner8.setBounds(10, 380, 100, 30);
		c.add(spinner8);
		
		JLabel label8 = new JLabel("Amount that start infected");
		label8.setBounds(120, 380, 200, 30);
		c.add(label8);
		
		JPanel greenPanel = new JPanel();
		greenPanel.setBackground(Color.GREEN);
		greenPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		greenPanel.setBounds(20, 430, 20, 20);
		c.add(greenPanel);
		
		JLabel greenLabel = new JLabel("Healthy but susceptible");
		greenLabel.setBounds(60, 430, 200, 20);
		c.add(greenLabel);
		
		JPanel redPanel = new JPanel();
		redPanel.setBackground(Color.RED);
		redPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		redPanel.setBounds(20, 470, 20, 20);
		c.add(redPanel);
		
		JLabel redLabel = new JLabel("Infected");
		redLabel.setBounds(60, 470, 200, 20);
		c.add(redLabel);
		
		JPanel grayPanel = new JPanel();
		grayPanel.setBackground(Color.GRAY);
		grayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		grayPanel.setBounds(20, 510, 20, 20);
		c.add(grayPanel);
		
		JLabel grayLabel = new JLabel("Immune");
		grayLabel.setBounds(60, 510, 200, 20);
		c.add(grayLabel);
		
		JPanel blackPanel = new JPanel();
		blackPanel.setBackground(Color.BLACK);
		blackPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		blackPanel.setBounds(20, 550, 20, 20);
		c.add(blackPanel);
		
		JLabel blackLabel = new JLabel("Dead");
		blackLabel.setBounds(60, 550, 200, 20);
		c.add(blackLabel);
		
		JButton button = new JButton("Start smilation");
		button.setBounds(65, 610, 200, 40);
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button) {
					new Thread(() -> {
						int amountOfDays = (Integer) spinner1.getValue();
						int amountOfHouses = (Integer) spinner2.getValue();
						int housesPerDay = (Integer) spinner3.getValue();
						int daysInfected = (Integer) spinner4.getValue();
						int daysImmune = (Integer) spinner5.getValue();
						float riskOfInfection = (Integer) spinner6.getValue() / 100f;
						float riskOfDeath = (Integer) spinner7.getValue() / 100f;
						int startInfected = (Integer) spinner8.getValue();
						
						World world = new World(amountOfDays, amountOfHouses, housesPerDay, daysInfected, daysImmune, riskOfInfection, riskOfDeath, startInfected);
						Stats stats = world.simulate();
						
						Graph graph = new Graph(600, 600);
						graph.upload(stats);
						graph.run();
					}).start();
				}
			}
		});
		c.add(button);
		
		
		super.setVisible(true);
	}
	
}
