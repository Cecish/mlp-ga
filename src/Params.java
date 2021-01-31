import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: eisti
 * Date: 10/29/15
 * Time: 11:48 AM
 * Parameters for the MLP
 */
public class Params {

    public final static double MUTATION_PERTURBATION = 0.05;
    public final static double ACCEPTABLE_ERROR = 0.001; //One part of the stop condition. I assume a satisfying error is equal or under 0.001
    public final static Random random = new Random(System.currentTimeMillis()); //Setting the seed

    private int nb_input_neurons; //1 or 2 here
    private int nb_hidden_layers; //Number of hidden layers for the MLP
    private int nb_hidden_neurons_per_layer; //Number of hidden neurons per (hidden) layer for the MLP
    private int nb_ouput_neurons; //Number of output neurons
    private int nb_rows_dataset; //Number of rows in the dataset (most of the data sets have 100 rows but some have 101 rows !)
    private String filename; //Filename of the data set file
    public static double crossover_rate_weights; //Crossover rate when evolving the weights
    public static double mutation_rate_weights;  //Mutation rate when evolving the weights
    public static double crossover_rate_activation_functions; //Crossover rate when evolving the activation functions
    public static double mutation_rate_activation_functions; //Mutation rate when evolving the activation functions
    public static int pop_size; //Size of the population
    public static int nb_epochs; //Number of epochs

    /**
     * Setting parameters for running the MLP according to the user's choice
     * @param filename name of the data set file used for training the MLP
     * @param choice_function identifier of the function to approximate
     */
    public Params(String filename, int choice_function) {
        this.filename = filename;
        this.nb_hidden_layers=2; //Default values (that definitely should ve evolved)
        this.nb_hidden_neurons_per_layer = 4; //Default values (that definitely should ve evolved)
        this.nb_ouput_neurons = 1;

        if ((choice_function == 5) || (choice_function == 6) ) {
            this.nb_input_neurons=2;
        } else {
            this.nb_input_neurons=1;
        }

        if ((choice_function == 2) || (choice_function == 4)) {
            this.nb_rows_dataset = 101;
        } else {
            this.nb_rows_dataset = 100;
        }
    }

    /**
     * Getting the number of input neurons
     * @return the number of input neurons
     */
    public int getNb_input_neurons() {
        return nb_input_neurons;
    }

    /**
     * Getting the number of hidden layers
     * @return the number of hidden layers
     */
    public int getNb_hidden_layers() {
        return nb_hidden_layers;
    }

    /**
     * Getting the number of hidden neurons per layer
     * @return the number of hidden neurons per layer
     */
    public int getNb_hidden_neurons_per_layer() {
        return nb_hidden_neurons_per_layer;
    }

    /**
     * Getting the number of output neurons
     * @return the number of output neurons
     */
    public int getNb_ouput_neurons() {
        return nb_ouput_neurons;
    }

    /**
     * Getting the number of rows in the data set used to train the MLP
     * @return the number of rows of this data set
     */
    public int getNb_rows_dataset() {
        return nb_rows_dataset;
    }

    /**
     * Getting the name of the data set file to use for training the MLP
     * @return the name of such file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setting the crossover rate used to evolve the hidden weights
     * @param crossover_rate_weights crossover rate to use for evolving the hidden weights
     */
    public static void setCrossover_rate_weights(double crossover_rate_weights) {
        Params.crossover_rate_weights = crossover_rate_weights;
    }

    /**
     * Setting the mutation rate used to evolve the hidden weights
     * @param mutation_rate_weights mutation rate to use for evolving the hidden weights
     */
    public static void setMutation_rate_weights(double mutation_rate_weights) {
        Params.mutation_rate_weights = mutation_rate_weights;
    }

    /**
     * Setting the crossover rate used to evolve the activation function IDs
     * @param crossover_rate_activation_functions crossover rate to use for evolving the activation function IDs
     */
    public static void setCrossover_rate_activation_functions(double crossover_rate_activation_functions) {
        Params.crossover_rate_activation_functions = crossover_rate_activation_functions;
    }

    /**
     * Setting the mutation rate used to evolve the activation function IDs
     * @param mutation_rate_activation_functions mutation rate to use for evolving the activation function IDs
     */
    public static void setMutation_rate_activation_functions(double mutation_rate_activation_functions) {
        Params.mutation_rate_activation_functions = mutation_rate_activation_functions;
    }

    /**
     * Setting the size of the population
     * @param pop_size the size of the population
     */
    public void setPop_size(int pop_size) {
        this.pop_size = pop_size;
    }

    /**
     * Setting the number of epochs
     * @param nb_epochs the number of epochs
     */
    public void setNb_epochs(int nb_epochs) {
        this.nb_epochs = nb_epochs;
    }
}
