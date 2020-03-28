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
            if (str.contains("(")) {
                int j;
                for (j = 0; j < str.length(); j++) {
                    if (str.charAt(j) == ')')
                        break;
                }
                String polyatomic = str.substring(str.indexOf("(")+1,str.indexOf(")"));
                LinkedHashMap<String,Integer> poly = FormulaParser.parseFormula(polyatomic);
                for (Map.Entry<String,Integer> e : poly.entrySet())
                    e.setValue(e.getValue()*formulas.get(i).get(polyatomic));
                formulas.get(i).remove(polyatomic);
                int finalI = i;
                poly.forEach((k, v) -> formulas.get(finalI).merge(k, v, (key, val) -> v + val));
            }
            for (Map.Entry<String,Integer> pair : formulas.get(i).entrySet()) {
                molarMass += elementMasses.get(pair.getKey()) * pair.getValue();
            }
            System.out.print("Molar mass of "+str+" is ");
            System.out.printf("%.3f",molarMass);
            System.out.print(" g/mol\n");
        }
    }
}
