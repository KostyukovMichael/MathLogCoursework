package ru.kostyukov.einstein;

import com.github.javabdd.BDD.AllSatIterator;

public class AliceSolutionPrinter implements SolutionPrinter {

  private BddModel model = null;
  private DataHolder holder = null;

  public AliceSolutionPrinter(BddModel model, DataHolder holder) {
    this.model = model;
    this.holder = holder;
  }

  public void printSolutions(AllSatIterator satIterator) {
    while (satIterator.hasNext()) {
      byte[] sat = satIterator.next();
      expandAndDecrypt(sat, 0);
    }
  }

  private void expandAndDecrypt(byte[] sat, int pos) {
    if (pos == sat.length) {
      decryptValue(sat);
    } else if (sat[pos] != -1) {
      expandAndDecrypt(sat, pos + 1);
    } else if (sat[pos] == -1) {
      sat[pos] = 0;
      expandAndDecrypt(sat, pos + 1);
      sat[pos] = 1;
      expandAndDecrypt(sat, pos + 1);
      sat[pos] = -1;
    }
  }

  private void decryptValue(byte[] sat) {
    int n = model.getN();
    int m = model.getM();
    int t = model.bitWidth();

    for (int object = 0; object < n; object++) {
      System.out.print(object + ": ");
      for (int prop = 0; prop < m; prop++) {
        int value = 0;

        for (int bit = 0; bit < t; bit++) {
          if (sat[model.varIndex(object, prop, bit)] == 1) {
            value |= (1 << bit);
          }
        }

        System.out.print(holder.getName(prop, value) + " | ");
      }
      System.out.println();
    }
    System.out.println();
  }
}
