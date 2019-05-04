package hyperola.io.decision;

import java.util.List;
import java.util.Objects;
import hyperola.io.base.Data;

public class DecisionTreeLeaf implements DecisionTreeNode {

    private final int depth;
    private final List<Data> data;
    private final String lb;

    public DecisionTreeLeaf(List<Data> dataSet, String lb, int depth) {
        assert depth >= 0;
        this.depth = depth;
        data = List.copyOf(dataSet);
        this.lb = Objects.requireNonNull(lb);
    }

    @Override
    public String find(Data tested) {
        return lb;
    }

    @Override
    public int getOffspringCount() {
        return 1;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getDeepest() {
        return depth;
    }

    @Override
    public List<Data> getData() {
        return data;
    }
}
