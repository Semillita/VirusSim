package io.semillita.virus;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

	private Matrix4f projectionMatrix, viewMatrix;
	public Vector2f position;
	
	public Camera(int width, int height) {
		this.position = new Vector2f(0, 0);
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection(width, height);
	}
	
	public void adjustProjection(int width, int height) {
		projectionMatrix.identity();
		projectionMatrix.ortho(0.0f, width, 0.0f, height, 0.0f, 100.0f);
	}
	
	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
				cameraFront.add(position.x, position.y, 0.0f),
				cameraUp);
		return this.viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
}
