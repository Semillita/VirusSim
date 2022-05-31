package io.semillita.virus;

import java.io.IOException;

public class Files {

	public static String read(String filepath) {
		try {
			byte[] bytes = Files.class.getResourceAsStream(filepath).readAllBytes();
			return new String(bytes, "ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
