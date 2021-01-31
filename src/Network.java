import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eisti
 * Date: 10/25/15
 * Time: 6:45 PM
 * Architecture of an artificial neural network
 */
public class Network {

    //Attributes ====================================================
    private List<Layer> layers_array; //Array of all the hidden layers of the network + the output layer
    private int nb_input_neurons;  //Number of input neurons
    private int nb_output_neurons; //Number of output neurons
    private int nb_hidden_layers;  //Number of hidden layers
    private int nb_neurons_hidden_layer; //number of neurons for each hidden layer
    private List<Integer> activation_functions_id; //Array of activation functions' id. Each value designate the activation function of one specific layer
    private double output_Signal;  //Final output of the network

    //Constructor ===================================================
    /**
     * Building an artificial neural network
     * @param nb_input_neurons number of inputs (here one or two)
     * @param nb_output_neurons number of outputs (here one or two)
     * @param nb_hidden_layers number of hidden neural layers
     * @param nb_neurons_hidden_layer number of neurons per hidden layer
     * @param training_type
     */
    public Network(int nb_input_neurons, int nb_output_neurons, int nb_hidden_layers, int nb_neurons_hidden_layer, int training_type) {
        int i;

        this.nb_input_neurons = nb_input_neurons;
        this.nb_output_neurons = nb_output_neurons;
        this.nb_hidden_layers = nb_hidden_layers;
        this.nb_neurons_hidden_layer = nb_neurons_hidden_layer;
        this.layers_array = new ArrayList<>();
        this.activation_functions_id = new ArrayList<>();

        //Random activation functions are assigned to each layer (they may be improved/changed through training)
        if (training_type != 1) {
            //Initialising the array of activation functions' id
            initActivationFunctionID();

        //The sigmoid function is the activation function used for each layer.
        //Activation functions won't be improved through training, only the weights will IN THIS CASE
        } else {
            for (i=0; i< nb_hidden_layers +1; i++) {
                activation_functions_id.add(1); //ID = 1 -> Sigmoid function
            }
        }

        //Actually creating the neural network
        initNetwork();
    }

