package task2;

import java.io.File;

public class Main {

    static void siteParse() {
        SiteParser siteParser = new SiteParser();
        for (int i = 1; i < 100; i++) {
            siteParser.parse("https://www.lamoda.ru/c/399/clothes-bluzy-rubashki/?page=", i);
        }
        for (int i = 1; i < 170; i++) {
            siteParser.parse("https://www.lamoda.ru/c/369/clothes-platiya/?page=", i);
        }
    }

    public static void main(String[] args) {
        siteParse();
        SiteParser siteParser = new SiteParser();
        siteParser.readFromFile(new File("catalog.txt"));
        Indexer indexer = new Indexer();
        indexer.index(SiteParser.lamodaEntityList);

        Searcher searcher = new Searcher();

        //пример выполнения
        searcher.searchForQuery("платье Mango желтый");
        searcher.searchForQuery("рубашка Modis черный");
        searcher.searchForQuery("рубашка Befree красный");
        searcher.searchForQuery("блуза Mango голубой");
        searcher.searchForQuery("платье Befree зеленый");
        searcher.searchForQuery("рубашка Desigual серый");
    }
}
