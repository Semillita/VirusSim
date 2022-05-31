package io.semillita.virus;

public class Timer {

	private static long lastTime = 0;
	
	public static void start() {
		lastTime = System.nanoTime();
	}
	
	public static double stop() {
		return (System.nanoTime() - lastTime) / 1_000_000_000d;
	}
	
}
