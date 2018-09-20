package task2;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static task2.NDCGService.nDCG;

public class Searcher {

    private static final String INDEX_DIR = "D:\\university\\технологии программирования Кузнецов\\Final\\UpdateSearch\\src\\main\\resources";
    List<String> fields = new ArrayList<String>();

    private static IndexSearcher createSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    private static TopDocs searchByField(String field, String value, IndexSearcher searcher) {
        QueryParser qp = new QueryParser(field, new SynonymAnalyzer());
        Query query;
        TopDocs result;
        try {
            query = qp.parse(value);
            result = searcher.search(query, 15);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TopDocs searchByQuery(String userQuery, IndexSearcher searcher) {
        QueryParser qp = new MultiFieldQueryParser(new String[]{"type", "brand", "color"}, new StandardAnalyzer());
        Query query;
        TopDocs result;
        try {
            query = qp.parse(userQuery);
            result = searcher.search(query, 20);
            return result;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Integer> getRelevance(String query, List<ScoreDoc> foundDocs, IndexSearcher searcher) {
        List<Integer> relevanceList = new ArrayList<>();
        String[] queryValues = query.split(" ");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < queryValues.length; i++) {
            list.add(queryValues[i]);
        }
        for (ScoreDoc sd : foundDocs) {
            int relevance = 0;
            Document d;
            try {
                d = searcher.doc(sd.doc);
                String[] docValues = new String[]{d.get("type"), d.get("brand"), d.get("color")};
                if (d.get("type").compareTo(list.get(0)) == 0) {
                    for (int i = 0; i < docValues.length; i++) {
                        if (containsIgnoreCase(list, docValues[i])) {
                            relevance++;
                        }
                    }
                } else {
                    relevance = 0;
                }
                relevanceList.add(relevance);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return relevanceList;
    }

    static boolean containsIgnoreCase(List<String> list, String element) {
        for (String s : list) {
            if (s.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }

    void search() {
        try {
            IndexSearcher searcher = createSearcher();
            fields.add("name");
            fields.add("link");
            fields.add("brand");
            fields.add("price");
            fields.add("color");
            fields.add("type");

            boolean flag = false;

            Scanner in = new Scanner(System.in);
            while (!flag) {
                System.out.println("Enter field for search:");
                String field = in.nextLine();
                if (fields.contains(field)) {
                    flag = true;
                    System.out.println("Enter field value:");
                    String value = in.nextLine();
                    searchByField(field, value, searcher);
                    TopDocs foundDocs = searchByField(field, value, searcher);
                    System.out.println("Total Results :: " + foundDocs.totalHits);
                    for (ScoreDoc sd : foundDocs.scoreDocs) {
                        Document d = searcher.doc(sd.doc);
                        System.out.println(String.format(d.get("name")));
                        System.out.println(String.format(d.get("price")));
                        System.out.println(String.format("https://www.lamoda.ru" + d.get("link")));
                    }
                } else {
                    System.out.println("wrong field name");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void searchForQuery(String query) {
        try {
            IndexSearcher searcher = createSearcher();
            fields.add("name");
            fields.add("link");
            fields.add("brand");
            fields.add("price");
            fields.add("color");
            fields.add("type");

            TopDocs foundDocs = searchByQuery(query, searcher);
            List<ScoreDoc> foundDocsList = new ArrayList<>();
            for (ScoreDoc sd : foundDocs.scoreDocs) {
                foundDocsList.add(sd);
            }

            List<Integer> relevance = getRelevance(query, foundDocsList, searcher);
            System.out.println("Total Results :: " + foundDocs.totalHits);
            List<ScoreDoc> sortTopDocs = sortResult(foundDocs, relevance);
            relevance = getRelevance(query, sortTopDocs, searcher);
            for (ScoreDoc sd : sortTopDocs) {
                Document d = searcher.doc(sd.doc);
                System.out.println(String.format(d.get("name")));
                System.out.println(String.format(d.get("price")));
                System.out.println(String.format("https://www.lamoda.ru" + d.get("link")));
            }
            System.out.print(query + " ");

            for (int i = 0; i < relevance.size(); i++) {
                System.out.print(relevance.get(i) + " ");
            }

            System.out.println("NDCG:" + nDCG(relevance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer maxRelevance(List<Integer> relevances) {
        int max = 0;
        for (Integer relevance : relevances) {
            if (relevance.intValue() > max) {
                max = relevance;
            }
        }
        return max;
    }

    private List<ScoreDoc> sortResult(TopDocs topDocs, List<Integer> relevances) {
        Integer maxRelevance = maxRelevance(relevances);
        List<ScoreDoc> result = new ArrayList<>();
        List<Integer> sortRelevance = new ArrayList<>();
        sortRelevance.addAll(relevances);

        Map<ScoreDoc, Integer> map = new HashMap<>();

        int iter = 0;
        for (ScoreDoc sd : topDocs.scoreDocs) {
            map.put(sd, relevances.get(iter));
            iter++;
        }
        Collections.sort(sortRelevance, (o1, o2) -> {
            if (o1 > o2) {
                return -1;
            } else if (o1 < o2) {
                return 1;
            }
            return 0;
        });

        for (int i = 3; i > -1; i--) {
            for (Map.Entry<ScoreDoc, Integer> e : map.entrySet()) {
                if (e.getValue().equals(i)) {
                    result.add(e.getKey());
                }
            }
        }
        return result;
    }
}
