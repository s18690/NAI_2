package nai;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Perceptron {
    private static double eta;
    private static double[] weights;
    private static double bias;
    private static final int NUMBER_OF_ITERATIONS = 10;
    public static final String DELIMITER = ",";
    public static final int SETOSA_NUMBER = 1;
    public static final int VERSICOLOR_NUMBER = 0;



    public static void main(String[] args) {
        double e = 0.3;
        String path = "src/data/perceptron.data";
        learn(e, path);
         double accuracy =getAccuracy("src/data/perceptron.test.data");
        System.out.println("accuracy is equal " +accuracy );
        // now interface
        double [] vector = getVector();
        var type = getAppropriateType(vector);
        System.out.println(type);

    }





    public static void learn(double e, String trainSet) {
        List<Point> learningSet;
        try {
            learningSet = readFile(trainSet);
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
        int vectorSize = learningSet.get(0).coordinates.length;
        weights = getRandomWeights(vectorSize);
        eta = e;
        bias = Math.random() * 10;
        int maxError = 0;
        int currentError;
        int counter = 0;
        do {
            currentError = iterateOverList(learningSet);
            counter++;
        }
        while (counter < NUMBER_OF_ITERATIONS && currentError > maxError);
        System.out.println("needed " + counter + " iterations");

    }

    private static double getAccuracy(String path) {
        List<Point> testData;
        int countError = 0;
        try {
            testData = readFile(path);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        for (Point p: testData){
            if(calculateIfCorrectType(p) != 0)
                countError++;
        }
        int correctNumber = testData.size()-countError;

        return (double)correctNumber/testData.size();
    }



    private static double [] getVector(){
        var scanner = new Scanner(System.in);
        System.out.println("pass number of dimensions your vector");
        int k = Integer.parseInt(scanner.nextLine());
        double [] vector = new double[k];
        for(int i = 0; i<k; ++i)
            vector[i] = Double.parseDouble(scanner.nextLine());
        return vector;
    }

    private static  IrisType getAppropriateType(double [] vector){
        double net = calculateNet(vector,weights,bias);
        int output = calculateOutput(net);
        if(output == SETOSA_NUMBER)
            return IrisType.SETOSA;
        else if(output == VERSICOLOR_NUMBER)
            return IrisType.VERSICOLOR;
        else
            throw new RuntimeException("something is wrong");
    }



    private static int iterateOverList(List<Point> learningSet) {
        int currentError = 0;
        for (Point p : learningSet) {
            int a = calculateIfCorrectType(p);
            if (a == 0)
                continue;
            calculateNewWeights(p.coordinates, a);
            calculateNewBias();
            currentError++;
        }
        return currentError;

    }

    private static void calculateNewWeights(double[] coordinates, int a) {
        for (int i = 0; i < weights.length; ++i)
            weights[i] += a * eta * coordinates[i];

    }

    private static void calculateNewBias() {
        bias += eta;
    }

    private static int calculateIfCorrectType(Point p) {
        double net = calculateNet(p.coordinates, weights, bias);
        int output = calculateOutput(net);
        int convertedType = convertTypeToNumber(p.type);
        return convertedType - output;
    }


    private static double calculateNet(double[] vector, double[] weights, double bias) {
        int sum = 0;
        for (int i = 0; i < vector.length; ++i)
            sum += vector[i] * weights[i];
        sum -= bias;
        return sum;
    }

    private static int calculateOutput(double net) {
        return net >= 0 ? 1 : 0;
    }

    private static int convertTypeToNumber(IrisType type) {
        return type == IrisType.SETOSA ? SETOSA_NUMBER : VERSICOLOR_NUMBER;
    }

    private static double[] getRandomWeights(int size) {
        double[] result = new double[size];
        for (int i = 0; i < size; ++i)
            result[i] = (Math.random() * 10);
        return result;

    }


    private static List<Point> readFile(String fileName) throws Exception {
        List<Point> result = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
            result.add(getPointFromLine(scanner.nextLine()));
        scanner.close();
        return result;
    }

    private static Point getPointFromLine(String line) {
        String[] array = line.split(DELIMITER);
        double[] coordinates = new double[array.length - 1];
        for (int i = 0; i < array.length - 1; ++i)
            coordinates[i] = Double.parseDouble(array[i]);
        IrisType type = Point.getType(array[array.length - 1]);
        return new Point(coordinates, type);
    }

}
