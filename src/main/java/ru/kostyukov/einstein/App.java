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

    solve(model, printer, grid, false);
    solve(model, printer, grid, true);

    model.free();
  }

  private static void solve(BddModel model, SolutionPrinter printer, Grid grid, boolean glued) {
    final ConstraintBuilder constraintBuilder = new ConstraintBuilder(model, grid, glued);

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

    // При такой расстановке получается 288 решений, поэтому пришлось добавить еще 2 ограничения
    result = result.andWith(constraintBuilder.type1(3, 0, 3));
    result = result.andWith(constraintBuilder.type1(1, 8, 0));

    System.out.println(result.satCount());
    if (result.satCount().intValue() <= 50) {
      printer.printSolutions(result.allsat());
    }

    result.free();
  }
}
