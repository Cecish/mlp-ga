import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Cécile RIQUART
 * Date: 10/28/15
 * Time: 4:50 PM
 * Architecture of a chromosome. For the multilayer perceptron, one chromosome corresponds to
 * weights of hidden layers and array of activation functions' ID
 */
public class Chromosome {

    //Attributes ====================================================
    private List<Double> genes_array; //Array of (hidden + output) weights
    private List<Integer> activation_function_array; //Activation functions' ID for each neuron
    private List<Integer> array_weights_per_layer; //Number of weights per layer (the number of layer = activation_function_array.size())
    private double fitness; //Fitness value for a chromosome

    //Constructors ==================================================
    /**
     * Building a chromosome. Initially, weight values (in the array og genes) are set randomly
     * between MIN_WEIGHT and MAX_WEIGHT
     * @param nb_hidden_weights number of genes corresponding number of hidden weights in the network + bias
     * @param training_type how the user wants to train the MLP
     */
    public Chromosome(int nb_hidden_weights, int training_type) {
        int i, random_id;

        this.genes_array = new ArrayList<>();
        this.activation_function_array = new ArrayList<>();
        this.array_weights_per_layer = new ArrayList<>();

        /*for (i=0; i< nb_hidden_weights; i++) {

            genes_array.add( ( MultilayerPerceptron.MIN_WEIGHT
                    + (MultilayerPerceptron.MAX_WEIGHT
                    - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble()) );
        }     */
        //Léon Bottou weights initialisation
        double a = 2.38;
        double min_range, max_range;
        for (i=0; i < nb_hidden_weights; i++) {
            if (i<2) {
                genes_array.add( ( MultilayerPerceptron.MIN_WEIGHT
                    + (MultilayerPerceptron.MAX_WEIGHT
                    - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble()) );
            } else {
                Neuron n = new Neuron(4);
                min_range = (-a)/(Math.sqrt(n.getNbInputWeights(true)));
                max_range = (a)/(Math.sqrt(n.getNbInputWeights(true)));

                double random_weight = min_range + (max_range - min_range) * Params.random.nextDouble();
                genes_array.add(i,random_weight);
            }
        }

        //Only for the very beginning. Then the number of neurons per HIDDEN LAYERS will evolve
        for (i=0; i< activation_function_array.size(); i++) {
            if (i==activation_function_array.size()-1) { //output layer
                array_weights_per_layer.add(1); //One output
            } else {//Hidden layers
                array_weights_per_layer.add(3);
            }
        }

        for (i=0; i< nb_hidden_weights; i++) {

            if (training_type == 1) {
                activation_function_array.add(1);
            } else {
                random_id = (int) ((5) * Params.random.nextDouble());
                activation_function_array.add(random_id);
            }
        }


    }

    /**
     * A less detailed chromosome constructor
     */
    public Chromosome() {
        this.genes_array = new ArrayList<>();
        this.activation_function_array = new ArrayList<>();
    }

    //Accessors + Methods ============================================
    /**
     * Getting the chromosome's fitness value
     * @return the chromosome's fitness value
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Getting the nth gene
     * @param pos index/position of the weight to be accessed
     * @return the nth weight
     */
    public double get_nth_weight(int pos) {
        double res_weight = 0;

        try {
            res_weight = genes_array.get(pos);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return res_weight;
    }

    /**
     * Getting the weights of an individual
     * @return the desired weights (array)
     */
    public List<Double> getGenes_array() {
        return genes_array;
    }

    /**
     * Setting the weights of an individual
     * @param genes_array new array of weights
     */
    public void setGenes_array(List<Double> genes_array) {
        this.genes_array = genes_array;
    }

    /**
     * Getting the nth activation function ID
     * @param pos index/position of the nth activation function id to access
     * @return the nth activation function id
     */
    public int get_nth_ID(int pos) {
        int res_ID = 0;

        try {
            res_ID = activation_function_array.get(pos);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return res_ID;
    }

    /**
     * Setting the nth activation function ID
     * @param pos index/position of the nth activation function id to change
     * @param id new value for the nth activation function id
     */
    public void set_nth_ID(int pos, int id) {

        try {
            activation_function_array.set(pos, id);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Setting the fitness value
     * @param fitness new value of the chromosome's fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Setting the nth gene
     * @param pos index/position of the weight to be modified
     * @param weight new value of such weight
     */
    public void set_nth_weight(int pos, double weight) {

        try {
            genes_array.set(pos, weight);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Getting the number of activation function IDs
     * @return the number of activation function IDs
     */
    public int getNbID() {
        return activation_function_array.size();
    }

    /**
     * Getting the array of activation function IDs
     * @return the array of activation function IDs of an individual
     */
    public List<Integer> getActivation_function_array() {
        return activation_function_array;
    }

    /**
     * Setting all the activation function IDs of an individual
     * @param activation_function_array new array of action function IDs
     */
    public void setActivation_function_array(List<Integer> activation_function_array) {
        for (int i=0; i< activation_function_array.size(); i++) {
            this.activation_function_array.set(i, activation_function_array.get(i));
        }
    }

    /**
     * Chromosome comparator. Chromosomes can be compared according to their fitness value.
     * For instance Chromosome1 is "smaller" than Chromosome2 if it has a smaller fitness value
     */
    public static Comparator<Chromosome> ChromosomeComparatorByFitness = new Comparator<Chromosome>() {
        @Override
        public int compare(Chromosome o1, Chromosome o2) {
            int res;

            if (o1.getFitness() < o2.getFitness()) {
                res =-1;
            } else if (o1.getFitness() > o2.getFitness()) {
                res = 1;
            } else {
                res = 0;
            }
            return res;
        }
    };

    /**
     * Modification of some weights value (randomly chosen) depending one the mutation rate
     * @param mutation_rate value of the mutation rate : likelihood of a mutation happening
     * @param mutation_perturbation max value authorised when changing/mutating weights
     */
    public void mutation(double mutation_rate, double mutation_perturbation) {
        Double rand_pos;
        double rand_weight;

        if (Params.random.nextDouble() <= mutation_rate) {
            rand_weight = -0.02 + (0.02 - (-0.02)) * Params.random.nextDouble();
            rand_pos = (Params.random.nextDouble() * (genes_array.size() -1) );

            this.set_nth_weight(rand_pos.intValue(), this.get_nth_weight(rand_pos.intValue()) + (rand_weight * mutation_perturbation));
            //System.out.println("weight = "+this.get_nth_weight(rand_pos.intValue()));
        }
    }

    /**
     * Printing a Chromosome in the console (especially for debugging purposes)
     */
    public void printChromosome() {
        int i;

        System.out.print("Chromosome weights: [ ");
        for(i = 0; i< genes_array.size(); i++) {
            System.out.print(" " + genes_array.get(i) + " ");
        }
        System.out.println("]");

        System.out.print("Chromosome activation array: [ ");
        for(i = 0; i< activation_function_array.size(); i++) {
            System.out.print(" " + activation_function_array.get(i) + " ");
        }
        System.out.println("]");
    }
}
