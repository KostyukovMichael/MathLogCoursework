package ru.kostyukov;

import com.github.javabdd.BDD;
import com.github.javabdd.BDD.AllSatIterator;
import com.github.javabdd.BDDFactory;

public class App {
  public static void main(String[] args) {
    callBDD();
  }

  private static void callBDD() {
    BDDFactory factory = BDDFactory.init("java", 100_000, 10_000);
    factory.setVarNum(6);

    BDD a1 = factory.ithVar(0);
    BDD a2 = factory.ithVar(1);
    BDD a3 = factory.ithVar(2);
    BDD b1 = factory.ithVar(3);
    BDD b2 = factory.ithVar(4);
    BDD b3 = factory.ithVar(5);

    BDD g = a1.and(b1).or(a2.and(b2).or(a3.and(b3)));

    System.out.println("g: " + g.nodeCount());

    // a1 a2 a3 b1 b2 b3
    factory.swapVar(1, 3);
    // a1 b1 a3 a2 b2 b3
    factory.swapVar(2, 1);
    // a1 b1 a2 a3 b2 b3
    factory.swapVar(2, 4);
    // a1 b1 a2 b2 a3 b3

    System.out.println("g: " + g.nodeCount());

    System.out.println("sats: " + g.satCount());

    AllSatIterator iterator = g.allsat();
    while (iterator.hasNext()) {
      byte[] sats = iterator.next();
      for (int i = 0; i < 6; i++) {
        System.out.print(sats[i] < 0 ? "X" : sats[i]);
      }
      System.out.println();
    }

    factory.done();
  }
}
