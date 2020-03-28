import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing relevant methods to deal with chemical formulas
 */
public class FormulaParser {
    /**
     * @param formulas contains the formulas of one side of the equation
     * @return a map with the merged values of each formula's map
     */
    public static LinkedHashMap<String,Integer> getSideAtoms (String[] formulas) {
        ArrayList<LinkedHashMap<String,Integer>> arr = new ArrayList<>();
        for (String formula : formulas) arr.add(parseFormula(formula));
        LinkedHashMap<String,Integer> res = new LinkedHashMap<>();
        for (LinkedHashMap<String,Integer> map : arr) {
            map.forEach((k, v) -> res.merge(k, v, (key, val) -> v + val));
        }
        return res;
    }

    /**
     * Uses stack algorithm
     * @param formula is a chemical formula
     * @return a map of each element and its frequency
     */
    public static LinkedHashMap<String, Integer> parseFormula(String formula) {
        Pattern p = Pattern.compile("([A-Z][a-z]*)(\\d*)|(\\()|(\\))(\\d*)");
        Matcher m = p.matcher(removeCoefficient(formula)); //removes coefficients as input here
        Stack<LinkedHashMap<String, Integer>> stack = new Stack();
        stack.push(new LinkedHashMap<>());

        int mx;
        while (m.find()) {
            String match = m.group();
            if (match.equals("(")) {
                stack.push(new LinkedHashMap<>());
            } else if (match.startsWith(")")) {
                LinkedHashMap<String, Integer> top = stack.pop();
                mx = match.length() > 1 ? Integer.parseInt(match.substring(1)) : 1;
                for (String name: top.keySet()) {
                    stack.peek().put(name, stack.peek().getOrDefault(name, 0) + top.get(name) * mx);
                }
            } else {
                int i = 1;
                while (i < match.length() && Character.isLowerCase(match.charAt(i))) {
                    i++;
                }
                String name = match.substring(0, i);
                mx = i < match.length() ? Integer.parseInt(match.substring(i)) : 1;
                stack.peek().put(name, stack.peek().getOrDefault(name, 0) + mx);
            }
        }
        return stack.peek();
    }

    /**
     * secondary modified algorithm to aid in balancing with polyatomic ions
     * @param formula a formula
     * @return a element-frequency map
     */
    public static LinkedHashMap<String, Integer> parseFormula1(String formula) {
        Pattern p = Pattern.compile("([A-Z][a-z]*)(\\d*)|(\\()|(\\))(\\d*)");
        Matcher m = p.matcher(removeCoefficient(formula)); //removes coefficients as input here
        Stack<LinkedHashMap<String, Integer>> stack = new Stack();
        stack.push(new LinkedHashMap<>());

        int mx;
        while (m.find()) {
            String match = m.group();
            if (match.equals("(")) {
                stack.push(new LinkedHashMap<>());
            } else if (match.startsWith(")")) {
                LinkedHashMap<String, Integer> top = stack.pop();
                mx = match.length() > 1 ? Integer.parseInt(match.substring(1)) : 1;
                stack.peek().put(formula.substring(formula.indexOf("(")+1,formula.indexOf(")")),mx);
            } else {
                int i = 1;
                while (i < match.length() && Character.isLowerCase(match.charAt(i))) {
                    i++;
                }
                String name = match.substring(0, i);
                mx = i < match.length() ? Integer.parseInt(match.substring(i)) : 1;
                stack.peek().put(name, stack.peek().getOrDefault(name, 0) + mx);
            }
        }
        return stack.peek();
    }

    /**
     * to streamline balancing process
     * @param formula a string formula
     * @return the "cleaned" formula
     */
    public static String removeCoefficient (String formula) {
        int i;
        for (i = 0; i < formula.length(); i++)
            if (!Character.isDigit(formula.charAt(i)))
                break;
        return formula.substring(i);
    }
}

