import java.util.*;

public class Catelizer {

    public Catelizer(double[][] trainNumFeature) {
        this.trainNumFeature = trainNumFeature;
        int featureNum = trainNumFeature[0].length;
        int instanceNum = trainNumFeature.length;
        for (int i = 0; i != featureNum; ++i) {
            double avg = 0.0;
            for (double[] t: trainNumFeature) {
                avg += t[i];
            }
            avg /= instanceNum;
            double sum = 0.0;
            for (double[] t: trainNumFeature) {
                double sub = t[i] - avg;
                sum += sub * sub;
            }
            sum /= instanceNum;
            double interval = 6 * Math.sqrt(sum) / splitNum;
            meanList.add(avg);
            intervalList.add(interval);
        }
    }

    public String[][] getTrainExtFeature() {
        return catelize(trainNumFeature);
    }

    public String[][] getTestExtFeature(double[][] testNumFeature) {
        return catelize(testNumFeature);
    }

    private String[][] catelize(double[][] data) {
        String[][] result = new String[data.length][];
        for (int i = 0; i != result.length; ++i) {
            result[i] = new String[data[0].length];
        }
        for (int i = 0; i != result[0].length; ++i) {
            double mean = meanList.get(i);
            double interval = intervalList.get(i);
            double left = mean - (splitNum / 2) * interval;
            double right = mean + (splitNum / 2) * interval;
            for (int j = 0; j != result.length; ++j) {
                double x = data[j][i];
                if (x <= left) {
                    result[j][i] = "-1";
                } else if (x >= right) {
                    result[j][i] = String.valueOf(splitNum);
                } else {
                    int t = (int)((x - left) / interval);
                    result[j][i] = String.valueOf(t);
                }
            }
        }
        return result;
    }

    private double[][] trainNumFeature;
    private static final int splitNum = 16;
    private List<Double> meanList = new ArrayList<Double>();
    private List<Double> intervalList = new ArrayList<Double>();

}
