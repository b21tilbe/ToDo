package assignment3;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Fika {
  public static void main(String[] args) {
    CoffeeMachine coffeeMachine = new CoffeeMachine();

    // creates the different workers and gives them a random energyLevel number between 30 and 90
    Worker worker1 = new Worker("Joel", new Random().nextInt(61) + 30, coffeeMachine);
    Worker worker2 = new Worker("Selma", new Random().nextInt(61) + 30, coffeeMachine);
    Worker worker3 = new Worker("Astrid", new Random().nextInt(61) + 30, coffeeMachine);
    Worker worker4 = new Worker("Theo", new Random().nextInt(61) + 30, coffeeMachine);

    // create threads for each worker
    Thread t1 = new Thread(worker1);
    Thread t2 = new Thread(worker2);
    Thread t3 = new Thread(worker3);
    Thread t4 = new Thread(worker4);

    Thread drinkThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) { // endless loop
          synchronized(coffeeMachine) {
            while (coffeeMachine.currentDrinks == coffeeMachine.maxDrinks) { // wait if the reserve is full
              try {
                coffeeMachine.wait(2000 * CoffeeMachine.timeFactor);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            while (coffeeMachine.currentDrinks < coffeeMachine.maxDrinks) {
              coffeeMachine.drinks.add("A drink");
              coffeeMachine.currentDrinks++;
              System.out.println("Drink created. Coffee Machine has " + coffeeMachine.currentDrinks + " drinks in reserve.");

              coffeeMachine.notifyAll();

              try {
                Thread.sleep(2000 * CoffeeMachine.timeFactor); // wait 2 sec before iteration
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
    });

    //start the threads
    drinkThread.start();
    t1.start();
    t2.start();
    t3.start();
    t4.start();

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.exit(0); // end program
      }
    }, 20000); // 20000 ms = 20 sec
  }
}