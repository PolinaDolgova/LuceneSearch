package task2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import task2.model.LamodaEntity;
import task2.model.entity.Blouse;
import task2.model.entity.Dress;
import task2.model.entity.Shirt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SiteParser {

    static int count = 0;
    static List<LamodaEntity> lamodaEntityList = new ArrayList<LamodaEntity>();

    void writeItemToFile(File file, LamodaEntity lamodaEntity) {
        try {
            FileWriter writer = new FileWriter(file, true);
            StringBuilder builder = new StringBuilder();
            builder.append("link:" + lamodaEntity.getLink() + "\r\n");
            builder.append("name:" + lamodaEntity.getName() + "\r\n");
            builder.append("price:" + lamodaEntity.getPrice() + "\r\n");
            builder.append("brand:" + lamodaEntity.getBrand() + "\r\n");
            builder.append("color:" + lamodaEntity.getColor() + "\r\n");
            writer.append(builder);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void parse(String connectionLink, Integer pageNumber) {
        try {
            Document doc = Jsoup.connect(connectionLink + pageNumber.toString()).get();
            Element catalog = doc.select("div.products-catalog__list").first();
            Elements catalogItems = catalog.getElementsByClass("products-list-item");
            File file = new File("catalog.txt");
            for (int i = 0; i < 60; i++) {
                System.out.println("LamodaEntity №" + count);
                LamodaEntity lamodaEntity = create(catalogItems.get(i));
                lamodaEntityList.add(lamodaEntity);
                writeItemToFile(file, lamodaEntity);
                lamodaEntity.information();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void readFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str = reader.readLine();
            while (str != null) {
                System.out.println("LamodaEntity №" + count);
                String link = str.substring(5);
                String name = reader.readLine();
                name = name.substring(5);
                String price = reader.readLine();
                price = price.substring(6);
                String brand = reader.readLine();
                brand = brand.substring(6);
                String color = reader.readLine();
                color = color.substring(6);
                LamodaEntity lamodaEntity;
                if (name.indexOf("Платье") != -1) {
                    lamodaEntity = new Dress(link, name, price, brand, color);
                } else if (name.indexOf("Рубашка") != -1) {
                    lamodaEntity = new Shirt(link, name, price, brand, color);
                } else {
                    lamodaEntity = new Blouse(link, name, price, brand, color);
                }
                lamodaEntityList.add(lamodaEntity);
                str = reader.readLine();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static LamodaEntity create(Element element) {
        String link = element.getElementsByTag("a").attr("href");
        String text = element.select("img").attr("alt");
        String name = text.substring(0, text.indexOf("Жен"));
        String price = element.select("span.price").text();
        text = element.select("div.products-list-item__brand").text();
        String brand = text.substring(0, text.indexOf("/") - 1);
        String temp = name.substring(name.indexOf("цвет:"));
        String color = temp.substring(6, temp.indexOf("."));
        if (text.indexOf("Платье") != -1) {
            return new Dress(link, name, price, brand, color);
        }
        if (name.indexOf("Рубашка") != -1) {
            return new Shirt(link, name, price, brand, color);
        } else {
            return new Blouse(link, name, price, brand, color);
        }
    }
}
