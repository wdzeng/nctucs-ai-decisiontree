package hyperola.io.base;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toList;

public class Tool {

    public static final Random RAND = new Random(System.currentTimeMillis());

    public static List<Integer> range(int n) {
        if (n <= 1) throw new IllegalArgumentException();
        return IntStream.range(0, n).boxed().collect(toList());
    }

    private Tool(){}
}
