import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eisti
 * Date: 10/28/15
 * Time: 9:34 PM
 * Architecture of a population of chromosomes
 */
public class Population {

    //Attributes ====================================================
    private List<Chromosome> pop;  //List of chromosomes
    private int pop_size;    //Number of chromosomes

    //Constructors ==================================================
    /**
     * Building a population
     * @param nb_chromosomes number of individuals in the population
     * @param nb_genes number of genes
     * @param training_type how the user wants to train the MLP
     */
    public Population(int nb_chromosomes, int nb_genes, int training_type) {
        int i;

        this.pop_size = nb_chromosomes;
        this.pop = new ArrayList<>();

        for (i=0; i< pop_size; i++) {
            pop.add(i,new Chromosome(nb_genes, training_type));
        }
    }

    //Accessors + Methods ============================================
    /**
     * Getting the number chromosomes/individuals in the population
     * @return the number of chromosomes in the population
     */
    public int getPop_size() {
        return pop_size;
    }

    /**
     * Getting the nth chromosome of the population
     * @param pos index/position of the chromosome to be accessed
     * @return the nth chromosome of the population
     */
    public Chromosome get_nth_chromosome(int pos) {
        Chromosome res = null;

        try {
            res = pop.get(pos);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return res;
    }

    /**
     * Reproducing the population with fittest individuals so that the next generation can get closer to the expecting result
     * @param nb_weights_per_chromosome umber of hidden weights
     * @param nb_layer
     * @param training_type the nth chromosome of the population
     */
    public void populationReproduction(int nb_weights_per_chromosome, int nb_layer, int training_type) {
        int i=0;
        List<Chromosome> new_population = new ArrayList<>();
        Chromosome child1 = new Chromosome(nb_weights_per_chromosome, training_type);
        Chromosome child2 = new Chromosome(nb_weights_per_chromosome, training_type);

        //Sorting the population by fitness (ascending order)
        Collections.sort(pop,Chromosome.ChromosomeComparatorByFitness);

        /*for (i=0; i< Params.ELITISM; i++) {
            new_population.add(pop.get(i));
            //System.out.println("========================================BEST CHROMS FROM LAST POP"+i+"\n");
            //pop.get(i).printChromosome();
            //System.out.println("=================fitness = "+pop.get(i).getFitness()+"\n");
        } */

        //Until the next generation is complete
        while (new_population.size() < pop.size()) {

            switch (training_type) {
                case 1 :
                    new_population = reproduceWeightsOnly(child1, child2, new_population, nb_weights_per_chromosome);
                    break;
                case 2 :
                    new_population = reproduceActivationFunctionOnly(child1, child2, new_population, nb_weights_per_chromosome);
                    break;
                case 3 :
                    new_population = reproduceWeightsAndActivationFunction(child1, child2, new_population, nb_weights_per_chromosome);
                    break;
            }
        }

        this.pop = new_population;
    }

    /**
     * Only training the weights during the process of the population reproduction
     * @param child1 First child produced by parent1 and parent2. It will be part of the new population
     * @param child2 Second child produced by parent1 and parent2. It will be part of the new population
     * @param new_population New population to be produced
     * @param nb_weights_per_chromosome Number of hidden weights
     */
    private List<Chromosome> reproduceWeightsOnly(Chromosome child1, Chromosome child2, List<Chromosome> new_population, int nb_weights_per_chromosome) {
        Chromosome parent1, parent2;
        int delimiter_unchanged = (int) (pop_size * (1-Params.crossover_rate_weights));
        Double rand_pos_m = -1.0, temp;

        for (int i=0; i< pop_size; i++) {
            if (i<= delimiter_unchanged) {
                new_population.add(pop.get(i));

            } else {
                //Selection : tournament selection
                ArrayList<Chromosome> selec = new ArrayList<>();
                for (int l =0 ; l< 4; l++) {
                    Double randoom = Params.random.nextDouble() * (pop_size-1);
                    selec.add(pop.get(randoom.intValue()));
                }
                Collections.sort(selec, Chromosome.ChromosomeComparatorByFitness);
                parent1 = pop.get(0);
                parent2 = pop.get(1);

                //Crossover : one-point crossover
                Double rand_pos = (nb_weights_per_chromosome) * (Params.random.nextDouble());

                List<Double> weights1 = new ArrayList<>();
                List<Double> weights2 = new ArrayList<>();

                for (int j=0; j< nb_weights_per_chromosome; j++) {
                    if ((j <= rand_pos.intValue())   ) {
                        weights1.add(parent1.get_nth_weight(j));
                        weights2.add(parent2.get_nth_weight(j));
                    } else {
                        weights1.add(parent2.get_nth_weight(j));
                        weights2.add(parent1.get_nth_weight(j));
                    }
                }

                child1.setGenes_array(weights1);
                child2.setGenes_array(weights2);

                //Mutation
                Double nb_to_mutate = (nb_weights_per_chromosome * Params.mutation_rate_weights);
                for (int k =0; k< nb_to_mutate.intValue(); k++) {
                    temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);

                    rand_pos_m = temp;
                    //child1.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + (0.05 * (-0.2 + (0.2 - (-0.2)) * Params.random.nextDouble())) );
                    //child2.set_nth_weight(rand_pos_m.intValue(), child2.get_nth_weight(rand_pos_m.intValue()) + (0.05 * (-0.2 + (0.2 - (-0.2)) * Params.random.nextDouble())) );
                    //child1.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + ( (Params.MUTATION_PERTURBATION) * (Params.random.nextDouble()*(1+1) -1) ) );
                    //child2.set_nth_weight(rand_pos_m.intValue(), child2.get_nth_weight(rand_pos_m.intValue()) + ( (Params.MUTATION_PERTURBATION) * (Params.random.nextDouble()*(1+1) -1) ) );
                    //child1.set_nth_weight(rand_pos_m.intValue(), MultilayerPerceptron.MIN_WEIGHT + (MultilayerPerceptron.MAX_WEIGHT - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble());
                    //child2.set_nth_weight(rand_pos_m.intValue(), MultilayerPerceptron.MIN_WEIGHT + (MultilayerPerceptron.MAX_WEIGHT - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble());
                    child1.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + ( -0.3 + (0.3 + 0.3)*Params.random.nextDouble() ) );
                    child2.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + ( -0.3 + (0.3 + 0.3)*Params.random.nextDouble() ) );
                    //parent1.set_nth_weight(rand_pos_m.intValue(), parent1.get_nth_weight(rand_pos_m.intValue()) + ( -0.2 + (0.2 + 0.2)*Params.random.nextDouble() ) );
                    //parent2.set_nth_weight(rand_pos_m.intValue(), parent2.get_nth_weight(rand_pos_m.intValue()) + ( -0.2 + (0.2 + 0.2)*Params.random.nextDouble() ) );
                }

                new_population.add(child1);
                new_population.add(child2);
            }
        }

        return new_population;
    }

    /**
     * Only training the activation functions during the process of the population reproduction
     * @param child1 First child produced by parent1 and parent2. It will be part of the new population
     * @param child2 Second child produced by parent1 and parent2. It will be part of the new population
     * @param new_population New population to be produced
     * @param nb_weights_per_chromosome Number of hidden weights = number of activation function IDs
     */
    private List<Chromosome> reproduceActivationFunctionOnly(Chromosome child1, Chromosome child2, List<Chromosome> new_population, int nb_weights_per_chromosome) {
        Chromosome parent1, parent2;
        int delimiter_unchanged = (int) (pop_size * (1-Params.crossover_rate_activation_functions));
        Double rand_pos_m = -1.0, temp;

        for (int i=0; i< pop_size; i++) {
            if (i<= delimiter_unchanged) {
                new_population.add(pop.get(i));
            } else {
                //Selection : tournament selection
                ArrayList<Chromosome> selec = new ArrayList<>();
                for (int l =0 ; l< 4; l++) {
                    Double randoom = Params.random.nextDouble() * (pop_size-1);
                    selec.add(pop.get(randoom.intValue()));
                }
                Collections.sort(selec, Chromosome.ChromosomeComparatorByFitness);
                parent1 = selec.get(0);
                parent2 = selec.get(1);

                //Crossover : one-point crossover
                Double rand_pos = (nb_weights_per_chromosome) * (Params.random.nextDouble());
                List<Double> weights1 = new ArrayList<>();
                List<Double> weights2 = new ArrayList<>();
                List<Integer> IDfunctions1 = new ArrayList<>();
                List<Integer> IDfunctions2 = new ArrayList<>();

                for (int j=0; j< nb_weights_per_chromosome; j++) {
                    if ((j <= rand_pos.intValue())   ) {
                        weights1.add(parent1.get_nth_weight(j));
                        weights2.add(parent2.get_nth_weight(j));
                        IDfunctions1.add(parent1.get_nth_ID(j));
                        IDfunctions2.add(parent2.get_nth_ID(j));
                    } else {
                        //The weights are not evolving here (only the activation function ID)
                        weights1.add(parent1.get_nth_weight(j));
                        weights2.add(parent2.get_nth_weight(j));
                        IDfunctions1.add(parent2.get_nth_ID(j));
                        IDfunctions2.add(parent1.get_nth_ID(j));
                    }
                }

                child1.setGenes_array(weights1);
                child2.setGenes_array(weights2);
                child1.setActivation_function_array(IDfunctions1);
                child2.setActivation_function_array(IDfunctions2);

                //Mutation
                Double nb_to_mutate = (nb_weights_per_chromosome * Params.mutation_rate_activation_functions);
                for (int k =0; k< nb_to_mutate; k++) {
                    temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);
                    while (temp.intValue() == rand_pos_m.intValue()) {
                        temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);
                    }
                    rand_pos_m = temp;
                    Double temp2 = (Params.random.nextDouble() * 4.9);

                    child1.set_nth_ID(rand_pos_m.intValue(), temp2.intValue() );
                    child2.set_nth_ID(rand_pos_m.intValue(), temp2.intValue() );
                }

                new_population.add(child1);
                new_population.add(child2);
            }
        }

        return new_population;
    }

    private List<Chromosome> reproduceWeightsAndActivationFunction(Chromosome child1, Chromosome child2, List<Chromosome> new_population, int nb_weights_per_chromosome) {
        Chromosome parent1, parent2;
        int delimiter_unchanged = (int) (pop_size * (1-Params.crossover_rate_weights));
        Double rand_pos_m = -1.0, temp;

        for (int i=0; i< pop_size; i++) {
            if (i<= delimiter_unchanged) {
                new_population.add(pop.get(i));

            } else {
                //Selection : tournament selection
                ArrayList<Chromosome> selec = new ArrayList<>();
                for (int l =0 ; l< 4; l++) {
                    Double randoom = Params.random.nextDouble() * (pop_size-1);
                    selec.add(pop.get(randoom.intValue()));
                }
                Collections.sort(selec, Chromosome.ChromosomeComparatorByFitness);
                parent1 = pop.get(0);
                parent2 = pop.get(1);

                //Crossover : one-point crossover
                Double rand_pos = (nb_weights_per_chromosome) * (Params.random.nextDouble());
                List<Double> weights1 = new ArrayList<>();
                List<Double> weights2 = new ArrayList<>();
                List<Integer> IDfunctions1 = new ArrayList<>();
                List<Integer> IDfunctions2 = new ArrayList<>();

                for (int j=0; j< nb_weights_per_chromosome; j++) {
                    if ((j <= rand_pos.intValue())   ) {
                        weights1.add(parent1.get_nth_weight(j));
                        weights2.add(parent2.get_nth_weight(j));
                        IDfunctions1.add(parent1.get_nth_ID(j));
                        IDfunctions2.add(parent2.get_nth_ID(j));
                    } else {
                        weights1.add(parent2.get_nth_weight(j));
                        weights2.add(parent1.get_nth_weight(j));
                        IDfunctions1.add(parent2.get_nth_ID(j));
                        IDfunctions2.add(parent1.get_nth_ID(j));
                    }
                }

                child1.setGenes_array(weights1);
                child2.setGenes_array(weights2);
                child1.setActivation_function_array(IDfunctions1);
                child2.setActivation_function_array(IDfunctions2);

                //Mutation
                Double nb_to_mutate_weights = (nb_weights_per_chromosome * Params.mutation_rate_weights);
                Double nb_to_mutate_id = (nb_weights_per_chromosome * Params.mutation_rate_activation_functions);
                for (int k =0; k< nb_to_mutate_weights.intValue(); k++) {
                    temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);
                    while (temp.intValue() == rand_pos_m.intValue()) {
                        temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);
                    }
                    rand_pos_m = temp;
                    Double temp2  = (Params.random.nextDouble() * (4.9 - 1)) + 1;

                    //child1.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + (0.05 * (-0.2 + (0.2 - (-0.2)) * Params.random.nextDouble())) );
                    //child2.set_nth_weight(rand_pos_m.intValue(), child2.get_nth_weight(rand_pos_m.intValue()) + (0.05 * (-0.2 + (0.2 - (-0.2)) * Params.random.nextDouble())) );
                    //child1.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + ( (Params.MUTATION_PERTURBATION) * (Params.random.nextDouble()*(1+1) -1) ) );
                    //child2.set_nth_weight(rand_pos_m.intValue(), child2.get_nth_weight(rand_pos_m.intValue()) + ( (Params.MUTATION_PERTURBATION) * (Params.random.nextDouble()*(1+1) -1) ) );
                    //child1.set_nth_weight(rand_pos_m.intValue(), MultilayerPerceptron.MIN_WEIGHT + (MultilayerPerceptron.MAX_WEIGHT - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble());
                    //child2.set_nth_weight(rand_pos_m.intValue(), MultilayerPerceptron.MIN_WEIGHT + (MultilayerPerceptron.MAX_WEIGHT - MultilayerPerceptron.MIN_WEIGHT) * Params.random.nextDouble());
                    child1.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + ( -0.3 + (0.3 + 0.3)*Params.random.nextDouble() ) );
                    child2.set_nth_weight(rand_pos_m.intValue(), child1.get_nth_weight(rand_pos_m.intValue()) + ( -0.3 + (0.3 + 0.3)*Params.random.nextDouble() ) );
                }

                for (int k =0; k< nb_to_mutate_id; k++) {
                    temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);
                    while (temp.intValue() == rand_pos_m.intValue()) {
                        temp = Params.random.nextDouble() * (nb_weights_per_chromosome - 1);
                    }
                    rand_pos_m = temp;
                    //Double temp2  = (Params.random.nextDouble() * (4.9 - 1)) + 1;
                    Double temp2 = (Params.random.nextDouble() * 4.9);

                    child1.set_nth_ID(rand_pos_m.intValue(), temp2.intValue() );
                    child2.set_nth_ID(rand_pos_m.intValue(), temp2.intValue() );
                }

                new_population.add(child1);
                new_population.add(child2);
            }
        }

        return new_population;
    }

    /**
     * Returning the best chromosome of the population (the one who has the smallest fitness value)
     * @return the best chromosome of the population
     */
    public Chromosome getBestChromosome() {
        double best_fitness = pop.get(0).getFitness();
        int index = 0, i;

        for (i=1; i< pop_size; i++) {
            if (pop.get(i).getFitness()<best_fitness) {
                index = i;
                best_fitness = pop.get(i).getFitness();
            }
        }

        return pop.get(index);
    }

    /**
     * Printing a population (for debugging purposes)
     */
    public void printPopulation() {
        for (int i=0; i< pop_size; i++) {
            this.pop.get(i).printChromosome();
        }
    }

    /**
     * Printing the fitness of each individual of the population (for debugging purposes)
     */
    public void printFitness() {
        System.out.println("--------------------");
        for (int i=0; i< pop_size; i++) {
            System.out.println(pop.get(i).getFitness());
        }
        System.out.println("--------------------");
    }


}