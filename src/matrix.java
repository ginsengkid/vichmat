import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;
public class matrix {
    private double[][] array;
    private int rowAmount, columnAmount;
    private double epsilon;
    private double [] sums;

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
    }

    public int  makeTriangle (){
        double multiplier;
        int k;
        int needed_line;
        //finding and swapping not zero line
        if (compareToZero(array[0][0])){
            needed_line = findNotZeroElement(0);
            if (needed_line == -1) return 3;
            swapLines(needed_line, 0);
        }
        for (int i = 1; i < rowAmount; i++) {
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

}