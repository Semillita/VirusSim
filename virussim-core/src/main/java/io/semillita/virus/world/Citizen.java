package io.semillita.virus.world;

import java.util.Random;

public class Citizen {

	private final int daysInfected, daysImmune;
	
	private boolean queueInfection = false;
	private int daysInState = 0;
	private float riskOfDeath;
	private State state;
	
	public Citizen(int daysInfected, int daysImmune, float riskOfDeath, State startState) {
		this.daysInfected = daysInfected;
		this.daysImmune = daysImmune;
		this.riskOfDeath = riskOfDeath;
		this.state = startState;
	}
	
	public State getState() {
		return state;
	}
	
	public void queueInfection() {
		queueInfection = true;
	}
	
	public void update() {
		if (state == State.HEALTHY && queueInfection) {
			state = State.INFECTED;
			queueInfection = false;
			daysInState = 0;
		} else if (state == State.INFECTED && daysInState == daysInfected) {
			Random random = new Random();
			if (random.nextDouble() > riskOfDeath) {
				state = State.IMMUNE;
			} else {
				state = State.DEAD;
			}
			daysInState = 0;
		} else if (state == State.IMMUNE && daysInState == daysImmune) {
			state = State.HEALTHY;
			daysInState = 0;
		} else {
			daysInState++;
		}
	}
	
	@Override
	public String toString() {
		return "Citizen: " + state;
	}
	
	public static enum State {
		HEALTHY,
		INFECTED,
		IMMUNE,
		DEAD;
	}
}
