import java.io.FileNotFoundException;
import java.util.*;

/**
 * Effectively uses parsing and matrices to balance a chemical equation
 * Also contains the I/O
 */
public class Balance {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        try {
            System.out.print("\nPlease enter the chemical equation: ");
            String equation = s.nextLine().replaceAll("\\s", "");
            System.out.println("Wish to continue? [Y/N]");
            String yn = s.nextLine().toUpperCase();
            if (yn.equals("Y")) {
                String[] sides = equation.split("(-->)|(=)|(--->)|(->)|(==>)");
                String[] side1 = sides[0].split("\\+");
                String[] side2 = sides[1].split("\\+");

                for (int i = 0; i < side1.length; i++)
                    side1[i] = FormulaParser.removeCoefficient(side1[i]);
                for (int i = 0; i < side2.length; i++)
                    side2[i] = FormulaParser.removeCoefficient(side2[i]);

                ArrayList<String> s1 = new ArrayList<>(Arrays.asList(side1));
                ArrayList<String> s2 = new ArrayList<>(Arrays.asList(side2));
                LinearAlgebra solution = new LinearAlgebra(s1, s2);
                System.out.println("\nThe balanced equation is:\n");
                solution.solve();

            } else {
                Date d = new Date();
                System.out.printf("Logged out at: %tT%n",d);
                System.out.println();
            }
        } catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
            System.err.println("\nTry again and enter a valid formula. Ensure the progression is \"-->\" not" +
                    " \"--->\" and that extraneous symbols are not included.\n");
        }
    }
}
