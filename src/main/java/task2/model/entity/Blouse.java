package task2.model.entity;

import task2.model.LamodaEntity;

public class Blouse extends LamodaEntity{

    static {
        typeEntityLink = "https://www.lamoda.ru/c/399/clothes-bluzy-rubashki/?page=";
        maxPageCount = 60;
    }

    public Blouse () {
    }

    public Blouse (String link, String name, String price, String brand, String color) {
        this.type = "блуза";
        this.link = link;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.color = color;
    }
}
