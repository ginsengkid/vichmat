import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class matrix {
    private double[][] array;
    private int rowAmount, columnAmount;
    double epsilon;
    double [] sums;


    private void create(int k, int l) {
        this.array = new double[k][];
        int i;
        for (i = 0; i < k; i++)
            this.array[i] = new double[l];
    }


    public void print() {
        int i, j;
        for (i = 0; i < rowAmount; i++) {
            for (j = 0; j < columnAmount; j++)
                System.out.printf("%15.6E", array[i][j]);
            System.out.println();
        }
        System.out.println();
    }


    public void init(String s) throws FileNotFoundException {
        File file = new File(s);
        Scanner scan = new Scanner(file);
        Pattern pat = Pattern.compile("[ \t]+");
        String str = scan.nextLine();
        String[] sn = pat.split(str);
        rowAmount = Integer.parseInt(sn[0]);
        columnAmount = Integer.parseInt(sn[1]) + 1;
        epsilon = Math.pow(10, -Double.parseDouble(sn[2]) - 1);
        this.create(rowAmount, columnAmount);
        int i, j;
        for (i = 0; i < rowAmount; i++) {
            str = scan.nextLine();
            sn = pat.split(str);
            for (j = 0; j < columnAmount; j++)
                array[i][j] = Double.parseDouble(sn[j]);
        }
        scan.close();
        sums = sumOfLines();
    }


    public int  makeTriangle (){
        double multiplier;
        int k;
        int needed_line;
        //finding and swapping not zero line
        for (int i = 1; i < rowAmount; i++) {

            needed_line = i;
            if (compareToZero(array[i][i])) {
                needed_line = findNotZeroElement(i);
                if(needed_line == -1)
                    return (3);
                swapLines(needed_line, i);
            }

            //making column to zero
            k = i;
            for (int j = i; j < rowAmount; j ++) {
                multiplier = array[k][i-1]/ array[i-1][i-1];
                multiplicationAndSubtractionOfLine(multiplier, i, j);
                k++;
            }
        }
        return(4);
    }


    private int findNotZeroElement(int i) {
        if (!compareToZero(array[i][i])) return i;
        for (int q = i + 1; q < rowAmount - i; q++)
            if (!compareToZero(array[q][i]))
                return q;
        return -1;
    }

    //comparing with 0, considering accuracy
    private boolean compareToZero(double a){
        return (Math.abs(a) < epsilon);
    }


    private void swapLines(int i, int q){
        double[] temp = array[q];
        array[q] = array[i];
        array[i] = temp;
    }

    // k - number of iteration, i - number of line.
    private void multiplicationAndSubtractionOfLine(double multiplier, int k, int i){
        for (int j = 0; j < columnAmount; j++){
            array[i][j] -= (multiplier * array[k-1][j]);
            if (compareToZero(array[i][j]))
                array[i][j] = 0;
        }
    }


    public int checkSolutions() {
        if (compareToZero(array[rowAmount -1][columnAmount-2]))
            if (compareToZero(array[rowAmount -1][columnAmount-1]))
                return 1;    // 0 = 0
            else
                return 2;   // 0 = (!0)
        return 4;           // have only 1 solution
    }

    public double[] findSolutions() {
        /*
        1st "for" is going up through matrix
        2nd "for" is going on line, calculating summaries
        of the matching roots and their coefficients
         */
        double[] final_array = new double[rowAmount];
        for (int i = rowAmount - 1; i >= 0; i--){
            double summary = 0;
            for (int j = i + 1; j < rowAmount; j++){
                summary += array[i][j] * final_array[j];
            }
            final_array[i] += (array[i][rowAmount] - summary) / array[i][i];
        }
        return final_array;
    }


    //iterative method//

    public boolean checkDiagonalForZero(){
        int counter = 0;
        for (int i = 0; i < rowAmount; i++) {
            if (array[i][i] == 0)
                counter++;
        }
        return counter == rowAmount;
    }

    /*
    SCC - sufficient condition for convergence
    (predominance of the diagonal elements)
    */

    public boolean checkForZeros() {
        for (int i = 0; i < rowAmount; i++)
            if (compareToZero(array[i][i]))
                return false;
        return true;
    }

    public boolean checkSCC(){
        boolean strictlyMore = false;

        for (int i = 0; i < rowAmount; i++) {
            double sum = Math.abs(array[i][i]) - sums[i];
            if (sum > 0) {          //нужна ли проверка на точность (?)
                if (sum >= 0)       // (?)
                    strictlyMore = true;
            }
            else return false;
        }
        return strictlyMore;
    }

    public double[] sumOfLines(){
        double[] temp = new double[rowAmount];
        for (int i = 0; i < rowAmount; i ++) {
            for (int j = 0; j < columnAmount - 1; j++)
                temp[i] += Math.abs(array[i][j]);
        }
        return temp;
    }

    public double[] solveByIterations(boolean needControl){
        double[] result = new double[rowAmount];
        boolean isOk = true;
        double x, delta = Double.MAX_VALUE;
        int counter = 0;
        do {
            x = result[0];
            double summary;

            for (int i = 0; i < rowAmount; i++){
                summary = 0;
                for (int j = 0; j < i; j ++)
                    summary += result[j] * array[i][j];

                for (int j = i + 1; j < rowAmount; j++)
                    summary += result[j] * array[i][j];

                result[i] = (array[i][columnAmount - 1] - summary) / array[i][i];
            }
            counter++;

            if (needControl){
                if (counter >= 6){
                    double temp = Math.abs(result[0] - x);
                    if (delta >= temp) delta = temp;
                    else isOk = false;
                }
                if (counter == 10) needControl = false;
            }
        } while (Math.abs(result[0] - x) >= epsilon && isOk);

        System.out.println(counter);
        if (!isOk) return null;
        return result;
    }
}