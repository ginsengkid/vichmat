import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class matrix {
    private double[][] a;
    private int n, m, accuracy;


    private void create(int k, int l) {
        this.a = new double[k][];
        int i;
        for (i = 0; i < k; i++)
            this.a[i] = new double[l];
    }


    public void print() {
        int i, j;
        for (i = 0; i < a.length; i++) {
            for (j = 0; j < a[0].length; j++)
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
        n = Integer.parseInt(sn[0]);
        m = Integer.parseInt(sn[1]) + 1;
        accuracy = Integer.parseInt(sn[2]);
        this.create(n, m);
        int i, j;
        for (i = 0; i < n; i++) {
            str = scan.nextLine();
            sn = pat.split(str);
            for (j = 0; j < m; j++)
                a[i][j] = Double.parseDouble(sn[j]);
        }
        scan.close();
    }


    private void rotate_square_matrix(){
        double temporary;
        if (a.length == a[0].length){
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < a[0].length; j++) {
                    temporary = a[i][j];
                    a[i][j] = a[j][i];
                    a[j][i] = temporary;
                }
        }
    }


    private void rotate_matrix_from_to (double[][] matrix, double[][] temp){
        for (int i = 0; i < temp.length; i++)
            for (int j = 0; j < temp[0].length; j++)
                temp[i][j] = matrix[j][i];
    }


    private double[] find_sums(double [][] mat) {
        double temp = 0;
        double[] array = new double[mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++)
                temp += mat[i][j];
            array[i] = temp;
            temp = 0;
        }
        return array;
    }


    public void sort() {
        if (a.length == a[0].length) {
            rotate_square_matrix();
            sort_by(a);
            rotate_square_matrix();
        }
        else {
            double[][] mat = new double[a[0].length][a.length];
            rotate_matrix_from_to(a, mat);
            sort_by(mat);
            rotate_matrix_from_to(mat, a);
        }
    }


    private void sort_by(double[][] mat) {
        double temp;
        double[] array = find_sums(mat),temp_array;
        int i, j;
        for (i = 1; i < array.length; i++) {
            j = i;
            temp = array[j];
            temp_array = mat[j];
            for (;j > 0 && temp > array[j-1];j--) {
                array[j] = array[j-1];
                mat[j] = mat[j-1];
            }
            array[j] = temp;
            mat[j] = temp_array;
        }

    }


    public answer  solve_matrix_by_gauss (){
        double multiplier;
        int k;
        int needed_line;
        //поиск и свап с ненулевой строкой
        for (int i = 1; i < a.length; i++) {
            needed_line = find_not_zero_element(i);
            if (needed_line != i)
                swap_lines(needed_line, i);

            //"зануление столбца"
            if (needed_line != -1) {
                k = i;
                for (int j = i; j < a.length; j ++) {
                    multiplier = a[k][i-1]/a[i-1][i-1];
                    multiple_and_subtraction_of_line(multiplier, i, j);
                    k++;
                }
            }
            else return (new answer(null, 3)); //вырожденная система
        }
        return(find_solutions());
    }


    private int find_not_zero_element(int i) {
        if (!compare_to_zero(a[i][i])) return i;
        for (int q = i + 1; q < a.length - i; q++)
            if (!compare_to_zero(a[q][i]))
                return q;
        return -1;
    }

    //сравнение числа с 0, с учетом точности
    private boolean compare_to_zero(double a){
        return ( Math.abs(a) < Math.pow(10, -accuracy-1));
    }


    private void swap_lines (int i, int q){
        double[] temp = a[q];
        a[q] = a[i];
        a[i] = temp;
    }

    // k - номер прохода, i - номер строки.
    private void multiple_and_subtraction_of_line(double multiplier, int k, int i){
        for (int j = 0; j < a[0].length; j++){
            a[i][j] = a[i][j] - (multiplier * a[k-1][j]);
        }
    }


    private answer find_solutions() {
        if (a[a.length-1][a[0].length-2] == 0)
            if (a[a.length-1][a[0].length-1] == 0)  // 0 = 0
                return new answer(null,1);
            else
                return new answer(null, 2);   // 0 = (!0)

        /*
        первый цикл идет вверх по матрице
        второй идет слева направо по строке, составляю сумму произведений
        корней на соответсвующие коэффициенты
         */
        double[] final_array = new double[a.length];
        for (int i = a.length - 1; i >= 0; i--){
            double summary = 0;
            for (int j = i + 1; j < a.length; j++){
                summary += a[i][j] * final_array[j];
            }
            final_array[i] += (a[i][a.length] - summary) / a[i][i];
        }
        return new answer(final_array, 0); //имеет единсвенное решение
    }

    public static class answer{
        private final double[] array;
        private final int result;
        public answer(double[] a, int b){
            array = a;
            result = b;
        }

        public int getResult() {
            return result;
        }

        public double[] getArray() {
            return array;
        }
    }
}