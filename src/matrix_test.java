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
        System.out.println("123132");
        int result = mat.makeTriangle();
        mat.print();
        System.out.println();
        getResult(result);
        if(result == 4) {
            result = mat.checkSolutions();
            if(result != 4)
                getResult(result);
            else {
                double[] answer = mat.findSolutions();
                printMatrix(answer);
            }
        }

    }

    public static void getResult(int result) {
        switch (result) {
            case 0 -> System.out.println("Система имеет единственное решение \n");
            case 1 -> System.out.println("Решений бесконечно много \n");
            case 2 -> System.out.println("Решений нет \n");
            case 3 -> System.out.println("Система вырожденная \n");
            case 4 -> System.out.println("Систему можно решать дальше \n");
        }
    }

    public static void printMatrix(double [] matrix) {
        for (double v : matrix) System.out.printf("%15.6E", v);
        System.out.println();
    }
}