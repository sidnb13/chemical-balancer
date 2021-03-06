import org.apache.commons.math3.linear.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * performs the solving of a matrix, determines solutions for coefficients
 */
public class LinearAlgebra {
    private final ArrayList<String> side1;
    private final ArrayList<String> side2;

    /**
     * the constructor
     * @param s1 .
     * @param s2 .
     */
    public LinearAlgebra (ArrayList<String> s1, ArrayList<String> s2) {
        side1 = s1;
        side2 = s2;
    }

    private final ArrayList<String> t = new ArrayList<>();
    private final ArrayList<LinkedHashMap<String,Integer>> formulas = new ArrayList<>();

    /**
     * @return the matrix to be used
     */
    public double[][] getRawMatrix () {
        t.addAll(side1); t.addAll(side2);
        for (String s : t)
            formulas.add(FormulaParser.parseFormula(s));
        ArrayList<String> arr = new ArrayList<>();
        for (LinkedHashMap<String,Integer> a : formulas) {
            for (Map.Entry<String,Integer> e : a.entrySet())
                if (!arr.contains(e.getKey()))
                    arr.add(e.getKey());
        }
        double[][] mat = new double[arr.size()][formulas.size()];
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[0].length; col++) {
                String el = arr.get(row);
                mat[row][col] = formulas.get(col).get(el) == null ? 0 : formulas.get(col).get(el);
            }
        }
        return mat;
    }

    /**
     * Primary method to solve a matrix. Turns a rectangular matrix into a square one and takes leftover component
     * as the constant vector. Thus, with N coefficients, N-1 solutions would be displayed with the Nth
     * being det|A| where A is the square matrix
     */
    public void solve () throws FileNotFoundException {
        double[][] mat = getRawMatrix();
        int newLength = Math.min(mat.length, mat[0].length);
        double[][] newMat = new double[newLength][newLength];
        double[] cons = new double[newLength];
        for (int row = 0; row < newMat.length; row++) {
            for (int col = 0; col < newMat[0].length; col++) {
                newMat[row][col] = col > side1.size()-1 ? -mat[row][col] : mat[row][col];
            }
            cons[row] = mat[row][mat[0].length-1];
        }
        int count = 0;
        for (int i = 0; i < newMat.length; i++) {
            if (cons[i] == Math.abs(newMat[i][newMat[0].length-1]))
                count++;
        }
        if (count == cons.length) {
            for (int i = 0; i < newMat.length; i++) {
                newMat[i][mat[0].length-1] = 1;
            }
        }
        RealMatrix coefficients = new Array2DRowRealMatrix(newMat,false);
        RealVector constants = new ArrayRealVector(cons,false);
        RealMatrix inverse = new LUDecomposition(coefficients).getSolver().getInverse();
        RealMatrix inverseTimesDeterminant = inverse.scalarMultiply(new LUDecomposition(coefficients).getDeterminant());
        RealVector ans = inverseTimesDeterminant.operate(constants);
        double[] s = ans.toArray();
        int[] sol = new int[s.length];
        for (int i = 0; i < sol.length; i++)
            sol[i] = (int) s[i];
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i : sol)
            arr.add(i);
        arr.add((int) Math.abs(new LUDecomposition(coefficients).getDeterminant()));
        simplify(arr);
        for (int i = 0; i < t.size(); i++) {
            System.out.print((arr.get(i) == 1 ? "" : arr.get(i)) +""+t.get(i) +
                    (i != side1.size()-1 ? (i == t.size()-1 ? "" : " + ") : " --> ")+(i == t.size() - 1 ? "\n":""));
        }
        PeriodicProperties.showProperties(formulas,t);
    }

    /**
     * recursive algorithm to simplify answer vector
     * @param arr is the solution vector
     */
    public void simplify (ArrayList<Integer> arr) {
        int countEvens = 0;
        for (int i = arr.size()-1; i >= 0; i--)
            if (arr.get(i) == 0)
                arr.remove(i);
        for (int i : arr) {
            if (i % 2 == 0) {
                countEvens++;
            }
        }
        for (int i = 0; i < arr.size(); i++)
            arr.set(i,Math.abs(arr.get(i)));
        if (countEvens == arr.size() ) {
            for (int i = 0; i < arr.size(); i++)
                arr.set(i,Math.abs(arr.get(i))/2);
            simplify(arr);
        }
    }

    /**
     * For debugging purposes, printing a matrix
     * @param mat a matrix
     */
    public void printMat (double[][] mat) {
        for (double[] doubles : mat) {
            for (int col = 0; col < mat[0].length; col++) {
                System.out.print((int)doubles[col] + " ");
            }
            System.out.println();
        }
    }
}
