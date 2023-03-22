package assignment3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class CoffeeMachine {

  private Queue < Worker > queue = new LinkedList < > ();
  public Queue < String > drinks = new LinkedList < > (); // queue for holding drinks
  public int maxDrinks = 20; // maximum number of drinks that can be in the reserve
  public int currentDrinks = 0; // current number of drinks in the reserve
  public Worker currentWorker;

  public synchronized void addWorker(Worker worker) {
    while (worker.getEnergyLevel() < 100) { // loop while energy level is less than 100
      System.out.println(worker.getName() + " joined the queue...");
      queue.add(worker);
      Worker w = queue.peek();
      try {
        Thread.sleep(2000 * timeFactor); // takes 2 second to make coffee
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      String drinkType = getDrinkType();
      int energyLevel = getEnergyLevel(drinkType);
      //String drink = w.getName() + " got a " + drinkType + " (+" + energyLevel + ")";
      getDrink();

      w.increaseEnergyLevel(energyLevel); //increases energyLevel
      System.out.println(w.getName() + " enjoyed a " + drinkType + " with energy level " + energyLevel + ".");
      queue.poll();
      notifyAll(); // wake up all waiting threads
    }
  }

  // Helper method to remove a drink from the reserve
  private String getDrink() {
    synchronized(this) {
      while (currentDrinks == 0 && queue.isEmpty()) { // wait if the reserve is empty and there are no workers in the queue
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      if (currentDrinks == 0 && !queue.isEmpty()) { // allow worker to join the queue even if reserve is empty
        currentWorker = queue.peek();
        System.out.println(currentWorker.getName() + " is waiting for a drink...");
      }
      while (currentDrinks == 0 && currentWorker != null) { // wait for a drink if a worker is in the queue
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      if (currentDrinks > 0) { // serve the drink if available
        String drink = drinks.poll();
        currentDrinks--;
        System.out.println(drink + " taken from the reserve. Current reserve: " + currentDrinks + "/" + maxDrinks);
        notifyAll();
        return drink;
      } else {
        return null;
      }
    }
  }

  // Helper method to get a random drink type
  private String getDrinkType() {
    Random rand = new Random();
    int num = rand.nextInt(3); // generate a random number between 0 and 2
    switch (num) {
    case 0:
      return "BlackCoffee";
    case 1:
      return "Cappuccino";
    case 2:
      return "Latte";
    default:
      return ""; // this should never happen
    }
  }

  private int getEnergyLevel(String drinkType) {
    Random rand = new Random();
    switch (drinkType) {
    case "BlackCoffee":
      return rand.nextInt(6) + 15; // random energy level between 15 and 20
    case "Cappuccino":
      return rand.nextInt(11) + 20;
    case "Latte":
      return rand.nextInt(11) + 25;
    default:
      return 0; // return 0 if drinkType is not recognized
    }
  }
  public static int timeFactor = 1; // the scaling factor for time
}