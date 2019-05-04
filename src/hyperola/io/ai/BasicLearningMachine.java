package hyperola.io.ai;

import java.util.ArrayList;
import java.util.List;
import hyperola.io.base.Data;
import hyperola.io.decision.DecisionTreeNode;
import hyperola.io.decision.PrepruningPolicy;
import hyperola.io.decision.ProPruningPolicy;
import static hyperola.io.base.Data.getAttributeCountOfDataSet;
import static hyperola.io.base.Tool.range;

public class BasicLearningMachine implements LearningMachine {

    private final DecisionTreeNode root;

    public BasicLearningMachine(List<Data> dataSet) {
        this(dataSet, null);
    }

    public BasicLearningMachine(List<Data> dataSet, PrepruningPolicy p) {
        List<Integer> intList = new ArrayList<>();
        int nAttr = getAttributeCountOfDataSet(dataSet);
        root = DecisionTreeNode.grow(dataSet, range(nAttr), nAttr, 0, p);
    }

    @Override
    public String predict(Data input) {
        return root.find(input);
    }

    @Override
    public void proprune(ProPruningPolicy p) {
        p.tryPrune(root);
    }

    public DecisionTreeNode getRoot() {
        return root;
    }
}
