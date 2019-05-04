package hyperola.io.ai;

import java.util.HashMap;
import java.util.Map;
import hyperola.io.base.Data;
import hyperola.io.decision.DecisionTreeNode;
import hyperola.io.decision.ProPruningPolicy;

public abstract class RandomForestLearningMachine implements LearningMachine {

    protected DecisionTreeNode[] roots;

    public RandomForestLearningMachine(int nTree) {
        if (nTree < 1) throw new IllegalArgumentException();
        roots = new DecisionTreeNode[nTree];
    }

    @Override
    public final String predict(Data input) {
        Map<String, Integer> voting = new HashMap<>();
        for (DecisionTreeNode n: roots) {
            String lb = n.find(input);
            Integer i = voting.get(lb);
            voting.put(lb, i == null? 1: (i + 1));
        }
        String maxLb = null;
        int maxVotes = 0;
        for (Map.Entry<String, Integer> e: voting.entrySet()) {
            if (maxLb == null || e.getValue() > maxVotes) {
                maxLb = e.getKey();
                maxVotes = e.getValue();
            }
        }
        return maxLb;
    }

    @Override
    public final void proprune(ProPruningPolicy p) {
        for (DecisionTreeNode root: roots) p.tryPrune(root);
    }

    public final DecisionTreeNode[] getRoots() {
        return roots;
    }
}
