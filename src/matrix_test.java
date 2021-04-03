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
        System.out.println();

        int result = mat.makeTriangle();
        mat.print();
        System.out.println();
        getResult(result);
        if(result == 4) {
            result = mat.checkSoultions();
            if(result != 4)
                getResult(result);
            else {
                double[] answer = mat.findSolutions();
                printMatrix(answer);
            }
        }

    }

    public static void getResult(int result) {
        switch (result){
            case 0:
                System.out.println("Система имеет единственное решение \n");
                break;
            case 1:
                System.out.println("Решений бесконечно много \n");
                break;
            case 2:
                System.out.println("Решений нет \n");
                break;
            case 3:
                System.out.println("Система вырожденная \n");
                break;
            case 4:
                System.out.println("Систему можно решать дальше \n");
                break;

        }
    }

    public static void printMatrix(double [] matrix) {
        for (int i = 0; i < matrix.length; i++)
            System.out.printf("%15.6E", matrix[i]);
        System.out.println();
    }
}