package ru.kostyukov.einstein;

public class Grid {
  private final int rows;
  private final int cols;

  public Grid(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
  }

  // 0 1 2 3
  // 4 5 6 7
  // 8 9 10 11
  // 12 13 14 15

  // 0 1 2
  // 3 4 5
  // 6 7 8
  public int leftDown(int pos, boolean withGlue) {
    if (withGlue) {
      if (pos % cols == 0) {
        return -1;
      } else {
        return (pos + cols - 1) % (rows * cols);
      }
    } else {
      if (pos % cols == 0 || rows * cols - pos <= cols) {
        return -1;
      } else {
        return pos + cols - 1;
      }
    }
  }

  public int rightDown(int pos, boolean withGlue) {
    if (withGlue) {
      if (pos % cols - cols + 1 == 0) {
        return -1;
      } else {
        return (pos + cols + 1) % (rows * cols);
      }
    } else {
      if (pos % cols - cols + 1 == 0 || rows * cols - pos <= cols) {
        return -1;
      } else {
        return pos + cols + 1;
      }
    }
  }
}
