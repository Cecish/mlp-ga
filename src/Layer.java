import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Cecile RIQUART
 * Date: 10/25/15
 * Time: 6:20 PM
 * Architecture of a layer of the Artificial Neural Network
 */
public class Layer {

    //Attributes ====================================================
    private List<Neuron> neurons_array; //The array of neurons in this layer

    //Constructor ===================================================
    /**
     * In order to build a neural layer, one must first specify its number neurons and
     * the number of input weights for each of them.
     * Building a neural layer means adding neurons to this layer
     * @param nb_neurons number of neurons in a layer
     * @param neuron_nb_input_weights number of input weights for each neuron in the layer
     */
    public Layer(int nb_neurons, int neuron_nb_input_weights) {
        int i;
        Neuron n;

        this.neurons_array = new ArrayList<>();

        for (i=0; i<nb_neurons; i++) {
            n = new Neuron(neuron_nb_input_weights);
            neurons_array.add(i, n);
        }
    }

    //Accessors + Methods ============================================
    /**
     * Getter for accessing the nth neuron of the layer
     * @param pos index/position the neuron to be accessed in the layer
     * @return  the nth neuron of the layer
     */
    public Neuron get_nth_Neurons(int pos) {
        Neuron res = null;

        try {
            res = neurons_array.get(pos);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return res;
    }

    /**
     * Determining the size of a layer
     * @return  the size of the layer i.e. the number of neurons in the layer
     */
    public int getSizeLayer() {
        return neurons_array.size();
    }
}

