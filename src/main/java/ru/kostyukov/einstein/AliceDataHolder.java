package ru.kostyukov.einstein;

import java.util.HashMap;
import java.util.Map;

public class AliceDataHolder implements DataHolder {

  private final Map<Integer, Map<Integer, String>> maps = new HashMap<>();

  public AliceDataHolder() {
    fillWithAliceData();
  }

  public String getName(int prop, int value) {
    String propName =
        switch (prop) {
          case 0 -> "Гость";
          case 1 -> "Чай";
          case 2 -> "Угощение";
          case 3 -> "Чашка";
          default -> "undefined";
        };

    return propName + ": " + maps.get(prop).get(value);
  }

  public void fillWithAliceData() {
    final Map<Integer, String> guests = new HashMap<>();
    guests.put(0, "Алиса");
    guests.put(1, "Безумный шляпник");
    guests.put(2, "Мартовский заяц");
    guests.put(3, "Соня");
    guests.put(4, "Белый кролик");
    guests.put(5, "Чеширский кот");
    guests.put(6, "Герцогиня");
    guests.put(7, "Шалтай-Болтай");
    guests.put(8, "Гусеница");
    maps.put(0, guests);

    final Map<Integer, String> teas = new HashMap<>();
    teas.put(0, "Дарджилинг");
    teas.put(1, "Ассам");
    teas.put(2, "Эрл-Грей");
    teas.put(3, "Мятный");
    teas.put(4, "Каркаде");
    teas.put(5, "Улун");
    teas.put(6, "Анчан");
    teas.put(7, "Зеленый");
    teas.put(8, "Ромашковый");
    maps.put(1, teas);

    final Map<Integer, String> treats = new HashMap<>();
    treats.put(0, "Хлеб с маслом");
    treats.put(1, "Апельсиновый мармелад");
    treats.put(2, "Кексы со смородиной");
    treats.put(3, "Пудинг");
    treats.put(4, "Имбирное печенье");
    treats.put(5, "Булочка с повидлом");
    treats.put(6, "Булочка с корицей");
    treats.put(7, "Рахат-лукум");
    treats.put(8, "Кексы с цукатами");
    maps.put(2, treats);

    final Map<Integer, String> cups = new HashMap<>();
    cups.put(0, "Фарфоровая");
    cups.put(1, "Треснувшая");
    cups.put(2, "Без ручки");
    cups.put(3, "Без дна");
    cups.put(4, "Стеклянная");
    cups.put(5, "Съедобная");
    cups.put(6, "Золотая");
    cups.put(7, "Железная");
    cups.put(8, "Крошечная");
    maps.put(3, cups);
  }
}
