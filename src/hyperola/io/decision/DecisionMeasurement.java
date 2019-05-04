package hyperola.io.decision;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hyperola.io.base.Data;
import static java.util.Comparator.comparingDouble;

public class DecisionMeasurement {

    public final int index;
    public final double threshold;
    private final double impurity;

    private DecisionMeasurement(int index, double threshold, double impurity) {
        this.index = index;
        this.threshold = threshold;
        this.impurity = impurity;
    }

    private static void requireValidDataSet(List<Data> dataSet) {
        assert Data.getAttributeCountOfDataSet(dataSet) >= 2;
    }

    public static DecisionMeasurement measure(List<Data> dataSet, Collection<Integer> attrCandidates) {
        requireValidDataSet(dataSet);
        DecisionMeasurement best = null;
        for (int i: attrCandidates) {
            DecisionMeasurement d = measureByAttribute(dataSet, i);
            if (best == null || d.impurity < best.impurity) best = d;
        }
        assert best != null;
        return best;
    }

    public static DecisionMeasurement measureByAttribute(List<Data> dataSet, final int attrIndex) {
        requireValidDataSet(dataSet);
        assert !isSingleValue(dataSet, attrIndex);

        final int size = dataSet.size();
        dataSet.sort(comparingDouble(a -> a.getAttributeAt(attrIndex)));

        double prevVal = dataSet.get(0).getAttributeAt(attrIndex);
        double minGini = Double.MAX_VALUE;
        double bestThreshold = Double.MIN_VALUE;
        for (int i = 1; i < size; i++) {
            double curVal = dataSet.get(i).getAttributeAt(attrIndex);
            if (curVal == prevVal) continue;
            double threshold = (curVal + prevVal) / 2;
            List<Data> left = dataSet.subList(0, i), right = dataSet.subList(i, size);
            double mgLeft = getGiniImpurity(left), mgRight = getGiniImpurity(right);
            double comparedGini = mgLeft * left.size() / size + mgRight * right.size() / size;
            if (comparedGini < minGini) {
                minGini = comparedGini;
                bestThreshold = threshold;
            }
            prevVal = curVal;
        }

        assert bestThreshold != Double.MIN_VALUE;
        return new DecisionMeasurement(attrIndex, bestThreshold, minGini);
    }

    public static double getGiniImpurity(Collection<Data> dataSet) {
        final int size = dataSet.size();
        if (size == 0) throw new IllegalArgumentException();
        if (size == 1) return 0;

        Map<Object, Integer> map = new HashMap<>();
        for (Data d: dataSet) {
            Object lb = d.getLabel();
            int c = map.containsKey(lb)? (map.get(lb) + 1): 1;
            map.put(lb, c);
        }

        double gini = 1.0;
        for (double c: map.values()) {
            c /= size;
            gini -= (c * c);
        }
        return gini;
    }

    public static void splitByThreshold(Collection<Data> src,
                                        double threshold,
                                        int attrIndex,
                                        Collection<? super Data> left,
                                        Collection<? super Data> right) {
        for (Data d: src) {
            if (d.getAttributeAt(attrIndex) < threshold) left.add(d);
            else right.add(d);
        }
    }

    public static boolean isSingleValue(List<Data> dataSet, int attrIndex) {
        double val = dataSet.get(0).getAttributeAt(attrIndex);
        return dataSet.stream().allMatch(e -> e.getAttributeAt(attrIndex) == val);
    }

    @Override
    public String toString() {
        return String.format("attr-index: %d, threshold: %f", index, threshold);
    }
}
