package task2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.IOException;

public class SynonymAnalyzer extends Analyzer {

    private SynonymMap mySynonymMap;

    public SynonymAnalyzer() {
        SynonymMap.Builder builder = createSynonymousMapBuilder();
        try {
            mySynonymMap = builder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {

        Tokenizer source = new ClassicTokenizer();
        TokenStream filter = new StandardFilter(source);
        filter = new SynonymGraphFilter(filter, mySynonymMap, true);
        return new TokenStreamComponents(source, filter);
    }

    private SynonymMap.Builder createSynonymousMapBuilder() {
        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        builder.add(new CharsRef("алый"), new CharsRef("красный"), true);
        builder.add(new CharsRef("малиновый"), new CharsRef("красный"), true);
        builder.add(new CharsRef("лимонный"), new CharsRef("желтый"), true);
        builder.add(new CharsRef("рыжий"), new CharsRef("оранжевый"), true);
        builder.add(new CharsRef("кремовый"), new CharsRef("бежевый"), true);
        return builder;
    }
}