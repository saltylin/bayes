import java.util.*;

public class Bayes {

    public Bayes(String[][] feature, int[] label, double[] weight) {
        int instanceNum = feature.length;
        int featureNum = feature[0].length;
        for (int j = 0; j != instanceNum; ++j) {
            if (!labelWeight.containsKey(label[j])) {
                labelWeight.put(label[j], weight[j]);
            } else {
                double pre = labelWeight.get(label[j]);
                labelWeight.put(label[j], pre + weight[j]);
            }
        }
        for (int i = 0; i != featureNum; ++i) {
            Map<Integer, Map<String, Double>> valMap = new HashMap<Integer, Map<String, Double>>();
            Map<Integer, Double> valWeight = new HashMap<Integer, Double>();
            for (int j = 0; j != instanceNum; ++j) {
                if (!valWeight.containsKey(label[j])) {
                    valWeight.put(label[j], weight[j]);
                } else {
                    double pre = valWeight.get(label[j]);
                    valWeight.put(label[j], pre + weight[j]);
                }
                if (!valMap.containsKey(label[j])) {
                    Map<String, Double> tmpMap = new HashMap<String, Double>();
                    valMap.put(label[j], tmpMap);
                }
                Map<String, Double> tmpMap = valMap.get(label[j]);
                if (!tmpMap.containsKey(feature[j][i])) {
                    tmpMap.put(feature[j][i], weight[j]);
                } else {
                    double pre = tmpMap.get(feature[j][i]);
                    tmpMap.put(feature[j][i], pre + weight[j]);
                }
            }
            valMapList.add(valMap);
            valTotalWeight.add(valWeight);
        }
    }

    public int predict(String[] testFeature) {
        double maxLog = -Double.MAX_VALUE;
        int result = -1;
        for (int t: labelWeight.keySet()) {
            double tmpLog = Math.log(labelWeight.get(t));
            for (int i = 0; i != testFeature.length; ++i) {
                double a = 0.0000007;
                if (valMapList.get(i).get(t).containsKey(testFeature[i])) {
                    a = valMapList.get(i).get(t).get(testFeature[i]);
                }
                double b = valTotalWeight.get(i).get(t);
                tmpLog += Math.log(a / b);
            }
            if (tmpLog > maxLog) {
                maxLog = tmpLog;
                result = t;
            }
        }
        return result;
    }

    private Map<Integer, Double> labelWeight = new HashMap<Integer, Double>();
    private List<Map<Integer, Map<String, Double>>> valMapList = new ArrayList<Map<Integer, Map<String, Double>>>();
    private List<Map<Integer, Double>> valTotalWeight = new ArrayList<Map<Integer, Double>>();

}
