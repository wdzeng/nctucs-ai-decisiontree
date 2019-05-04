package hyperola.io.ai;

import java.util.ArrayList;
import java.util.List;
import hyperola.io.base.Data;
import hyperola.io.base.Tool;
import hyperola.io.decision.DecisionTreeNode;
import hyperola.io.decision.PrepruningPolicy;
import static java.lang.Math.sqrt;

public class CrossValidationLearningMachine extends RandomForestLearningMachine {

    private final double[] accTrn, accVal, accTst;

    public CrossValidationLearningMachine(List<Data> trnSet, List<Data> tstSet, int epoch, PrepruningPolicy p) {
        this(trnSet, tstSet, epoch, (int) sqrt(Data.getAttributeCountOfDataSet(trnSet)), p);
    }

    public CrossValidationLearningMachine(List<Data> trnSet,
                                          List<Data> tstSet,
                                          int epoch,
                                          int nAttrPicked,
                                          PrepruningPolicy p) {
        super(epoch);
        accTrn = new double[epoch];
        accVal = new double[epoch];
        accTst = tstSet == null? null: new double[epoch];
        List<Integer> range = Tool.range(Data.getAttributeCountOfDataSet(trnSet));
        List<List<Data>> trnSubsets = split(trnSet, epoch);
        for (int i = 0; i < epoch; i++) learn(trnSubsets, tstSet, i, range, nAttrPicked, p);
    }

    private void learn(List<List<Data>> trnSubsets,
                       List<Data> tstSet,
                       final int t,
                       List<Integer> range,
                       int nAttrPicked,
                       PrepruningPolicy p) {
        List<Data> trnList = pick(trnSubsets, t), valList = trnSubsets.get(t);
        roots[t] = DecisionTreeNode.grow(trnList, range, nAttrPicked, 0, p);
        double acTrn = 0, acVal = 0, acTst = 0;
        for (int i = 0; i <= t; i++) {
            acTrn += roots[i].test(trnList);
            acVal += roots[i].test(valList);
            if (tstSet != null) acTst += roots[i].test(tstSet);
        }
        accTrn[t] = acTrn / (t + 1);
        accVal[t] = acVal / (t + 1);
        if (tstSet != null) accTst[t] = acTst / (t + 1);
    }

    private static List<Data> pick(List<List<Data>> subsets, int except) {
        List<Data> ret = new ArrayList<>();
        final int size = subsets.size();
        for (int i = 0; i < size; i++) if (i != except) ret.addAll(subsets.get(i));
        return ret;
    }

    public double getTrainingAccuracyAtEpoch(int epoch) {
        return accTrn[epoch];
    }

    public double getValidationAccuracyAtEpoch(int epoch) {
        return accVal[epoch];
    }

    public double getTestingAccuracyAtEpoch(int epoch) {return accTst == null? 0: accTst[epoch];}

    private static List<List<Data>> split(List<Data> src, int nGroup) {
        List<List<Data>> ret = new ArrayList<>(nGroup);
        int size = src.size();
        double n = (double) size / nGroup;
        if (n < 1) throw new IllegalArgumentException();
        for (int i = 0; i < nGroup; i++) {
            int i0 = (int) Math.round(n * i), i1 = (int) Math.round(n * (i + 1));
            ret.add(new ArrayList<>(src.subList(i0, i1)));
        }
        return ret;
    }
}
