package ru.kostyukov.einstein;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstraintBuilderTest {

  private BDDFactory factory;
  private BddModel model;
  private Grid grid;

  @BeforeEach
  void setUp() {
    factory = BDDFactory.init("java", 100_000, 10_000);
    model = new BddModel(factory, 4, 2); // N=4, M=2 -> 576 базовых решений
    grid = new Grid(2, 2);
  }

  @AfterEach
  void tearDown() {
    factory.done();
  }

  @Test
  void type1FixesExactObjectProperty() {
    ConstraintBuilder cb = new ConstraintBuilder(model, grid, false);
    BDD f = model.buildBaseConstraints().andWith(cb.type1(0, 0, 2)); // у объекта 0 свойство 0 = 2

    // 3! * 4! = 144
    assertEquals(144.0, f.satCount().doubleValue(), 1e-6);
    f.free();
  }

  @Test
  void type2LinksTwoPropertiesOfSameObject() {
    ConstraintBuilder cb = new ConstraintBuilder(model, grid, false);
    BDD base = model.buildBaseConstraints();
    BDD f = base.and(cb.type2(0, 1, 1, 3)); // prop0=1 <-> prop1=3

    // 4! * 3! = 144
    assertEquals(144.0, f.satCount().doubleValue(), 1e-6);

    // Противоречие: у объекта 0 prop0=1, но prop1=2
    BDD conflict = f.and(cb.type1(0, 0, 1)).andWith(cb.type1(1, 0, 2));
    assertEquals(0.0, conflict.satCount().doubleValue(), 1e-6);

    conflict.free();
    f.free();
    base.free();
  }

  @Test
  void type3EnforcesDirectionAndBoundariesWithoutGlue() {
    ConstraintBuilder cb = new ConstraintBuilder(model, grid, false);
    // prop0=1 справа-снизу от prop1=2. На сетке 2x2 без склейки единственная пара: 0 -> 3
    BDD rule = cb.type3(0, 1, 1, 2, true);
    BDD f = model.buildBaseConstraints().andWith(rule);

    // 3! * 3! = 36
    assertEquals(36.0, f.satCount().doubleValue(), 1e-6);

    // Опорный prop1=2 не может стоять в клетке 1 (у неё нет соседа справа-снизу)
    BDD badAnchor = f.and(cb.type1(1, 1, 2));
    assertEquals(0.0, badAnchor.satCount().doubleValue(), 1e-6);
    badAnchor.free();

    // Сосед prop0=1 не может стоять в клетке 0 (она ни для кого не справа-снизу)
    BDD badNeighbor = f.and(cb.type1(0, 0, 1));
    assertEquals(0.0, badNeighbor.satCount().doubleValue(), 1e-6);
    badNeighbor.free();

    f.free();
  }

  @Test
  void type3ChangesValidPairsWithGlue() {
    ConstraintBuilder cb = new ConstraintBuilder(model, grid, true);
    // Со склейкой на 2x2 две пары справа-снизу: 0 -> 3 и 2 -> 1
    BDD f = model.buildBaseConstraints().andWith(cb.type3(0, 1, 1, 2, true));

    // 2 * 3! * 3! = 72
    assertEquals(72.0, f.satCount().doubleValue(), 1e-6);
    f.free();
  }

  @Test
  void type4IsDisjunctionOfLeftAndRightType3() {
    // Без склейки: две возможные пары (0 -> 3 и 1 -> 2), у клетки 2 соседей нет
    ConstraintBuilder noGlue = new ConstraintBuilder(model, grid, false);
    BDD fNoGlue = model.buildBaseConstraints().andWith(noGlue.type4(0, 1, 1, 2));
    assertEquals(72.0, fNoGlue.satCount().doubleValue(), 1e-6);

    BDD anchorAt2NoGlue = fNoGlue.and(noGlue.type1(1, 2, 2));
    assertEquals(0.0, anchorAt2NoGlue.satCount().doubleValue(), 1e-6);
    anchorAt2NoGlue.free();
    fNoGlue.free();

    // Со склейкой: у клетки 2 появляется сосед справа-снизу (клетка 1)
    ConstraintBuilder withGlue = new ConstraintBuilder(model, grid, true);
    BDD fGlue =
        model
            .buildBaseConstraints()
            .andWith(withGlue.type4(0, 1, 1, 2))
            .andWith(withGlue.type1(1, 2, 2));
    assertEquals(36.0, fGlue.satCount().doubleValue(), 1e-6);
    fGlue.free();
  }
}
