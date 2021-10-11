import java.util.*;

public class Main {
    public static void main(String[] args) {

        Map<List<Double>, String> trainData = Reader.readDirectories("./data/train");
        Map<List<Double>, String> testData = Reader.readDirectories("./data/testset");


        Scanner sc1 = new Scanner(System.in);
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of classes: ");
        int numberOfClasses = sc1.nextInt();
        System.out.print("Enter number of inputs: ");
        int numberOfInputs = sc1.nextInt();
        System.out.print("Enter your alpha: ");
        double alpha = sc.nextDouble();

        NeuralNetwork nn = new NeuralNetwork(numberOfClasses,numberOfInputs,alpha);
        nn.train(trainData);

        System.out.print("Enter your input vector: ");
        List<Double> input = Reader.readFromConcole();
        System.out.println(nn.singlePredict(input));
//        nn.predict(testData);

        sc.close();
        sc1.close();
    }

}
