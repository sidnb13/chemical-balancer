import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class PeriodicProperties {
    public static LinkedHashMap<String,Double> elementMasses = new LinkedHashMap<>();

    public static void execute () throws FileNotFoundException {
        Scanner in = new Scanner(new File("elements.csv"));
        String[] line;
        while (in.hasNextLine()) {
            line = in.nextLine().split(",+");
            elementMasses.put(line[2],Double.parseDouble(line[3]));
        }
    }

    public static void showProperties (ArrayList<LinkedHashMap<String,Integer>> formulas, ArrayList<String> names) throws FileNotFoundException {
        execute();
        double molarMass;
        System.out.println();
        for (int i = 0; i < formulas.size(); i++) {
            molarMass = 0;
            String str = names.get(i);
            for (Map.Entry<String,Integer> pair : formulas.get(i).entrySet()) {
                molarMass += elementMasses.get(pair.getKey()) * pair.getValue();
            }
            System.out.print("Molar mass of "+str+" is ");
            System.out.printf("%.3f",molarMass);
            System.out.print(" g/mol\n");
        }
    }
}
