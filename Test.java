import java.util.Map;
import java.io.*;

public class Test {
    
    public static void main(String[] args) throws IOException{
        String[] filename = {"../resource/train.csv", "../resource/test.csv"};
        DataReader trainDataReader = new DataReader(filename[0], true);
        double[][] trainNumFeature = trainDataReader.getNumFeature();
        String[][] trainCateFeature = trainDataReader.getCateFeature();
        int[] trainResponse = trainDataReader.getResponse();
        DataReader testDataReader = new DataReader(filename[1], false);
        double[][] testNumFeature = testDataReader.getNumFeature();
        String[][] testCateFeature = testDataReader.getCateFeature();
        int[] testId = testDataReader.getId();
        Catelizer catelizer = new Catelizer(trainNumFeature);
        String[][] trainExtFeature = catelizer.getTrainExtFeature();
        String[][] testExtFeature = catelizer.getTestExtFeature(testNumFeature);
        int extFeatureNum = trainExtFeature[0].length;
        int cateFeatureNum = trainCateFeature[0].length;
        int totalFeatureNum = extFeatureNum + cateFeatureNum;
        int trainNum = trainNumFeature.length;
        int testNum = testNumFeature.length;
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
        Bayes model = new Bayes(trainFeature, trainResponse, weight);
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
        PrintWriter pw = new PrintWriter(new FileWriter(new File("../result.csv")));
        pw.println("\"Id\",\"Response\"");
        for (int i = 0; i != testNum; ++i) {
            int predict = model.predict(testFeature[i]);
            pw.println(testId[i] + "," + predict);
        }
        pw.close();
    }

}
