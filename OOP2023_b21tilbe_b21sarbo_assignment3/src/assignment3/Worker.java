package assignment3;

import java.util.Random;

public class Worker implements Runnable {
  private String name;
  private int energyLevel;
  private int T;
  private CoffeeMachine coffeeMachine;

  public Worker(String name, int energyLevel, CoffeeMachine coffeeMachine) {
    this.name = name;
    this.energyLevel = energyLevel;
    this.T = new Random().nextInt(1001 * CoffeeMachine.timeFactor) + 500 * CoffeeMachine.timeFactor; // random time between 500 ms and 1500 ms
    this.coffeeMachine = coffeeMachine;
  }

  public String getName() {
    return name;
  }

  public int getEnergyLevel() {
    return energyLevel;
  }

  public void increaseEnergyLevel(int energy) {
    energyLevel += energy;
  }

  @Override
  public void run() {
    System.out.println(name + " is working with energy level " + energyLevel);
    long startTime = System.currentTimeMillis(); // The time the workers start their day
    while (energyLevel > 0) {
      long currentTime = System.currentTimeMillis(); // current time
      if (currentTime - startTime >= T) { // has enough time passed?
        energyLevel--;
        System.out.println(name + " is working with energy level " + energyLevel);
        startTime = currentTime; // update start time
      }

      if (energyLevel < 30) { // if energy level is too low, join the coffee queue
        coffeeMachine.addWorker(this);
      }
    }
    System.out.println(name + " is too tired to work anymore!");
  }
}