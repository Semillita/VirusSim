package io.semillita.virus;

import static org.lwjgl.opengl.GL33.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.joml.Vector2f;

public class Renderer {

	private static final int POSITION_SIZE = 2;
	private static final int COLOR_SIZE = 3;

	private static final int POSITION_OFFSET = 0;
	private static final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;

	private static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE;
	private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private final int width, height;
	
	private int vaoID;
	private int vboID;

	private int maxTriangleCount = 20000;
	// private Vertex[] vertices;
	private float[] vertices;
	private Camera camera;
	private Shader shader;

	private int idx;

	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
		
		vertices = new float[maxTriangleCount * 3 * VERTEX_SIZE];

		camera = new Camera(width, height);
		shader = new Shader("/default.glsl");
		shader.compile();

		vaoID = createVAO();

		vboID = createVBO();

		createEBO();

		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);
	}

	public void begin() {
		idx = 0;
	}

	public void drawTriangle(float[] vertices, float[] color) {
		if (idx / 15 >= maxTriangleCount) {
			System.out.println("Filling");
			flush();
		}
		
		// Position
		this.vertices[idx] = vertices[0];
		this.vertices[idx + 1] = vertices[1];
		// Color
		this.vertices[idx + 2] = color[0];
		this.vertices[idx + 3] = color[1];
		this.vertices[idx + 4] = color[2];

		idx += 5;

		// Position
		this.vertices[idx] = vertices[2];
		this.vertices[idx + 1] = vertices[3];
		// Color
		this.vertices[idx + 2] = color[0];
		this.vertices[idx + 3] = color[1];
		this.vertices[idx + 4] = color[2];

		idx += 5;

		// Position
		this.vertices[idx] = vertices[4];
		this.vertices[idx + 1] = vertices[5];
		// Color
		this.vertices[idx + 2] = color[0];
		this.vertices[idx + 3] = color[1];
		this.vertices[idx + 4] = color[2];

		idx += 5;
	}

	public void drawRectangle(float x, float y, float width, float height, float[] color) {
		float[] v1 = {x, y, x, y + height, x + width, y + height};
		float[] v2 = {x, y, x + width, y + height, x + width, y};
		
		drawTriangle(v1, color);
		drawTriangle(v2, color);
	}

	public void end() {
		flush();
	}

	public void flush() {
		fillVertexBuffer(vboID, vertices);
		useShader();
		uploadMatricesToShader(camera, shader);

		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, idx, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);

		glBindVertexArray(0);
		
		idx = 0;
	}

	private void fillVertexBuffer(int vboID, float[] vertices) {
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
	}

	private void uploadMatricesToShader(Camera camera, Shader shader) {
		shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		shader.uploadMat4f("uView", camera.getViewMatrix());
	}

	private void useShader() {
		shader.use();
	}

	private int createVAO() {
		var vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		return vaoID;
	}

	private int createVBO() {
		var vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * VERTEX_SIZE_BYTES, GL_DYNAMIC_DRAW);
		return vboID;
	}

	private void createEBO() {
		int eboID = glGenBuffers();
		int[] indices = IntStream.range(0, maxTriangleCount * 3).toArray();

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}

}
