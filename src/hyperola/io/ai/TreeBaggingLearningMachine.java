package hyperola.io.ai;

import java.util.List;
import hyperola.io.base.Data;
import hyperola.io.base.Tool;
import hyperola.io.decision.DecisionTreeNode;
import hyperola.io.decision.PrepruningPolicy;
import static hyperola.io.base.Data.getAttributeCountOfDataSet;
import static hyperola.io.base.Tool.range;
import static java.util.Collections.shuffle;
import static java.util.List.copyOf;

public class TreeBaggingLearningMachine extends RandomForestLearningMachine {

    public TreeBaggingLearningMachine(List<Data> dataSet, int epoch, double trainingRate, PrepruningPolicy p) {
        super(epoch);

        final int nData = dataSet.size();
        if (nData == 0) throw new IllegalArgumentException();

        final int nTrained = (int) (trainingRate * nData);
        if (nTrained < 1 || nTrained > nData) throw new IllegalArgumentException();

        List<Data> selectedData = dataSet.subList(0, nTrained);
        List<Integer> attrList = copyOf(range(getAttributeCountOfDataSet(dataSet)));
        roots = new DecisionTreeNode[epoch];
        for (int i = 0; i < epoch; i++) {
            shuffle(dataSet, Tool.RAND);
            roots[i] = DecisionTreeNode.grow(selectedData, attrList, attrList.size(), 0, p);
        }
    }
}
