package ru.kostyukov.einstein;

import com.github.javabdd.BDD;

public class ConstraintBuilder {

  private final BddModel model;
  private final Grid grid;
  private final boolean glued;

  public ConstraintBuilder(BddModel model, Grid grid, boolean glued) {
    this.model = model;
    this.grid = grid;
    this.glued = glued;
  }

  /**
   * Создает новую BDD от 1 с наложенным ограничением типа 1.
   *
   * <p>Тип 1: объект {@code object} имеет свойство {@code prop} в значении {@code value}.
   *
   * <p><i>Пример: <b>норвежец</b> живет в <b>первом</b> доме.</i>
   *
   * @return построенную BDD.
   */
  public BDD type1(int prop, int object, int value) {
    return model.one().andWith(model.p(prop, object, value));
  }

  /**
   * Создает новую BDD от 1 с наложенным ограничением типа 2.
   *
   * <p>Тип 2: Любой объект обладающий своством {@code prop1} в значении {@code value1} также
   * обладает свойством {@code prop2} в значении {@code value2}.
   *
   * <p><i>Пример: <b>англичанин</b> живет в <b>красном</b> доме.</i>
   *
   * @return построенную BDD.
   */
  public BDD type2(int prop1, int value1, int prop2, int value2) {
    final int n = model.getN();
    BDD result = model.one();

    for (int object = 0; object < n; object++) {
      result =
          result.andWith(model.p(prop1, object, value1).biimpWith(model.p(prop2, object, value2)));
    }

    return result;
  }

  /**
   * Создает новую BDD от 1 с наложенным ограничением типа 3.
   *
   * <p>Тип 3: объект, обладающий свойством {@code prop1} в значении {@code value1}, сидит
   * справа/слева от объекта, обладающего свойством {@code prop2} в значении {@code value2}.
   *
   * <p><i>Пример: зеленый дом находится слева от белого.</i>
   *
   * <p>Объект с {@code (prop2, value2)} является опорным, а объект {@code (prop1, value1)}
   * обозначает соседа.
   *
   * @param right если {@code true}, то объект с {@code (prop1, value1)} является правым соседом
   *     объекта {@code (prop2, value2)}, иначе — левым.
   * @return построенную BDD.
   */
  public BDD type3(int prop1, int value1, int prop2, int value2, boolean right) {
    final int n = model.getN();
    boolean[] hasMaster = new boolean[n];
    BDD result = model.one();

    for (int object = 0; object < n; object++) {
      int neighbor = right ? grid.rightDown(object, glued) : grid.leftDown(object, glued);

      if (neighbor != -1) {
        hasMaster[neighbor] = true;
        result =
            result.andWith(
                model.p(prop2, object, value2).biimpWith(model.p(prop1, neighbor, value1)));
      } else {
        BDD p = model.p(prop2, object, value2);
        result = result.andWith(p.not());
        p.free();
      }
    }

    for (int cell = 0; cell < n; cell++) {
      if (!hasMaster[cell]) {
        BDD p = model.p(prop1, cell, value1);
        result = result.andWith(p.not());
        p.free();
      }
    }

    return result;
  }

  /**
   * Создает новую BDD от 1 с наложенным ограничением типа 4.
   *
   * <p>Тип 4: объект, обладающий свойством {@code prop1} в значении {@code value1}, сидит или
   * справа, или слева от объекта, обладающего свойством {@code prop2} в значении {@code value2}.
   *
   * <p><i>Пример: зеленый дом рядом с кирпичным</i>
   *
   * @return построенную BDD.
   */
  public BDD type4(int prop1, int value1, int prop2, int value2) {
    BDD result = model.one();

    result =
        result.andWith(
            type3(prop1, value1, prop2, value2, false)
                .orWith(type3(prop1, value1, prop2, value2, true)));

    return result;
  }
}
