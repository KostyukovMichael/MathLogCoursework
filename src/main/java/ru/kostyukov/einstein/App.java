package ru.kostyukov.einstein;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDFactory;

public class App {
  public static void main(String[] args) {
    startTeaTime();
  }

  private static void startTeaTime() {
    final int n = 9;
    final int m = 4;
    final int rows = 3;
    final int cols = 3;

    final BDDFactory factory = BDDFactory.init("java", 100_000, 10_000);

    final BddModel model = new BddModel(factory, n, m);
    final DataHolder holder = new AliceDataHolder();
    final SolutionPrinter printer = new AliceSolutionPrinter(model, holder);
    final Grid grid = new Grid(rows, cols);

    solveWithoutGlue(model, printer, grid);
    solveWithGlue(model, printer, grid);

    model.free();
  }

  // 21 + 2 ограничения для 12 решений без склейки
  private static void solveWithoutGlue(BddModel model, SolutionPrinter printer, Grid grid) {
    final ConstraintBuilder constraintBuilder = new ConstraintBuilder(model, grid, false);

    BDD result = applyCommonConstraints(model, grid, constraintBuilder);

    result = result.andWith(constraintBuilder.type1(3, 0, 3));
    result = result.andWith(constraintBuilder.type1(1, 8, 0));

    System.out.println("===Без склейки===\n" + result.satCount());
    if (result.satCount().intValue() <= 20) {
      printer.printSolutions(result.allsat());
    }

    result.free();
  }

  private static void solveWithGlue(BddModel model, SolutionPrinter printer, Grid grid) {
    final ConstraintBuilder constraintBuilder = new ConstraintBuilder(model, grid, true);

    BDD result = applyCommonConstraints(model, grid, constraintBuilder);

    result = result.andWith(constraintBuilder.type1(1, 8, 0));
    result = result.andWith(constraintBuilder.type3(0, 2, 1, 8, true));
    result = result.andWith(constraintBuilder.type4(1, 6, 2, 3));

    System.out.println("===Со склейкой===\n" + result.satCount());
    if (result.satCount().intValue() <= 20) {
      printer.printSolutions(result.allsat());
    }

    result.free();
  }

  // 21 Ограничения по варианту
  private static BDD applyCommonConstraints(
      BddModel model, Grid grid, ConstraintBuilder constraintBuilder) {

    BDD result = model.one();

    // 7 ограничений типа 1
    result = result.andWith(constraintBuilder.type1(3, 4, 7));
    result = result.andWith(constraintBuilder.type1(0, 5, 5));
    result = result.andWith(constraintBuilder.type1(1, 3, 4));
    result = result.andWith(constraintBuilder.type1(0, 7, 7));
    result = result.andWith(constraintBuilder.type1(3, 2, 5));
    result = result.andWith(constraintBuilder.type1(2, 6, 8));
    result = result.andWith(constraintBuilder.type1(0, 1, 1));

    // Ограничения типа 5 и 6
    result = result.andWith(model.buildBaseConstraints());

    // 5 ограничений типа 2
    result = result.andWith(constraintBuilder.type2(0, 8, 2, 1));
    result = result.andWith(constraintBuilder.type2(1, 7, 3, 0));
    result = result.andWith(constraintBuilder.type2(2, 4, 1, 3));
    result = result.andWith(constraintBuilder.type2(0, 5, 3, 8));
    result = result.andWith(constraintBuilder.type2(0, 3, 2, 5));

    // 5 ограничений типа 3
    result = result.andWith(constraintBuilder.type3(0, 3, 2, 3, false));
    result = result.andWith(constraintBuilder.type3(2, 6, 2, 2, true));
    result = result.andWith(constraintBuilder.type3(0, 8, 0, 4, true));
    result = result.andWith(constraintBuilder.type3(1, 4, 3, 4, false));
    result = result.andWith(constraintBuilder.type3(0, 7, 2, 7, false));

    // 4 ограничения типа 4
    result = result.andWith(constraintBuilder.type4(1, 5, 1, 3));
    result = result.andWith(constraintBuilder.type4(3, 8, 1, 2));
    result = result.andWith(constraintBuilder.type4(3, 1, 3, 6));
    result = result.andWith(constraintBuilder.type4(0, 6, 0, 4));

    return result;
  }
}
