package ru.kostyukov.einstein;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDFactory;

public class BddModel {

  private final BDDFactory factory;
  private final int n; // objects
  private final int m; // properties

  public BddModel(BDDFactory factory, int n, int m) {
    this.factory = factory;
    this.n = n;
    this.m = m;

    factory.setVarNum(varCount());
  }

  /**
   * Вычисляет T.
   *
   * @return количество бит на одно значение свойства.
   */
  public int bitWidth() {
    return 32 - Integer.numberOfLeadingZeros(n - 1);
  }

  /**
   * @return общее число переменных при заданных N и M.
   */
  public int varCount() {
    return n * m * bitWidth();
  }

  /**
   * Строит BDD, который говорит о том, что на месте object свойство prop равно числу value.
   *
   * @param prop — номер свойства.
   * @param object — номер объекта.
   * @param value — номер значения свойства.
   * @return построенную диаграмму для этого выражения.
   */
  public BDD p(int prop, int object, int value) {
    final int T = bitWidth();
    BDD result = factory.one();

    for (int bit = 0; bit < T; bit++) {
      if (((value >> bit) & 1) == 1) {
        result.andWith(factory.ithVar(varIndex(object, prop, bit)));
      } else {
        result.andWith(factory.nithVar(varIndex(object, prop, bit)));
      }
    }

    return result;
  }

  /**
   * Инициализирует единицей результирующую функцию F, и последовательно накладывает ограничения
   * типа 5 и 6.
   *
   * <p><b>Тип 5</b>: У двух различных object значения prop не совпадают.
   *
   * <p><b>Тип 6</b>: Значения свойств (value) не могут быть больше заданного N (без этого
   * ограничения они могут занимать всю выделенную им битовую шину T).
   *
   * <p><i>Пример: при N = 9, T = 4 ширина шины вмещает 16 значений t = 0, ..., 15, но t должна быть
   * ограничена N.</i>
   *
   * @return F с примененными типами ограничений 5 и 6.
   */
  public BDD buildBaseConstraints() {
    BDD result = factory.one();

    // type 6
    for (int object = 0; object < n; object++) {
      for (int prop = 0; prop < m; prop++) {
        for (int value = n; value < ((1 << bitWidth())); value++) {
          BDD p = p(prop, object, value);
          result = result.andWith(p.not());
          p.free();
        }
      }
    }

    // type 5
    for (int i1 = 0; i1 < n - 1; i1++) {
      for (int i2 = i1 + 1; i2 < n; i2++) {
        for (int k = 0; k < m; k++) {
          for (int j = 0; j < n; j++) {
            BDD pi2 = p(k, i2, j);
            result = result.andWith(p(k, i1, j).impWith(pi2.not()));
            pi2.free();
          }
        }
      }
    }

    return result;
  }

  // z[i, j, t] = x[T * M * i + T * k + t]
  //
  // TMi = доходим до нужного объекта
  // Tk = доходим до нужного свойства объекта
  // t = доходим до определенного бита значений свойств.
  //
  // Другой вариант: x[i, j, t] = x[T * N * k + T * i + t]
  // TNk = доходим до нужного свойства
  // Ti = спускаемся до нужного объекта
  // t = определенный бит значения
  //
  // Во втором варианте мы рассматриваем все объекты для свойства 0, потом все объекты для свойства
  // 1 и т.д.
  // Это должно уменьшить нагрузку на память, так как графу не придется помнить переменные свойства
  // после его обработки
  public int varIndex(int object, int prop, int bit) {
    int T = bitWidth();
    return T * n * prop + T * object + bit;
  }
}
