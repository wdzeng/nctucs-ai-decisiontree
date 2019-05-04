package hyperola.io.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import hyperola.io.base.Data;
import static hyperola.io.base.Tool.RAND;
import static hyperola.io.decision.DecisionMeasurement.measure;
import static hyperola.io.decision.DecisionMeasurement.splitByThreshold;
import static hyperola.io.decision.DecisionTreeNode.grow;

public class DecisionTreeBranch implements DecisionTreeNode {

    private final int attrIndex;
    private final double threshold;
    private final int depth;
    private final DecisionTreeNode left, right;

    /**
     * Generates a branch by given splitting method, count of attributes considered and a list of selected indexes of
     * attributes
     */
    private DecisionTreeBranch(List<Data> dataSet,
                               DecisionMeasurement r,
                               Collection<Integer> attrIndexes,
                               int nAttrPicked,
                               int depth,
                               PrepruningPolicy p) {
        assert depth >= 0;
        threshold = r.threshold;
        attrIndex = r.index;
        this.depth = depth;
        List<Data> leftSet = new ArrayList<>(), rightSet = new ArrayList<>();
        splitByThreshold(dataSet, r.threshold, r.index, leftSet, rightSet);
        left = grow(leftSet, attrIndexes, nAttrPicked, depth + 1, p);
        right = grow(rightSet, attrIndexes, nAttrPicked, depth + 1, p);
    }

    /**
     * Generates a branch by minimum Gini impurity. A list of indexes of attributes and count of attributes considered
     * are given.
     */
    public DecisionTreeBranch(List<Data> dataSet,
                              Collection<Integer> attrIndexes,
                              int nAttrPicked,
                              int depth,
                              PrepruningPolicy p) {
        this(dataSet,
             measure(dataSet, randomPick(attrIndexes, nAttrPicked)),
             attrIndexes,
             attrIndexes.size(),
             depth,
             p);
    }

    private static Collection<Integer> randomPick(Collection<Integer> candidates, int count) {
        if (count >= candidates.size()) return candidates;
        List<Integer> list = new ArrayList<>(candidates);
        Collections.shuffle(list, RAND);
        return list.subList(0, count);
    }

    @Override
    public String find(Data tested) {
        return tested.getAttributeAt(attrIndex) < threshold? left.find(tested): right.find(tested);
    }

    @Override
    public int getOffspringCount() {
        return left.getOffspringCount() + right.getOffspringCount() + 1;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getDeepest() {
        return Math.max(left.getDeepest(), right.getDeepest());
    }

    @Override
    public List<Data> getData() {
        List<Data> ret = new ArrayList<>();
        ret.addAll(left.getData());
        ret.addAll(right.getData());
        return ret;
    }
}
