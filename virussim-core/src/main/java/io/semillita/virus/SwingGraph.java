package io.semillita.virus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingGraph extends JFrame {

	Display display;
	
	public SwingGraph(int width, int height, int margin) {
		super();
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setResizable(false);
		
		display = new Display(width, height, margin);
		
		super.add(display);
		super.pack();
		
		setVisible(true);
	}
	
	public void upload(Stats stats, int index) {
		display.upload(stats, index);
	}
	
	private class Display extends JPanel {
	
		private final int width, height, margin;
		
		private Stats stats;
		
		public Display(int width, int height, int margin) {
			super.setPreferredSize(new Dimension(width + 2 * margin, height + 2 * margin));
			
			this.width = width;
			this.height = height;
			this.margin = margin;
			
			super.repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			var g2d = (Graphics2D) g;
			
			if (stats == null) {
				System.out.println("stats == null");
				return;
			}
			
			final float stepWidth = width / (stats.values.size() - 1);
			
			for (int c = 0; c < stats.values().size() - 1; c++) {
				
				int xStart = (int) (margin + c * stepWidth);
				int xEnd = (int) (xStart + stepWidth);
				
				Color[] colors = {Color.RED, Color.BLUE, Color.GREEN};
				
				var col1 = stats.values().get(c);
				var col2 = stats.values().get(c + 1);
				
				int y1 = margin, y2 = margin;
				
				for (int h = 0; h < stats.levels(); h++) {
					
					g2d.setColor(colors[h]);
					
					var height1 = col1.get(h) * height;
					var height2 = col2.get(h) * height;
					
					int[] xPoints = {xStart, xStart, xEnd, xEnd};
					int[] yPoints = {y1, (int) (y1 + height1), (int) (y2 + height2), y2};
					
					y1 += height1;
					y2 += height2;
					
					g2d.fillPolygon(xPoints, yPoints, 4);
				}
			}
			
//			int[] xPoints = {200, 150, 100};
//			int[] yPoints = {100, 200, 100};
//			
//			g2d.fillPolygon(xPoints, yPoints, 3);
//			
			//System.out.println("Finish paint");
			
			// Draw base
			g2d.setColor(Color.BLACK);
			
			g2d.fillRect(margin, margin, 5, height);
			g2d.fillRect(margin, margin + height - 5, width, 5);
			//
			
		}
		
		public void upload(Stats stats, int index) {
			this.stats = stats;
			
			//System.out.println("Begin paint #" + index);
			super.repaint();
			//System.out.println("Finish paint #" + index);
		}
		
	}
	
	public static record Stats (List<List<Float>> values, int levels) {}
	
}
