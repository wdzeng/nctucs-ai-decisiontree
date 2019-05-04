package hyperola.io.ai;

import java.util.Collections;
import java.util.List;
import hyperola.io.base.Data;
import hyperola.io.base.Tool;
import hyperola.io.decision.DecisionTreeNode;
import hyperola.io.decision.PrepruningPolicy;
import static hyperola.io.base.Data.getAttributeCountOfDataSet;
import static hyperola.io.base.Tool.RAND;
import static java.lang.Math.sqrt;

public class AttributeBaggingLearningMachine extends RandomForestLearningMachine {

    public AttributeBaggingLearningMachine(List<Data> dataSet, int epoch, PrepruningPolicy p) {
        this(dataSet, epoch, (int) sqrt(getAttributeCountOfDataSet(dataSet)), p);
    }

    public AttributeBaggingLearningMachine(List<Data> dataSet,
                                           int epoch,
                                           int nAttrPicked, PrepruningPolicy p) {
        super(epoch);

        final int nData = dataSet.size();
        if (nData == 0) throw new IllegalArgumentException();

        final int nTotalAttr = getAttributeCountOfDataSet(dataSet);
        if (nAttrPicked <= 0 || nAttrPicked > nTotalAttr) throw new IllegalArgumentException();

        List<Integer> indexes = List.copyOf(Tool.range(nTotalAttr));
        Collections.shuffle(dataSet, RAND);
        for (int i = 0; i < epoch; i++) {
            roots[i] = DecisionTreeNode.grow(dataSet, indexes, nAttrPicked, 0, p);
        }
    }
}
