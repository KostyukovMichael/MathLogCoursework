package ru.kostyukov.einstein;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GridTest {

  private final Grid grid = new Grid(3, 3);

  @Test
  void neighborsWithoutGlueMatchReadme() {
    assertEquals(-1, grid.leftDown(0, false));
    assertEquals(4, grid.rightDown(0, false));

    assertEquals(3, grid.leftDown(1, false));
    assertEquals(5, grid.rightDown(1, false));

    assertEquals(4, grid.leftDown(2, false));
    assertEquals(-1, grid.rightDown(2, false));

    assertEquals(-1, grid.leftDown(3, false));
    assertEquals(7, grid.rightDown(3, false));

    assertEquals(6, grid.leftDown(4, false));
    assertEquals(8, grid.rightDown(4, false));

    assertEquals(7, grid.leftDown(5, false));
    assertEquals(-1, grid.rightDown(5, false));

    for (int cell = 6; cell <= 8; cell++) {
      assertEquals(-1, grid.leftDown(cell, false));
      assertEquals(-1, grid.rightDown(cell, false));
    }
  }

  @Test
  void neighborsWithVerticalGlueMatchReadme() {
    // Первые две строки не меняются
    assertEquals(-1, grid.leftDown(0, true));
    assertEquals(4, grid.rightDown(0, true));
    assertEquals(6, grid.leftDown(4, true));
    assertEquals(8, grid.rightDown(4, true));

    // Нижняя строка переходит в верхнюю, но без склейки по бокам
    assertEquals(-1, grid.leftDown(6, true));
    assertEquals(1, grid.rightDown(6, true));

    assertEquals(0, grid.leftDown(7, true));
    assertEquals(2, grid.rightDown(7, true));

    assertEquals(1, grid.leftDown(8, true));
    assertEquals(-1, grid.rightDown(8, true));
  }
}
