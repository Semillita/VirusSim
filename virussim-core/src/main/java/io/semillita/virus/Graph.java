package io.semillita.virus;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL;

public class Graph extends Thread {

	private static final float[] RED = { 1, 0, 0 };
	private static final float[] GREEN = { 0, 1, 0 };
	private static final float[] BLUE = { 0, 0, 1 };
	private static final float[] BLACK = { 0, 0, 0 };
	private static final float[] GRAY = { 0.4f, 0.4f, 0.4f };

	static {
		glfwInit();
	}

	private static final int margin = 100;

	private final int width, height;
	private Renderer renderer;
	private Stats stats;

	public Graph(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void run() {
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		// glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		// glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);

		long handle = glfwCreateWindow(width, height, "Graph", 0, 0);
		glfwSetWindowPos(handle, 500, 300);
		glfwSetWindowOpacity(handle, 1f);

		System.out.println(handle);

		glfwMakeContextCurrent(handle);
		GL.createCapabilities();

		renderer = new Renderer(width, height);
		draw(renderer, handle);
		
		while (!glfwWindowShouldClose(handle)) {
			//glClearColor(1, 1, 1, 1);
			//glClear(GL_COLOR_BUFFER_BIT);

			//glfwSwapBuffers(handle);
			glfwSwapInterval(1);
			glfwPollEvents();
		}

		glfwDestroyWindow(handle);
	}

	public void upload(Stats stats) {
		System.out.println(stats);
		this.stats = stats;
	}
	
	private void draw(Renderer renderer, long handle) {
		long startTime = System.nanoTime();

		glClearColor(1, 1, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT);
		
		renderer.begin();
		
		if (stats != null) {
			if (stats.measures().size() > 1) {

				final float stepWidth = (width - 2 * margin) / (stats.measures().size() - 1);

				for (int c = 0; c < stats.measures().size() - 1; c++) {
					int xStart = (int) (margin + c * stepWidth);
					int xEnd = (int) (xStart + stepWidth);

					float[][] colors = { GREEN, RED, GRAY, BLACK };

					var col1 = stats.measures().get(c);
					var col2 = stats.measures().get(c + 1);

					int y1 = margin, y2 = margin;

					for (int h = 0; h < 4; h++) {

						var color = colors[h];

						var height1 = col1.get(h) * (height - 2 * margin);
						var height2 = col2.get(h) * (height - 2 * margin);

						float[] triangle1 = { xStart, y1, xStart, y1 + height1, xEnd, y2 + height2 };
						float[] triangle2 = { xStart, y1, xEnd, y2 + height2, xEnd, y2 };

						y1 += height1;
						y2 += height2;

						renderer.drawTriangle(triangle1, color);
						renderer.drawTriangle(triangle2, color);
					}
				}
			}
		}
		
		renderer.drawRectangle(margin, margin, width - 2 * margin, 5, BLACK);
		renderer.drawRectangle(margin, margin, 5, height - 2 * margin, BLACK);
		renderer.end();

		glfwSwapBuffers(handle);
		
		long endTime = System.nanoTime();

		double timeElapsed = ((endTime - startTime) / 1_000_000_000d);
		
		System.out.println("Rendered in " + timeElapsed + " seconds");
	}

}
