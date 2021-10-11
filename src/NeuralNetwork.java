import java.util.*;

public class NeuralNetwork {
    private int numOfClasses;
    private int numOfInputs;
    private double[][] weights;
    private double alpha;


    public NeuralNetwork(int numOfClasses, int numOfInputs, double alpha) {
        this.numOfClasses = numOfClasses;
        this.numOfInputs = numOfInputs;
        this.alpha = alpha;
        double[][] randomWeights = new double[numOfClasses][numOfInputs + 1];
        for (int i = 0; i < numOfClasses; i++) {
            for (int j = 0; j < numOfInputs; j++) {
                randomWeights[i][j] = Math.random() * 10;
            }
        }
        weights = randomWeights;
    }

    public void train(Map<List<Double>, String> trainset) {
        double[][] predict;
        double error = 100;

        List<double[][]> predictions = new ArrayList<>();
        List<double[][]> realY = new ArrayList<>();

        while (error > 5.0) {
            for (Map.Entry<List<Double>, String> pair : trainset.entrySet()) {
                predict = predict(pair.getKey());
                train(pair.getKey(), pair.getValue(), predict);
                predictions.add(predict);
                realY.add(classToVector(pair.getValue()));

            }
            error = mse(realY, predictions);

            realY.clear();
            predictions.clear();
        }
    }

    private double error(double[][] real, double[][] predict){
        double error = 0;
        for (int i = 0; i < real.length ; i++) {
            error += Math.pow(real[i][0] - predict[i][0], 2);
        }
        return error;
    }

    private double mse(List<double[][]> realY, List<double[][]> predictions){
        double error = 0;
        for (int i = 0; i < realY.size(); i++) {
            error += error(realY.get(i), predictions.get(i));
        }
        return error;
    }

    private void train(List<Double> list, String nameOfClass, double[][] predict) {
        List<Double> copyList = new ArrayList<>(list);
        copyList.add(-1.0);
        double[][] sigmaError = dotProduct(dotProduct(subtract(classToVector(nameOfClass), predict), predict), subtract(generateVectorOfUnits(), predict));
        double[][] newWeights = multiplyMatrices(sigmaError, toArray(copyList));
        weights = sum(weights, multiplyMatrixBy(alpha, newWeights));
    }

    public void predict(Map<List<Double>, String> testSet) {
        for (Map.Entry<List<Double>, String> pair : testSet.entrySet()) {
            double[][] prediction = predict(pair.getKey());
            System.out.println(vectorToClass(prediction) + " =? " + pair.getValue());
        }
    }

    public double[][] predict(List<Double> list) {
        double[][] input = toArray(list);
        double[][] pred = sigmoid(multiplyMatrices(weights, transposeMatrix(input)));
//        System.out.println(Arrays.deepToString(pred));
        return pred;
    }

    public String singlePredict(List<Double> list) {
        return vectorToClass(predict(list));
    }

    private double[][] generateVectorOfUnits() {
        double[][] units = new double[numOfClasses][1];
        for (int i = 0; i < numOfClasses; i++) {
            units[i][0] = 1;
        }
        return units;
    }

    private double[][] toArray(List<Double> list) {
        double[][] arr = new double[1][list.size()];
        for (int i = 0; i < arr[0].length; i++) {
            arr[0][i] = list.get(i);
        }
        return arr;
    }

    static double[][] subtract(double a[][], double b[][]) {

        double c[][] = new double[a.length][a[0].length];

        for (int i = 0; i < c.length; i++)
            for (int j = 0; j < c[i].length; j++)
                c[i][j] = a[i][j] - b[i][j];

        return c;
    }

    static double[][] sum(double a[][], double b[][]) {
        double c[][] = new double[a.length][a[0].length];

        for (int i = 0; i < c.length; i++)
            for (int j = 0; j < c[i].length; j++)
                c[i][j] = a[i][j] + b[i][j];

        return c;
    }

    public double[][] transposeMatrix(double[][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }

    public double[][] sigmoid(double[][] arr) {
        double[][] tmpArr = new double[arr.length][1];
        for (int i = 0; i < tmpArr.length; i++) {
            tmpArr[i][0] = 1 / (1 + Math.pow(Math.E, -arr[i][0]));
        }
        return tmpArr;
    }

    public double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }

        return result;
    }

    public double[][] dotProduct(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][firstMatrix[0].length];
        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < firstMatrix[i].length; j++) {
                result[i][j] = firstMatrix[i][j] * secondMatrix[i][j];
            }
        }
        return result;
    }

    private double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }

    private double[][] multiplyMatrixBy(double num, double[][] matrix) {
        double[][] outputMatrix = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < outputMatrix.length; i++) {
            for (int j = 0; j < outputMatrix[0].length; j++) {
                outputMatrix[i][j] = num * matrix[i][j];
            }
        }
        return outputMatrix;
    }

    private static double[][] classToVector(String classToVector) {
        String[] nameOfClasses = Reader.getClasses();
        double[][] vector = new double[nameOfClasses.length][1];

        for (int i = 0; i < vector.length; i++) {
            if (nameOfClasses[i].equals(classToVector)) {
                vector[i][0] = 1.0;
            } else vector[i][0] = 0.0;
        }
        return vector;
    }

    private static String vectorToClass(double[][] vector) {
        int maxInd = maxInd(vector);
        String[] classes = Reader.getClasses();
        return classes[maxInd];
    }


    public static int maxInd(double[][] array){
        int index = 0;
        double max = array[0][0];
        for (int i = 1; i < array.length ; i++) {
            if (array[i][0] > max) {
                max = array[i][0];
                index = i;
            }
        }

        return index;
    }

}
