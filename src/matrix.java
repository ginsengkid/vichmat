import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class matrix {
    private double[][] a;
    private int rawAmount, columnAmount;
    double epsilon;


    private void create(int k, int l) {
        this.a = new double[k][];
        int i;
        for (i = 0; i < k; i++)
            this.a[i] = new double[l];
    }


    public void print() {
        int i, j;
        for (i = 0; i < rawAmount; i++) {
            for (j = 0; j < columnAmount; j++)
                System.out.printf("%15.6E", a[i][j]);
            System.out.println();
        }
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
                a[i][j] = Double.parseDouble(sn[j]);
        }
        scan.close();
    }


    public int  makeTriangle (){
        double multiplier;
        int k;
        int needed_line;
        //поиск и свап с ненулевой строкой
        for (int i = 1; i < rawAmount; i++) {

            needed_line = i;
            if (compareToZero(a[i][i])) {
                needed_line = findNotZeroElement(i);
                if(needed_line == -1)
                    return (3);
                swapLines(needed_line, i);
            }

            //"зануление столбца"
            k = i;
            for (int j = i; j < rawAmount; j ++) {
                multiplier = a[k][i-1]/a[i-1][i-1];
                multiplicationAndSubtractionOfLine(multiplier, i, j);
                k++;
            }
        }
        return(4);
    }


    private int findNotZeroElement(int i) {
        if (!compareToZero(a[i][i])) return i;
        for (int q = i + 1; q < rawAmount - i; q++)
            if (!compareToZero(a[q][i]))
                return q;
        return -1;
    }

    //сравнение числа с 0, с учетом точности
    private boolean compareToZero(double a){
        return ( Math.abs(a) < epsilon);
    }


    private void swapLines(int i, int q){
        double[] temp = a[q];
        a[q] = a[i];
        a[i] = temp;
    }

    // k - номер прохода, i - номер строки.
    private void multiplicationAndSubtractionOfLine(double multiplier, int k, int i){
        for (int j = 0; j < columnAmount; j++){
            a[i][j] -= (multiplier * a[k-1][j]);
            if (compareToZero(a[i][j]))
                a[i][j] = 0;
        }
    }


    public int checkSolutions() {
        if (compareToZero(a[rawAmount-1][columnAmount-2]))
            if (compareToZero(a[rawAmount-1][columnAmount-1]))
                return 1;    // 0 = 0
            else
                return 2;   // 0 = (!0)
        return 4;
    }

    public double[] findSolutions() {
        /*
        первый цикл идет вверх по матрице
        второй идет слева направо по строке, составляю сумму произведений
        корней на соответсвующие коэффициенты
         */
        double[] final_array = new double[rawAmount];
        for (int i = rawAmount - 1; i >= 0; i--){
            double summary = 0;
            for (int j = i + 1; j < rawAmount; j++){
                summary += a[i][j] * final_array[j];
            }
            final_array[i] += (a[i][rawAmount] - summary) / a[i][i];
        }
        return final_array; //имеет единсвенное решение
    }



}