package io.semillita.virus.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.semillita.virus.Stats;
import io.semillita.virus.Timer;
import io.semillita.virus.world.Citizen.State;

public class World {

	private static final boolean logFlag = false;
	
	private int amountOfDays, amountOfHouses, housesPerDay;
	private int daysInfected, daysImmune;
	private float riskOfInfection, riskOfDeath;
	private int startInfected;
	
	public World(int amountOfDays, int amountOfHouses, int housesPerDay, int daysInfected, int daysImmune,
			float riskOfInfection, float riskOfDeath, int startInfected) {
		this.amountOfDays = amountOfDays;
		this.amountOfHouses = amountOfHouses;
		this.housesPerDay = housesPerDay;
		this.daysInfected = daysInfected;
		this.daysImmune = daysImmune;
		this.riskOfInfection = riskOfInfection;
		this.riskOfDeath = riskOfDeath;
		this.startInfected = startInfected;
	}

	/**Runs the simulation*/
	public Stats simulate() {
		System.out.println("Simulate");
		
		Timer.start();
		
		List<List<Float>> measures = new ArrayList<>();;
		
		List<List<Citizen>> houses = new ArrayList<>();
		for (int i = 0; i < amountOfHouses; i++) {
			houses.add(new ArrayList<>());
		}
		
		List<Citizen> citizens = new ArrayList<>();
		for (int i = 0; i < amountOfHouses * 3 - startInfected; i++) {
			citizens.add(new Citizen(daysInfected, daysImmune, State.HEALTHY));
		}
		
		for (int i = 0; i < startInfected; i++) {
			citizens.add(new Citizen(daysInfected, daysImmune, State.INFECTED));
		}
		
		Random random = new Random();
		
		for (int day = 0; day < amountOfDays; day++) {
			if (logFlag) System.out.println("Day " + day);
			// Körs varje dag
			for (int interval = 0; interval < housesPerDay; interval++) {
				// Töm husen
				for (var house : houses) {
					house.clear();
				}
				
				for (var citizen : citizens) {
					int house = random.nextInt(amountOfHouses);
					houses.get(house).add(citizen);
				}
				
				// Räkna på infekterande
				for (var house : houses) {
					var infectedCitizens = house
							.stream()
							.filter(citizen -> citizen.getState() == State.INFECTED)
							.collect(Collectors.toList());
					
					var healthyCitizens = house
							.stream()
							.filter(citizen -> citizen.getState() == State.HEALTHY)
							.collect(Collectors.toList());
					
					var totalRiskOfInfection = 1 - Math.pow((1 - riskOfInfection), infectedCitizens.size());
					
					if (logFlag) System.out.println("Infected: " + infectedCitizens.size() + ", risk of infection: " + totalRiskOfInfection);
					
					for (var citizen : healthyCitizens) {
						var a = random.nextDouble();
						if (a <= totalRiskOfInfection) {
							citizen.queueInfection();
							if (logFlag) System.out.println("Queue infection");
						}
					}
				}
				
				if (logFlag) System.out.println(getPortions(citizens).get(1));
			}
			
			citizens.forEach(citizen -> citizen.update());
			measures.add(getPortions(citizens));
		}
		
		System.out.println("Simulated in " + Timer.stop() + " seconds");
		
		return new Stats(measures);
	}
	
	public List<Float> getPortions(List<Citizen> citizens) {
		float healthy = citizens
				.stream()
				.filter(citizen -> citizen.getState() == State.HEALTHY)
				.count() / (float) citizens.size();
		
		float infected = citizens
				.stream()
				.filter(citizen -> citizen.getState() == State.INFECTED)
				.count() / (float) citizens.size();
		
		float immune = citizens
				.stream()
				.filter(citizen -> citizen.getState() == State.IMMUNE)
				.count() / (float) citizens.size();
		
		return Arrays.asList(healthy, infected, immune);
	}
	
}