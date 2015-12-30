import java.util.*;

public class Adaboost {

    public Adaboost(String[][] feature, int[] label) {
        int instanceNum = feature.length;
        double[] w = new double[instanceNum];
        for (int i = 0; i != instanceNum; ++i) {
            w[i] = 1.0 / instanceNum;
        }
        double rem = Math.log(7);
        boolean[] flag = new boolean[instanceNum];
        for (int i = 0; i != 1; ++i) {
            Bayes bayes = new Bayes(feature, label, w);
            double error = 0.0;
            for (int j = 0; j != instanceNum; ++j) {
                int predict = bayes.predict(feature[j]);
                if (predict != label[j]) {
                    flag[j] = false;
                    error += w[j];
                } else {
                    flag[j] = true;
                }
            }
            double a = Math.log((1.0 - error) / error) + rem;
            a = a - (int)a;
            double wrongFactor = Math.exp(-a);
            double rightFactor = Math.exp(a);
            System.out.println(a + " " + wrongFactor + " " + rightFactor);/////////////
            double sum = 0.0;
            for (int j = 0; j != instanceNum; ++j) {
                if (flag[j]) {
                    w[j] *= rightFactor;
                } else {
                    w[j] *= wrongFactor;
                }
                sum += w[j];
            }
            for (int j = 0; j != instanceNum; ++j) {
                w[j] /= sum;
            }
            bayesList.add(bayes);
            aList.add(a);
        }
    }

    public int predict(String[] feature) {
        double[] vote = new double[9];
        for (int i = 0; i != bayesList.size(); ++i) {
            int pred = bayesList.get(i).predict(feature);
            vote[pred] += aList.get(i);
        }
        double maxVote = 0.0;
        int result = 0;
        for (int i = 1; i != 9; ++i) {
            if (vote[i] > maxVote) {
                maxVote = vote[i];
                result = i;
            }
        }
        return result;
    }

    private List<Bayes> bayesList = new ArrayList<Bayes>();
    private List<Double> aList = new ArrayList<Double>();

}
