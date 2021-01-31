import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: eisti
 * Date: 11/6/15
 * Time: 10:51 AM
 * Useful methods used to guide the user and fulfill his requirements (read training data sets, guide him with a menu,
 * provide output data from testing the MLP which can be used to plot approximations of desired functions)
 */
public class UsefulMethods {

    /**
     * Read a file's content an d put it in a matrix
     * @param input_filename Name of the file to read
     * @param height height of the data set (number of rows)
     * @param width of the data set (number of columns)
     * @return the matrix gathering the file's content
     */
    public Double[][] file_to_matrix(String input_filename, int height, int width) {
        Double[][] dataset = new Double[height][width];
        String[] lineArray;
        int index = 0;

        try{

            //Create object of FileReader
            FileReader inputFile = new FileReader(input_filename);

            //Instantiate the BufferedReader Class
            BufferedReader bufferReader = new BufferedReader(inputFile);

            //Variable to hold the one line data
            String line;

            // Read file line by line and print on the console
            while ((line = bufferReader.readLine()) != null)   {
                lineArray = line.split("\\,", -1);
                for (int i = 0; i < width; i++) {
                    dataset[index][i] = Double.parseDouble(lineArray[i]);
                }

                //System.out.println(line);
                index++;
            }
            //Close the buffer reader
            bufferReader.close();

        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
            System.exit(0);
        }

        return dataset;
    }

    public Double[][] file_to_matrix2(String input_filename, int height, int width) throws IOException {
        Double[][] matrix = new Double[height][width];
        String line;
        int row = 0;

        BufferedReader buffer = new BufferedReader(new FileReader(input_filename));

        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split("\\s+");

            for (int col = 0; col < width; col++) {

                matrix[row][col] = Double.parseDouble(vals[col]);
            }

            row++;
        }

