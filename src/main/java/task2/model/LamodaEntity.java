package task2.model;

public abstract class LamodaEntity {

    protected static Integer maxPageCount;
    protected static String typeEntityLink;

    protected String type;
    protected String link;
    protected String name;
    protected String price;
    protected String brand;
    protected String color;

    public LamodaEntity(){}

    public void information() {
        System.out.println("link: " + this.link);
        System.out.println("name: " + this.name);
        System.out.println("price: " + this.price);
        System.out.println("brand: " + this.brand);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public static String getTypeEntityLink() {
        return typeEntityLink;
    }

    public static Integer getMaxPageCount() {
        return maxPageCount;
    }
}
