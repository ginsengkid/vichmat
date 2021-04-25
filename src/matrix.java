import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class matrix {
    private double[][] array;
    private int rawAmount, columnAmount;
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
        for (i = 0; i < rawAmount; i++) {
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
        rawAmount = Integer.parseInt(sn[0]);
        columnAmount = Integer.parseInt(sn[1]) + 1;
        epsilon = Math.pow(10, -Double.parseDouble(sn[2]) - 1);
        this.create(rawAmount, columnAmount);
        int i, j;
        for (i = 0; i < rawAmount; i++) {
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
        for (int i = 1; i < rawAmount; i++) {

            needed_line = i;
            if (compareToZero(array[i][i])) {
                needed_line = findNotZeroElement(i);
                if(needed_line == -1)
                    return (3);
                swapLines(needed_line, i);
            }

            //making column to zero
            k = i;
            for (int j = i; j < rawAmount; j ++) {
                multiplier = array[k][i-1]/ array[i-1][i-1];
                multiplicationAndSubtractionOfLine(multiplier, i, j);
                k++;
            }
        }
        return(4);
    }


    private int findNotZeroElement(int i) {
        if (!compareToZero(array[i][i])) return i;
        for (int q = i + 1; q < rawAmount - i; q++)
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
        if (compareToZero(array[rawAmount-1][columnAmount-2]))
            if (compareToZero(array[rawAmount-1][columnAmount-1]))
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
        double[] final_array = new double[rawAmount];
        for (int i = rawAmount - 1; i >= 0; i--){
            double summary = 0;
            for (int j = i + 1; j < rawAmount; j++){
                summary += array[i][j] * final_array[j];
            }
            final_array[i] += (array[i][rawAmount] - summary) / array[i][i];
        }
        return final_array;
    }


    //iterative method//

    public boolean checkDiagonalForZero(){
        int counter = 0;
        for (int i = 0; i < rawAmount; i++) {
            if (array[i][i] == 0)
                counter++;
        }
        return counter == rawAmount;
    }

    /*
    SCC - sufficient condition for convergence
    (predominance of the diagonal elements)
    */
    public boolean checkSCC(){
        boolean strictlyMore = false;

        for (int i = 0; i < rawAmount; i++) {
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
        double[] temp = new double[rawAmount];
        for (int i = 0; i < rawAmount; i ++) {
            for (int j = 0; j < columnAmount - 1; j++)
                temp[i] += Math.abs(array[i][j]);
        }
        return temp;
    }

    public double[] solveByIterations(){
        double[] result = new double[rawAmount];
        double x;
        int counter = 0;
        //do{
            counter++;
            x = result[0];
            double summary = 0;
            for (int i = 0; i < rawAmount; i++){
                for (int j = 0; j < columnAmount - 1; j ++)
                    summary += result[j] * array[i][j];
                result[i] = (- summary + array[i][columnAmount - 1])/array[i][i];
            }
        //} while (Math.abs(result[0] - x) >= epsilon);
        System.out.println(counter);
        return result;
    }



}