import java.util.Map;

public class Main {
    
    public static void main(String[] args) {
        String filename = "../resource/train.csv";
        DataReader dataReader = new DataReader(filename, true);
        double[][] numFeature = dataReader.getNumFeature();
        String[][] cateFeature = dataReader.getCateFeature();
        int[] response = dataReader.getResponse();
        int totalNum = numFeature.length;
        int testNum = totalNum / 10;
        int trainNum = totalNum - testNum;
        double[][] trainNumFeature = new double[trainNum][];
        String[][] trainCateFeature = new String[trainNum][];
        int[] trainLabel = new int[trainNum];
        for (int i = 0; i != trainNum; ++i) {
            trainNumFeature[i] = numFeature[i];
            trainCateFeature[i] = cateFeature[i];
            trainLabel[i] = response[i];
        }
        double[][] testNumFeature = new double[testNum][];
        String[][] testCateFeature = new String[testNum][];
        int[] testLabel = new int[testNum];
        for (int i = 0; i != testNum; ++i) {
            testNumFeature[i] = numFeature[i + trainNum];
            testCateFeature[i] = cateFeature[i + trainNum];
            testLabel[i] = response[i + trainNum];
        }
        Catelizer catelizer = new Catelizer(trainNumFeature);
        String[][] trainExtFeature = catelizer.getTrainExtFeature();
        String[][] testExtFeature = catelizer.getTestExtFeature(testNumFeature);
        int extFeatureNum = trainExtFeature[0].length;
        int cateFeatureNum = cateFeature[0].length;
        int totalFeatureNum = extFeatureNum + cateFeatureNum;
        String[][] trainFeature = new String[trainNum][];
        for (int i = 0; i != trainNum; ++i) {
            trainFeature[i] = new String[totalFeatureNum];
            for (int j = 0; j != extFeatureNum; ++j) {
                trainFeature[i][j] = trainExtFeature[i][j];
            }
            for (int j = 0; j != cateFeatureNum; ++j) {
                trainFeature[i][j + extFeatureNum] = trainCateFeature[i][j];
            }
        }
        double[] weight = new double[trainFeature.length];
        for (int i = 0; i != weight.length; ++i) {
            weight[i] = 1.0 / weight.length;
        }
        Bayes model = new Bayes(trainFeature, trainLabel, weight);
        String[][] testFeature = new String[testNum][];
        for (int i = 0; i != testNum; ++i) {
            testFeature[i] = new String[totalFeatureNum];
            for (int j = 0; j != extFeatureNum; ++j) {
                testFeature[i][j] = testExtFeature[i][j];
            }
            for (int j = 0; j != cateFeatureNum; ++j) {
                testFeature[i][j + extFeatureNum] = testCateFeature[i][j];
            }
        }
        int right = 0;
        for (int i = 0; i != testNum; ++i) {
            int predict = model.predict(testFeature[i]);
            if (testLabel[i] == predict) {
                ++right;
            }
        }
        double accuracy = (double)right / testNum;
        System.out.println("accuracy: " + accuracy);
    }

}