    //Accessors + Methods ============================================
    /**
     * Getting the nth layer of a network
     * @param pos index/position of the layer to be accessed
     * @return the nth layer of the network
     */
    public Layer get_nth_layer(int pos) {
        Layer res_layer = null;

        try {
            res_layer = layers_array.get(pos);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return res_layer;
    }

    /**
     * Getting the number of layers in the network
     * @return the number layers in the network
     */
    public int getNbLayer() {
        return layers_array.size();
    }

    /**
     * Getting the number of weights of a network
     * @param bias if true, biases are included in the result
     * @return the number of weights of a network
     */
    public int getTotalWeightsNb(boolean bias) {
        int i, j, res = 0;

        //For each layer
        for (i=0; i< layers_array.size(); i++) {
            //For each neuron
            for (j=0; j< layers_array.get(i).getSizeLayer(); j++) {
                res += layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(bias);
            }
        }
        return res;
    }

    /**
     * Getting the the output signal of a network
     * @return the final output signal of a network
     */
    public double getOutput_Signal() {
        return output_Signal;
    }

    /**
     * Initialising the array of action functions' id. The size of this array corresponds to the number of hidden layers
     * Furthermore IDs are integer varying from 0 to 4 (cf. coursework subject : 5 activation functions are considered)
     */
    public void initActivationFunctionID() {
        int i;
        Double random_id;

        for (i=0; i< nb_hidden_layers; i++) {
            random_id = (4.9 * Params.random.nextDouble());
            activation_functions_id.add(random_id.intValue());
        }
    }

    /**
     * Building the network's architecture + initialising it (feeding it with layers,neurons)
     */
    public void initNetwork() {
        int i;
        Layer layer;

        //Creation of hidden layers
        //CASE 1 : there are no hidden layers ...
        if (nb_hidden_layers == 0) {
            // ... just one layer composed of the output neuron(s)
            layer = new Layer(nb_output_neurons, nb_input_neurons);
            layers_array.add(0, layer);

        //There is at least one hidden layer
        } else {
            for (i=0; i< nb_hidden_layers; i++) {
                //Special treatment for the first hidden layer: input weights are from the input layer
                if (i==0) {
                    layer = new Layer(nb_neurons_hidden_layer,nb_input_neurons);

                } else {
                    layer = new Layer(nb_neurons_hidden_layer, nb_neurons_hidden_layer);
                }
                layers_array.add(i, layer);
            }

            //Add the output layer at the end
            layer = new Layer(nb_output_neurons, nb_neurons_hidden_layer);
            layers_array.add(layer);
        }
    }

    /**
     * Updating network's weights with the ones from a chromosome (reference) in parameter
     * @param chromosome containing the new structure of the network
     */
    public void updateHiddenWeights(Chromosome chromosome) {
        int i,j, k, l=0;

        //For each layer
        for (i=0; i< layers_array.size(); i++) {
            //For each neuron
            for (j=0; j<layers_array.get(i).getSizeLayer(); j++) {
                //For each weight
                for (k=0; k<layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(true); k++) {      //BIAS CONSIDERED
                    layers_array.get(i).get_nth_Neurons(j).setInput_nth_weight(k, chromosome.get_nth_weight(l++));
                }
            }
        }
    }

    /**
     * Updating network's activation functions with the ones from a chromosome (reference) in parameter
     * @param chromosome containing the new structure of the network
     */
    public void updateActivationFunctionID(Chromosome chromosome) {
        int i, j, k = 0;

        //For each layer
        for (i=0; i< layers_array.size(); i++) {
            //For each neuron
            for (j=0; j< layers_array.get(i).getSizeLayer(); j++) {
                layers_array.get(i).get_nth_Neurons(j).setActivation_function_id(chromosome.get_nth_ID(k));
                k++;
            }
        }
    }

    /**
     * Feed forward propagation. The input(s) propagate(s) through the network, updating each neuron's output value
     * @param input Input(s) of the network
     */
    public void NetworkPropagation(ArrayList<Double> input) {
        int i, j, k;
        double signal;
        double bias;
        double temp = 0;

        //For each layer
        for (i = 0; i< layers_array.size(); i++) {

            //Special treatment for the first layer : input signals are those of the input(s) of the network
            if (i==0) {
                //For each neuron
                for (j=0; j< layers_array.get(i).getSizeLayer(); j++) {

                    signal = 0.0; //Reset

                    //For each weight (excluding biases because we are dealing with the input layer)
                    for (k=0; k< layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(false); k++) {
                        //System.out.println("-----------------------------------------");
                        for (int z = 0; z < input.size(); z++) {

                            signal += layers_array.get(i).get_nth_Neurons(j).getInput_nth_weight(k) * input.get(z);

                            //System.out.println(input.get(k)[z]);

                        }
                        //System.out.println("-----------------------------------------");
                    }
                    bias = layers_array.get(i).get_nth_Neurons(j).getInput_nth_weight(layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(true)-1);
                    //layers_array.get(i).get_nth_Neurons(j).setOutput_signal(Objective.activationFunction(layers_array.get(i).get_nth_Neurons(j).getActivation_function_id(), signal+bias));
                    layers_array.get(i).get_nth_Neurons(j).setOutput_signal(signal-bias);
                }

            //For all hidden layers
            } else if (i>0 && i<layers_array.size()-1) {
                //For each neuron
                for (j=0; j< layers_array.get(i).getSizeLayer(); j++) {

                    signal = 0.0; //Reset

                    //For each weight
                    for (k=0; k< layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(false); k++) {
                        signal += layers_array.get(i).get_nth_Neurons(j).getInput_nth_weight(k) * layers_array.get(i-1).get_nth_Neurons(k).getOutput_signal();
                    }

                    bias = layers_array.get(i).get_nth_Neurons(j).getInput_nth_weight(layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(true)-1);
                    //layers_array.get(i).get_nth_Neurons(j).setOutput_signal(Objective.activationFunction(layers_array.get(i).get_nth_Neurons(j).getActivation_function_id(), signal+bias));
                    layers_array.get(i).get_nth_Neurons(j).setOutput_signal(Objective.activationFunction(layers_array.get(i).get_nth_Neurons(j).getActivation_function_id(), signal) -bias);

                    //System.out.println("output value["+i+"]["+j+"] = "+layers_array.get(i).get_nth_Neurons(j).getOutput_signal());
                }

            //Special treatment for the output layer
            } else if (i == layers_array.size()-1) {
                //There's only one neuron in the output layer
                signal = 0.0; //Reset

                //For each weight
                for (k=0; k< layers_array.get(i).get_nth_Neurons(0).getNbInputWeights(false); k++) {
                    signal += layers_array.get(i).get_nth_Neurons(0).getInput_nth_weight(k) * layers_array.get(i-1).get_nth_Neurons(k).getOutput_signal();
                    //temp = (Objective.activationFunction(layers_array.get(i).get_nth_Neurons(0).getActivation_function_id(), signal));
                }

                //No bias and no activation function is used in the output layer !

                //System.out.println("output value["+i+"]["+j+"] = "+layers_array.get(i).get_nth_Neurons(j).getOutput_signal());

                //Let's update the final output of the network
                //output_Signal = temp;
                output_Signal = signal;
            }
        }
    }

    /**
     * Updating process of the network, directly followed by a feed forward propagation
     * @param input inputs of the network (one or two here)
     * @param chromosome reference used to update the network (cf updateHiddenWeights method)
     */
    public void feedForwardPropagation(ArrayList<Double> input, Chromosome chromosome, int training_type) {

        switch (training_type) {
            case 1 :
                //Updating network's hidden weights with chromosome
                updateHiddenWeights(chromosome);
                updateActivationFunctionID(chromosome);
                break;
            case 2 :
                //Updating network's activation function's IDs
                updateHiddenWeights(chromosome);
                updateActivationFunctionID(chromosome);
                break;
            case 3 :
                //Updating network's hidden weights with chromosome
                updateHiddenWeights(chromosome);
                //Updating network's activation function's IDs
                updateActivationFunctionID(chromosome);
                break;
            case 4 :
                //Updating network's hidden weights with chromosome
                updateHiddenWeights(chromosome);
                //Updating network's activation function's IDs
                updateActivationFunctionID(chromosome);
                break;
        }

        //Propagation
        NetworkPropagation(input);
    }

    /**
     * The error is defined as the absolute difference between the desired output and the actual output of the network
     * @param desired_output value expected at the neural network's output
     * @return the error (in terms of squared absolute difference)
     */
    public double getError(double desired_output) {

        return (Math.pow(Math.abs(desired_output - output_Signal), 2));
    }

    /**
     * Printing an artificial neural network in the console (especially for debugging purposes)
     */
    public void printNetwork() {
        //For each hidden layer
        for (int i =0; i < layers_array.size();i++) {
            System.out.println("########Layer "+i);

            //For each neurons
            for (int j=0; j < layers_array.get(i).getSizeLayer(); j++) {
                System.out.println("__Neuron = "+ j);

                //For each weight
                for (int k=0; k< layers_array.get(i).get_nth_Neurons(j).getNbInputWeights(true); k++) {
                    System.out.println("weight["+k+"] = "+layers_array.get(i).get_nth_Neurons(j).getInput_nth_weight(k)+"activation function["+k+"] = "+layers_array.get(i).get_nth_Neurons(j).getActivation_function_id());
                }
            }
        }
    }

}
