package hyperola.io.decision;

import java.util.List;
import hyperola.io.base.Data;

@FunctionalInterface
public interface PrepruningPolicy {

    String tryPrune(List<Data> dataSet, int depth);
}
