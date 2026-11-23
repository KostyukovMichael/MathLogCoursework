package ru.kostyukov.einstein;

import com.github.javabdd.BDD.AllSatIterator;

public interface SolutionPrinter {

  void printSolutions(AllSatIterator satIterator);
}
