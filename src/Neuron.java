import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Cécile RIQUART
 * Date: 10/25/15
 * Time: 4:53 PM
 * Architecture of a neuron found in Artificial Neural Networks
 */
public class Neuron {

    //Attributes ====================================================
    private double output_signal; //Output signal value
    private int activation_function_id; //Id of the activation function used for one neuron
    private List<Double> input_weights; //Input weights for one neuron

    //Constructor ===================================================
    /**
     * In order to build neuron, one must first specify its number of inputs.
     * Each input signal has a weight. This constructor randomly set input weights
     * between MIN_WEIGHT and MAX_WEIGHT.
     * @param nb_input_weights number of inputs for a neuron
     */
    public Neuron(int nb_input_weights) {
        int i;
        double random_weight;
        Double temp;

        this.input_weights = new ArrayList<>();

        //The bias is the nb_input_weights + 1 value
        /*for (i=0; i< nb_input_weights+1; i++) {

            random_weight = MultilayerPerceptron.MIN_WEIGHT + (MultilayerPerceptron.MAX_WEIGHT - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble();
            this.input_weights.add(i,random_weight);
        } */

        //Léon Bottou weights initialisation
        double a = 2.38;
        double min_range, max_range;
        for (i=0; i< nb_input_weights+1; i++) {
            min_range = (-a)/(Math.sqrt(this.getNbInputWeights(true)));
            max_range = (a)/(Math.sqrt(this.getNbInputWeights(true)));

            random_weight = min_range + (max_range - min_range) * Params.random.nextDouble();
            this.input_weights.add(i,random_weight);
        }

        //Initialising the activation function ID (integer between 0 and 4)
        temp = (4.9) * Params.random.nextDouble();
        this.activation_function_id = temp.intValue();
    }

    //Accessors + Methods ============================================
    /**
     * Getter for accessing the activation function assigned to a neuron
     * @return  the activation function's ID of one neuron
     */
    public int getActivation_function_id() {
        return activation_function_id;
    }

    /**
     * Setter for changing the activation function's ID of one neuron
     * @param activation_function_id new activation function's ID
     */
    public void setActivation_function_id(int activation_function_id) {
        this.activation_function_id = activation_function_id;
    }

    /**
     * Getter for accessing the output signal of a neuron
     * @return  the current value of the neuron's output signal
     */
    public double getOutput_signal() {
        return output_signal;
    }

    /**
     * Setter for changing the output signal of a neuron
     * @param output_signal new value of the output signal
     */
    public void setOutput_signal(double output_signal) {
        this.output_signal = output_signal;
    }

    /**
     * Getter for accessing the nth input weight of a neuron
     * @param pos position/index of the desired input weight
     * @return the nth input weight
     */
    public double getInput_nth_weight(int pos) {
        double res_weight = 0;

        try {
            res_weight = input_weights.get(pos);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return res_weight;
    }

    /**
     * Setter for changing the nth input weight of a neuron
     * @param pos position/index of the input weight to be modified
     * @param weight new value of such weight
     */
    public void setInput_nth_weight(int pos, double weight) {

        try {
            input_weights.set(pos, weight);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Obtaining a neuron's input weights length (the latter can include the bias)
     * @param bias true if the bias should be included to the result, false otherwise
     * @return a neuron's number of input weights
     */
    public int getNbInputWeights(boolean bias) {
        int res;

        if (bias) {
            res = input_weights.size();

        } else {
            res = input_weights.size()-1;
        }
        return res;
    }

    /**
     * Printing a Neuron in the console (especially for debugging purposes)
     */
    public void printNeuron() {
        System.out.println("Output signal = "+output_signal);

        for (int i=0; i< input_weights.size(); i++) {
            System.out.println("weight["+i+"] = "+input_weights.get(i));
        }
    }

}
