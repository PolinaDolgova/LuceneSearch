package task2;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import task2.model.LamodaEntity;

public class Indexer {
    private static final String INDEX_DIR = "D:\\university\\технологии программирования Кузнецов\\Final\\UpdateSearch\\src\\main\\resources";

    private static IndexWriter createWriter() throws IOException {
        FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    private static Document createDocument(LamodaEntity lamodaEntity) {
        Document document = new Document();
        document.add(new StringField("link", lamodaEntity.getLink(), Field.Store.YES));
        document.add(new TextField("type", lamodaEntity.getType(), Field.Store.YES));
        document.add(new TextField("name", lamodaEntity.getName(), Field.Store.YES));
        document.add(new TextField("price", lamodaEntity.getPrice(), Field.Store.YES));
        document.add(new TextField("brand", lamodaEntity.getBrand(), Field.Store.YES));
        document.add(new TextField("color", lamodaEntity.getColor(), Field.Store.YES));
        return document;
    }

    void index(List<LamodaEntity> lamodaEntities) {
        IndexWriter writer;
        try {
            writer = createWriter();
            List<Document> documents = new ArrayList<Document>();

            for (LamodaEntity lamodaEntity : lamodaEntities) {
                Document document = createDocument(lamodaEntity);
                documents.add(document);
            }

            writer.deleteAll();
            writer.addDocuments(documents);
            writer.commit();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
