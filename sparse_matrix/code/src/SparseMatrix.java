import java.io.*;
import java.util.Scanner;

class SparseMatrix {
    private int rows;
    private int cols;
    private Element[] elements;
    private int elementCount;

    // Inner class to represent non-zero entries
    private static class Element {
        int row, col, value;

        Element(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }

    public SparseMatrix(int rows, int cols, int maxElements) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new Element[maxElements];
        this.elementCount = 0;
    }

    public void addElement(int row, int col, int value) {
        if (value != 0) {
            elements[elementCount++] = new Element(row, col, value);
        }
    }

    public static SparseMatrix loadFromFile(String filePath) throws IOException, IllegalArgumentException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        if (!line.startsWith("rows=")) throw new IllegalArgumentException("Input file has wrong format");

        int rows = Integer.parseInt(line.substring(5));
        line = reader.readLine();
        if (!line.startsWith("cols=")) throw new IllegalArgumentException("Input file has wrong format");

        int cols = Integer.parseInt(line.substring(5));
        SparseMatrix matrix = new SparseMatrix(rows, cols, 1000000);

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (!line.startsWith("(") || !line.endsWith(")")) throw new IllegalArgumentException("Input file has wrong format");
            line = line.substring(1, line.length() - 1);

            String[] parts = line.split(",");
            if (parts.length != 3) throw new IllegalArgumentException("Input file has wrong format");

            try {
                int row = Integer.parseInt(parts[0].trim());
                int col = Integer.parseInt(parts[1].trim());
                int value = Integer.parseInt(parts[2].trim());
                matrix.addElement(row, col, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Input file has wrong format");
            }
        }
        reader.close();
        return matrix;
    }

    public int getElement(int row, int col) {
        for (int i = 0; i < elementCount; i++) {
            if (elements[i].row == row && elements[i].col == col) {
                return elements[i].value;
            }
        }
        return 0;
    }

    public void setElement(int row, int col, int value) {
        for (int i = 0; i < elementCount; i++) {
            if (elements[i].row == row && elements[i].col == col) {
                if (value == 0) {
                    elements[i] = elements[--elementCount]; // Remove element
                } else {
                    elements[i].value = value; // Update element
                }
                return;
            }
        }
        if (value != 0) {
            addElement(row, col, value); // Add new element
        }
    }

    public SparseMatrix add(SparseMatrix other) {
        SparseMatrix result = new SparseMatrix(rows, cols, elementCount + other.elementCount);
        for (int i = 0; i < elementCount; i++) {
            result.addElement(elements[i].row, elements[i].col, elements[i].value);
        }
        for (int i = 0; i < other.elementCount; i++) {
            int row = other.elements[i].row;
            int col = other.elements[i].col;
            int value = other.elements[i].value + result.getElement(row, col);
            result.setElement(row, col, value);
        }
        return result;
    }

    public SparseMatrix subtract(SparseMatrix other) {
        SparseMatrix result = new SparseMatrix(rows, cols, elementCount + other.elementCount);
        for (int i = 0; i < elementCount; i++) {
            result.addElement(elements[i].row, elements[i].col, elements[i].value);
        }
        for (int i = 0; i < other.elementCount; i++) {
            int row = other.elements[i].row;
            int col = other.elements[i].col;
            int value = result.getElement(row, col) - other.elements[i].value;
            result.setElement(row, col, value);
        }
        return result;
    }

    public SparseMatrix multiply(SparseMatrix other) {
        if (this.cols != other.rows) throw new IllegalArgumentException("Matrix lengths do not match for multiplication");

        SparseMatrix result = new SparseMatrix(this.rows, other.cols, this.elementCount * other.elementCount);
        for (int i = 0; i < this.elementCount; i++) {
            for (int j = 0; j < other.elementCount; j++) {
                if (this.elements[i].col == other.elements[j].row) {
                    int row = this.elements[i].row;
                    int col = other.elements[j].col;
                    int value = this.elements[i].value * other.elements[j].value + result.getElement(row, col);
                    result.setElement(row, col, value);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            SparseMatrix matrix1 = SparseMatrix.loadFromFile("C:\\Users\\deand\\dsa\\sample_inputs\\easy_sample_03_3.txt");
            SparseMatrix matrix2 = SparseMatrix.loadFromFile("C:\\Users\\deand\\dsa\\sample_inputs\\easy_sample_03_3.txt");

            System.out.println("\nChoose an operation:");
            System.out.println("1. Addition (+)");
            System.out.println("2. Subtraction (-)");
            System.out.println("3. Multiplication (*)");

            int operation = scanner.nextInt();
            SparseMatrix result = null;
            switch (operation) {
                case 1:
                    result  = matrix1.add(matrix2);
                    System.out.println("\nAddition Done:");
                    break;
                case 2:
                    result  = matrix1.subtract(matrix2);
                    System.out.println("\nSubtraction Done:");
                    break;
                case 3:
                    result  = matrix1.multiply(matrix2);
                    System.out.println("\nMultiplication Done:");
                    break;
                default:
                    System.out.println("Invalid operation choice.");
            }
            File outputDir = new File("./output");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            // Create the output.txt file in the output directory
            File outputFile = new File(outputDir, "output.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            for (int i = 0; i < result.elementCount; i++) {
                Element element = result.elements[i];
                writer.write("(" + element.row + ", " + element.col + ", " + element.value + ")\n");
            }
            writer.close();
            System.out.println("The result has been written to output/output.txt");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}


