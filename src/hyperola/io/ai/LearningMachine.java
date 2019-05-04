package hyperola.io.ai;

import java.util.Collection;
import hyperola.io.base.Data;
import hyperola.io.decision.ProPruningPolicy;

public interface LearningMachine {

    String predict(Data input);

    default double test(Collection<Data> tested) {
        int nCorr = 0;
        for (Data d: tested) if (predict(d).equals(d.getLabel())) nCorr++;
        return (double) nCorr / tested.size();
    }

    void proprune(ProPruningPolicy p);
}
