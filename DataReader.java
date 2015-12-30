import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class DataReader {

    public DataReader(String filename, boolean isTrainingFile) {
        String[] numAttrName = numAttrStr.split(",");
        Set<String> numAttrNameSet = new HashSet<String>();
        for (String name: numAttrName) {
            numAttrNameSet.add(name.trim());
        }
        try {
            Scanner scanner = new Scanner(new FileInputStream(new File(filename)));
            String str = scanner.nextLine();
            str = str.substring(1, str.length() - 1);
            String[] attrName = str.split("[,\"]+");
            int numFeatureNum = 0;
            int cateFeatureNum = 0;
            int attrNum = isTrainingFile? attrName.length - 2: attrName.length - 1;
            DataType[] dataType = new DataType[attrNum];
            for (int i = 0; i != dataType.length; ++i) {
                if (numAttrNameSet.contains(attrName[i + 1])) {
                    ++numFeatureNum;
                    dataType[i] = DataType.numeric;
                } else {
                    ++cateFeatureNum;
                    dataType[i] = DataType.categorical;
                }
            }
            List<Integer> idList = new ArrayList<Integer>();
            List<double[]> numFeatureList = new ArrayList<double[]>();
            List<String[]> cateFeatureList = new ArrayList<String[]>();
            List<Integer> responseList = null;
            if (isTrainingFile) {
                responseList = new ArrayList<Integer>();
            }
            while(scanner.hasNextLine()) {
                String[] attrValue = scanner.nextLine().split(",");
                double[] singleNumFeature = new double[numFeatureNum];
                String[] singleCateFeature = new String[cateFeatureNum];
                idList.add(Integer.valueOf(attrValue[0]));
                int numI = 0;
                int cateI = 0;
                for (int i = 0; i != dataType.length; ++i) {
                    if (dataType[i] == DataType.numeric) {
                        if (attrValue[i + 1].equals("")) {
                            singleNumFeature[numI++] = 0.0;
                        } else {
                            singleNumFeature[numI++] = Double.parseDouble(attrValue[i + 1]);
                        }
                    } else {
                        singleCateFeature[cateI++] = attrValue[i + 1];
                    }
                }
                numFeatureList.add(singleNumFeature);
                cateFeatureList.add(singleCateFeature);
                if (isTrainingFile) {
                    int singleResponse = Integer.parseInt(attrValue[attrValue.length - 1]);
                    responseList.add(singleResponse);
                }
            }
            scanner.close();
            id = new int[idList.size()];
            for (int i = 0; i != id.length; ++i) {
                id[i] = idList.get(i);
            }
            numFeature = numFeatureList.toArray(new double[0][]);
            cateFeature = cateFeatureList.toArray(new String[0][]);
            if (isTrainingFile) {
                response = new int[responseList.size()];
                for (int i = 0; i != response.length; ++i) {
                    response[i] = responseList.get(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public int[] getId() {
        return id;
    }

    public double[][] getNumFeature() {
        return numFeature;
    }

    public String[][] getCateFeature() {
        return cateFeature;
    }

    public int[] getResponse() {
        return response;
    }

    private int[] id;
    private double[][] numFeature;
    private String[][] cateFeature;
    private int[] response;
    private static final String numAttrStr = "Product_Info_4, Ins_Age, Ht, Wt, BMI, Employment_Info_1, Employment_Info_4, Employment_Info_6, Insurance_History_5, Family_Hist_2, Family_Hist_3, Family_Hist_4, Family_Hist_5, Medical_History_1, Medical_History_10, Medical_History_15, Medical_History_24, Medical_History_32";

}
