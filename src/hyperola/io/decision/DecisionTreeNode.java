package hyperola.io.decision;

import java.util.Collection;
import java.util.List;
import hyperola.io.base.Data;
import static hyperola.io.decision.DecisionMeasurement.isSingleValue;
import static java.util.stream.Collectors.toList;

public interface DecisionTreeNode {

    static DecisionTreeNode grow(List<Data> dataSet,
                                 Collection<Integer> orgAttrCandidates,
                                 int nAttrCandidates,
                                 int depth,
                                 PrepruningPolicy p) {
        final String lb = dataSet.get(0).getLabel();
        if (dataSet.stream().allMatch(e -> e.getLabel().equals(lb))) return new DecisionTreeLeaf(dataSet, lb, depth);

        if (p != null) {
            String plb = p.tryPrune(dataSet, depth);
            if (plb != null) return new DecisionTreeLeaf(dataSet, plb, depth);
        }

        Collection<Integer> duplicatedAttributes = getDuplicatedAttributes(dataSet, orgAttrCandidates);
        assert !duplicatedAttributes.isEmpty();
        return new DecisionTreeBranch(dataSet, duplicatedAttributes, nAttrCandidates, depth, p);
    }

    static Collection<Integer> getDuplicatedAttributes(List<Data> dataSet, Collection<Integer> candidates) {
        return candidates.stream()
                         .filter(i -> !isSingleValue(dataSet, i))
                         .collect(toList());
    }

    String find(Data tested);

    default double test(Collection<Data> tested) {
        if (tested.isEmpty()) throw new IllegalArgumentException();
        long n = tested.stream().filter(e -> e.getLabel().equals(find(e))).count();
        return (double) n / tested.size();
    }

    int getOffspringCount();

    int getDepth();

    int getDeepest();

    List<Data> getData();
}
