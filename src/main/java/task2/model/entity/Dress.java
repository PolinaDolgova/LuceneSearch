package task2.model.entity;

import task2.model.LamodaEntity;

public class Dress extends LamodaEntity {

    static {
        typeEntityLink = "https://www.lamoda.ru/c/369/clothes-platiya/?page=";
        maxPageCount = 60;
    }

    public Dress() {
    }

    public Dress(String link, String name, String price, String brand, String color) {
        this.type = "платье";
        this.link = link;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.color = color;
    }
}
