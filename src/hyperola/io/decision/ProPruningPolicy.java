package hyperola.io.decision;

@FunctionalInterface
public interface ProPruningPolicy {

    default void tryPrune(DecisionTreeNode n) {
        if (n instanceof DecisionTreeBranch) {
            prune((DecisionTreeBranch) n);
        }
    }

    void prune(DecisionTreeBranch n);
}