        return matrix;
    }

    /**
     * Write the content of a matrix into an output file (used to see the fitness evolution through each generation)
     * @param matrix matrix to export in a file
     */
    public static void writeMatrixToFile(ArrayList<Double> matrix, String filename) throws FileNotFoundException, IOException {
        int i,j;

        PrintWriter out = new PrintWriter(filename);

        for (i =0; i< matrix.size(); i++) {
            out.printf(matrix.get(i)+"\n");
        }
        out.close();
    }

    /**
     * Write the content of a matrix into an output file separated with spaces
     * @param matrix matrix to export in a file
     * @param input inputs from the input file
     * @param filename_output name of the output file
     */
    public static void writeMatrixToFileSpaces(ArrayList<Double> matrix, ArrayList<Double[]> input, String filename_output) throws FileNotFoundException {
        int i;
        PrintWriter out = new PrintWriter(filename_output);

        for (i=0; i< matrix.size(); i++) {
            if (input.size() == 2) {
                out.printf(input.get(0)[i]+" "+input.get(1)[i]+" "+matrix.get(i)+"\n");
            } else {
                out.printf(input.get(0)[i]+" "+matrix.get(i)+"\n");
            }
        }
        out.close();
    }


    /**
     * Matrix printing (debugging purposes)
     * @param matrix matrix to print
     * @param prompt title
     * @param height number of rows
     * @param width number of columns
     */
    public void DoubleMatrixPrinting (Double[][] matrix, String prompt, int height, int width) {
        int i,j;
        int tailleI, tailleJ;

        tailleI = matrix.length;
        tailleJ = matrix[0].length;

        try {
            System.out.println("############ "+prompt+" ############");
            System.out.println();
            for (i=0 ; i<height ; i++) {
                for (j=0 ; j<width ; j++) {
                    if (j==0) {
                        System.out.print("|  "+matrix[i][j]+"  |  ");
                    } else {
                        System.out.print(matrix[i][j]+"  |  ");
                    }
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("taille i = "+ tailleI+ " et taille j = "+tailleJ);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Erreur lors de l'affichage : matrice vide ou dépassement : "+e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Get the th column of a matrix
     * @param index position of the column to get
     * @param matrix in which we want to get a certain column
     * @return the desired column
     */
    public Double[] getColumn(int index, Double[][] matrix) {
        int i;
        Double[] res_column = new Double[matrix.length];
        System.out.println("matrix legnth = "+matrix.length);

        for (i = 0; i < matrix.length; i++) {
            res_column[i] = matrix[i][index];
        }

        return res_column;
    }

    /**
     * Menu displayed to the user at start
     * @return the choice of the user
     */
    public int menuProgram() throws NumberFormatException, IOException, IntegerOutOfRange {
        int choice_user = 0;

        System.out.println("*********************************************");
        System.out.println("Please enter the number corresponding to the parameters you wish to be evolved : ");
        System.out.println("1. The weights of the artificial neural network only\n" +
                           "2. The activation function only\n" +
                           "3. Both the weights and the activation function (it will also evolve part of the topology - the number of hidden neurons per hidden layer)");

        try {
            choice_user = readInteger();

            if ((choice_user < 1) || (choice_user > 3)) {
                throw new IntegerOutOfRange();
            }

        } catch (IntegerOutOfRange e1) {
            System.err.println("An integer (1, 2, or 3) was expected.\n" +
                    "Program stopped");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("An integer (1, 2, or 3) was expected.\n" +
                    "Program stopped");
            System.exit(0);
        }

        return choice_user;
    }

    /**
     * Menu to choose which function to approximate
     * @return the user's choice
     */
    public int menuFunctionToTrain() throws NumberFormatException, IOException, IntegerOutOfRange {
        int choice_user = 0;

        System.out.println("*********************************************");
        System.out.println("Please enter the number corresponding to the function you wish the Multilayer Perceptron to train : ");
        System.out.println("1. Linear function\n" +
                "2. Cubic function\n" +
                "3. Sine function\n" +
                "4. Tanh function\n" +
                "5. XOR function\n" +
                "6. A complex function");

        try {
            choice_user = readInteger();

            if ((choice_user < 1) || (choice_user > 6)) {
                throw new IntegerOutOfRange();
            }

        } catch (IntegerOutOfRange e1) {
            System.err.println("An integer (between 1 and 6) was expected.\n" +
                    "Program stopped");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("An integer (between 1 and 6) was expected.\n" +
                    "Program stopped");
            System.exit(0);
        }

        return choice_user;
    }

    /**
     * Getting the input file corresponding to the training data set chose be the user
     * @param choice choice of the user
     * @return the name of the corresponding data set file
     */
    public String getFilename(int choice) {
        String res = null;

        switch (choice) {
            case 1 :
                res = "1in_linear_comas.txt";
                break;
            case 2 :
                res = "1in_cubic_comas.txt";
                break;
            case 3 :
                res = "1in_sine_comas.txt";
                break;
            case 4 :
                res = "1in_tanh_comas.txt";
                break;
            case 5 :
                res = "2in_xor_comas.txt";
                break;
            case 6 :
                res = "2in_complex_comas.txt";
                break;
        }
        return res;
    }

    /**
     * The user gives a double
     * @param id to distinguish how the user wants to train the MLP
     * @param id_evolution to distinguish which parameters must be asked to the user
     * @return the double written by the user
     */
    public double chooseDouble(int id, int id_evolution) {
        double choice = 0;

        System.out.println("*********************************************");
        switch (id) {
            case 0 :
                if (id_evolution == 1) {
                    System.out.println("Please indicate the crossover rate to use ");

                } else if (id_evolution == 2) {
                    System.out.println("Please indicate the crossover rate to use for the WEIGHTS evolution");

                } else if (id_evolution == 3) {
                    System.out.println("Please indicate the crossover rate to use for the ACTIVATION FUNCTIONS evolution");
                }
                break;

            case 1 :
                if (id_evolution == 1) {
                    System.out.println("Please indicate the mutation rate to use ");

                } else if (id_evolution == 2) {
                    System.out.println("Please indicate the mutation rate to use for the WEIGHTS evolution");

                } else if (id_evolution == 3) {
                    System.out.println("Please indicate the mutation rate to use for the ACTIVATION FUNCTIONS evolution");
                }
                break;
        }

        try {
            choice = readDouble();

        } catch (Exception e) {
            System.err.println("A double was expected.\n" +
                    "Program stopped");
            System.exit(0);
        }

        return choice;
    }

    /**
     * The user choose an integer
     * @param id to distinguish which parameter to ask to the user
     * @return the user's answer
     */
    public int chooseInt(int id) {
        int choice = 0;

        System.out.println("*********************************************");
        switch (id) {
            case 0 :
                System.out.println("Please indicate the population size ");
                break;

            case 1 :
                System.out.println("Please indicate the number of epochs ");
                break;
        }

        try {
            choice = readInteger();

            if (choice < 0) {
                throw new IntegerOutOfRange();
            }

        } catch (Exception e) {
            System.err.println("positive integer was expected.\n" +
                    "Program stopped");
            System.exit(0);
        }

        return choice;
    }

    /**
     * Reading an integer
     * @return the integer read
     */
    public int readInteger() throws NumberFormatException, IOException {
        int choice_user;

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        choice_user = Integer.parseInt(input.readLine());

        return choice_user;
    }

    /**
     * Reading a double
     * @return the double read
     */
    public double readDouble() throws NumberFormatException, IOException {
        double choice_user;

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        choice_user = Double.parseDouble(input.readLine());

        return choice_user;
    }
}
