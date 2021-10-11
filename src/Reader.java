import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Reader {
    private static String path = "./data/train";

    public static Map<List<Double>, String> readFiles(String newPath, String foldersName) {
        String row;
        char[] rowData;
        Map<Character, Double> data = new HashMap<>();
        int countOfLetters = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(newPath))) {
            while ((row = reader.readLine()) != null) {
                rowData = row.toLowerCase().toCharArray();
                for (int i = 0; i < rowData.length; i++) {

                    int tmp = rowData[i];

                    if (tmp > 96 && tmp < 123) {
                        if (data.containsKey(rowData[i])) {
                            double cout = data.get(rowData[i]);
                            cout++;
                            data.put(rowData[i], cout);
                        } else {
                            data.put(rowData[i], 1.0);
                        }
                        countOfLetters++;
                    }
                }
            }
            data = normalize(countOfLetters, data);
            return toFinalMap(data, foldersName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static Map<List<Double>, String> toFinalMap(Map<Character, Double> oldData, String foldersName) {
        Map<List<Double>, String> data = new HashMap<>();
        List<Double> value = new ArrayList<>(oldData.values());
        data.put(value, foldersName);

        return data;
    }

    public static Map<List<Double>, String> readDirectories(String myPath) {
        String[] pathnames;
        String[] secondPathNames;
        String secondPath = myPath;
        String finalPath;
        File f = new File(myPath);
        pathnames = f.list();

        Map<List<Double>, String> data = new HashMap<>();
        for (String pathname : pathnames) {
            secondPath += "/" + pathname;
            f = new File(secondPath);
            secondPathNames = f.list();
            for (String secondPathName : secondPathNames) {
                finalPath = secondPath + "/" + secondPathName;
                data.putAll(readFiles(finalPath, pathname));
            }
            secondPath = myPath;
        }

        return data;
    }

    public static String[] getClasses() {
        String[] nameOfDirectories;
        File f = new File(path);
        nameOfDirectories = f.list();
        Arrays.sort(nameOfDirectories);
        return nameOfDirectories;
    }

    public static String getPath() {
        return path;
    }

    private static Map<Character, Double> normalize(int countOfLetters, Map<Character, Double> notNormalized) {
        Map<Character, Double> normalized = new HashMap<>();

        for (Map.Entry<Character, Double> pair : notNormalized.entrySet()) {
            normalized.put(pair.getKey(), pair.getValue() / countOfLetters);
        }
        return normalized;
    }

    public static List<Double> readFromConcole() {
        String text = "";
        Map<Character, Double> data = new HashMap<>();
        for (int i = 97; i < 123; i++) {
            data.put((char)i, 0.0);
        }
        Scanner sc = new Scanner(System.in);
        text = sc.nextLine();
        int countOfLetters = 0;
        char[] textArr = text.toLowerCase().toCharArray();
        for (int i = 0; i < textArr.length; i++) {
            int tmp = textArr[i];

            if (tmp > 96 && tmp < 123) {
                double cout = data.get(textArr[i]);
                cout++;
                data.put(textArr[i], cout);
                countOfLetters++;
            }
        }
        data = normalize(countOfLetters, data);

        sc.close();
        return toFinalList(data);
    }

    private static List<Double> toFinalList(Map<Character, Double> oldData){
        List<Double> data;
        data = new ArrayList<>(oldData.values());
        return data;
    }
}