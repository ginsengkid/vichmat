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

        int result;
        double[] array;
        /*
        if (mat.checkForZeros()){
            if (mat.checkSCC()) {
                array = mat.solveByIterations();
                printArray(array);
            }
            else {
                array = mat.solveByIterationsWithControl();
                if (array != null) printArray(array);
            }
        }
        else{
            result = mat.checkAnswer();
            getResult(result);
            mat.print();
            if (result == 5){
                array = mat.solveByIterations();
                printArray(array);
            }
            if (result == 6){
                array = mat.solveByIterationsWithControl();
                if (array == null) return;
                else printArray(array);
            }
            if (result == 7){
                result = mat.makeTriangle();
                mat.print();
                getResult(result);
                if(result == 4) {
                    result = mat.checkSolutions();
                    if(result != 4)
                        getResult(result);
                    else {
                        array= mat.findSolutions();
                        printArray(array);
                    }
                }
            }
        }*/
        result = mat.makeTriangle();
        mat.print();
        getResult(result);
        if(result == 4) {
            result = mat.checkSolutions();
            if (result != 4)
                getResult(result);
            else {
                array = mat.findSolutions();
                printArray(array);
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
            case 5 -> System.out.println("Система больше не имеет 0 на диагонали и соблюдается ДУС \n");
            case 6 -> System.out.println("Система больше не имеет 0 на диагонали, но не соблюдается ДУС \n");
            case 7 -> System.out.println("Систему нельзя решить итерационным методом \n");
        }
    }

    public static void printArray(double [] matrix) {
        System.out.println("Результат: ");
        for (double v : matrix) System.out.printf("%15.6E", v);
        System.out.println();
    }

}