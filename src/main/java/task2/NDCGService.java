package task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NDCGService {

    private int maxRelevance = 3;

    public static Double DCG(Integer relevance, Integer searchResultPosition) {
        return (relevance / (Math.log(searchResultPosition + 1) / Math.log(2)));
    }

    public static Double nDCG(List<Integer> relevance) {
        double dCGSum = 0;
        double iDCGSum = 0;
        List<Integer> sortRelevance = new ArrayList<>();
        sortRelevance.addAll(relevance);
        Collections.sort(sortRelevance, (o1, o2) -> {
            if (o1 > o2) {
                return -1;
            } else if (o1 < o2) {
                return 1;
            }
            return 0;
        });
        for (int i = 0; i < relevance.size(); i++) {
            dCGSum += DCG(relevance.get(i), i + 1);
            iDCGSum += DCG(sortRelevance.get(i), i + 1);
        }
        return dCGSum / iDCGSum;
    }
}
