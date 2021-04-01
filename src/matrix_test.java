import java.io.FileNotFoundException;

public class matrix_test {

    public static void main(String[] args) {
        matrix mat = new matrix();
        try {
            mat.init("D:\\file.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
        }

        mat.print();
        matrix.answer answer = mat.solve_matrix_by_gauss();
        double[] result = answer.getArray();
        int what_happened = answer.getResult();

        /*
        "0" - система имеет единственное решение
        "1" - решений бесконечно много
        "2" - решений нет
        "3" - система вырожденная
         */

        System.out.println();
        mat.print();
        System.out.println();
        for (double v : result) System.out.print(v + " ");
        System.out.println();

    }

}