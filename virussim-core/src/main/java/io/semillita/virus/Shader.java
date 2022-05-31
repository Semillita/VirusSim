package io.semillita.virus;

import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {

	private int shaderProgramID;
	private String vertexShaderSource;
	private String fragmentShaderSource;
	private String filepath;

	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			this.filepath = filepath;
			String source = new String(Files.readAllBytes(Paths.get(this.getClass().getResource(filepath).toURI())));
			
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
			
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\r\n", index);
			String firstPattern = source.substring(index, eol).trim();
			
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\r\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if(firstPattern.equals("vertex")) {
				vertexShaderSource = splitString[1];
			} else if(firstPattern.equals("fragment")) {
				fragmentShaderSource = splitString[1];
			} else {
				throw new IOException("Unexpected token: '" + firstPattern + "'");
			}
			
			if(secondPattern.equals("vertex")) {
				vertexShaderSource = splitString[2];
			} else if(secondPattern.equals("fragment")) {
				fragmentShaderSource = splitString[2];
			} else {
				throw new IOException("Unexpected token: '" + firstPattern + "'");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			assert false : "ERROR: Could not open file for shader: '" + filepath + "'";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void compile() {
		int vertexShaderID, fragmentShaderID;
		
		vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderID, vertexShaderSource);
		glCompileShader(vertexShaderID);
		
		var success = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
		if(success == GL_FALSE) {
			int len = glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: Vertex shader compilation failed");
			System.err.println(glGetShaderInfoLog(vertexShaderID, len));
			assert false : "";
		}
		
		fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderID, fragmentShaderSource);
		glCompileShader(fragmentShaderID);
		
		success = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);
		if(success == GL_FALSE) {
			int len = glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: Fragment shader compilation failed");
			System.err.println(glGetShaderInfoLog(fragmentShaderID, len));
			assert false : "";
		}
		
		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexShaderID);
		glAttachShader(shaderProgramID, fragmentShaderID);
		glLinkProgram(shaderProgramID);
		
		success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if(success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: Linking of shaders failed");
			System.err.println(glGetProgramInfoLog(shaderProgramID, len));
			assert false : "";
		}
	}
	
	public void use() {
		glUseProgram(shaderProgramID);
	}
	
	public void detach() {
		glUseProgram(0);
	}
	
	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
	public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }
	
	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);
	}
	
	public void uploadTextureArray(String varName, int[] slots) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1iv(varLocation, slots);
	}
}
