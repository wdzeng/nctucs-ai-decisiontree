package hyperola.io.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import static java.util.stream.Collectors.toSet;

public class Data {

    protected final double[] attrs;
    protected final String label;

    public Data(Data src, Collection<Integer> attrList) {
        int nAttr = attrList.size();
        attrs = new double[nAttr];
        int c = 0;
        for (int attrIndex: attrList) attrs[c++] = src.getAttributeAt(attrIndex);
        label = src.label;
    }

    public Data(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException();
        attrs = new double[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) attrs[i] = Double.parseDouble(args[i]);
        label = args[args.length - 1];
    }

    public static List<Data> importData(InputStream in) throws IOException {
        Set<Data> set = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String readed;
            while ((readed = br.readLine()) != null) {
                String[] args = readed.trim().split("\\s+|,");
                if (args.length <= 1) continue;
                set.add(new Data(args));
            }
        }
        return new ArrayList<>(set);
    }

    public static int getAttributeCountOfDataSet(List<Data> dataSet) {
        return dataSet.get(0).getAttributeCount();
    }

    public static int getLabelCountOfDataSet(Collection<Data> dataSet) {
        return dataSet.stream().map(Data::getLabel).collect(toSet()).size();
    }

    public int getAttributeCount() { return attrs.length; }

    public double getAttributeAt(int index) { return attrs[index]; }

    public String getLabel() { return label; }

    @Override
    public int hashCode() {
        return Arrays.hashCode(attrs) ^ label.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Data) {
            Data d = (Data) o;
            return Arrays.equals(attrs, d.attrs) && label.equals(d.label);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[ ");
        boolean first = true;
        for (double d: attrs) {
            if (first) first = false;
            else s.append(", ");
            s.append(d);
        }
        s.append(" ] ");
        s.append(label);
        return s.toString();
    }
}
