import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: eisti
 * Date: 10/25/15
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultilayerPerceptron {

    public static final double MIN_WEIGHT = -2.5;
    public static final double MAX_WEIGHT = 2.5;

    private static void trainMLP(int training_type, Params params) throws IOException {
        UsefulMethods u = new UsefulMethods();
        double test_temp = 0.0;
        Network net = new Network(params.getNb_input_neurons(), params.getNb_ouput_neurons(), params.getNb_hidden_layers(),params.getNb_hidden_neurons_per_layer(), training_type);
        Population pop = new Population(Params.pop_size, net.getTotalWeightsNb(true), training_type);
        Double[] output_data;
        Double[][] dataset = new Double[params.getNb_rows_dataset()][params.getNb_input_neurons()+1];
        ArrayList<Double[]> input_data = new ArrayList<>();
        dataset = u.file_to_matrix(params.getFilename(), params.getNb_rows_dataset(), params.getNb_input_neurons() + 1);
        Double[] input1, input2 = new Double[0];

        u.DoubleMatrixPrinting(dataset, "coucou", dataset.length, dataset[0].length);
        if ((params.getFilename() == "2in_xor_comas.txt") || (params.getFilename() == "2in_complex_comas.txt")) { // 2 inputs
            input1 = u.getColumn(0, dataset);
            input2 = u.getColumn(1, dataset);
            output_data = u.getColumn(2, dataset);
            input_data.add(input1);
            input_data.add(input2);
        } else { //one input
            input1 = u.getColumn(0, dataset);
            output_data = u.getColumn(1, dataset);
            input_data.add(input1);
        }
        ArrayList<Double> fitness_evolution = new ArrayList<>();

        for (int i=0; i< input_data.size();i++) {
            for (int j=0; j < input_data.get(0).length; j++) {
                System.out.println(input_data.get(i)[j]);
            }
        }

        double current_error = 0.0;
        double best_fitness = Double.POSITIVE_INFINITY;
        fitness_evolution.add(best_fitness);
        int i = 0, j, k;

        while ((best_fitness > Params.ACCEPTABLE_ERROR) && (i < Params.nb_epochs)) {
            System.out.println("### EPOCH N° " + i);

            //For each chromosome
            for(j=0; j< pop.getPop_size(); j++) {
                ArrayList<Double[]> input = new ArrayList<>();
                current_error = 0.0;
                for (k=0; k< input_data.size(); k++) {
                    input.add(input_data.get(k));
                }

                ArrayList<Double> ultimate_test = new ArrayList<>();
                ultimate_test.add(0.0);
                ultimate_test.add(0.0);


                for (k=0; k < input_data.get(0).length; k++) {
                    if (input_data.size() == 2) {
                        ultimate_test.set(0, input1[k]);
                        ultimate_test.set(1, input2[k]);
                    } else {
                        ultimate_test.set(0, input1[k]);
                    }
                    net.feedForwardPropagation(ultimate_test, pop.get_nth_chromosome(j), training_type);
                    current_error += net.getError(output_data[k])/input_data.get(0).length;
                }

                test_temp = (1 / (1 + current_error));

                pop.get_nth_chromosome(j).setFitness(current_error);
                //pop.get_nth_chromosome(j).setFitness(test_temp);
            }

            best_fitness = pop.getBestChromosome().getFitness();
            fitness_evolution.add(best_fitness);
            pop.printPopulation(); //Comment this line to not print each individual from the population at each generation

            pop.populationReproduction(net.getTotalWeightsNb(true), params.getNb_hidden_layers() + 1, training_type);
            i++;
        }
        //The population is alreday sorted (by ascending order of fitness)
        //Build the best network with the best individual
        net.updateHiddenWeights(pop.get_nth_chromosome(0));
        net.updateActivationFunctionID(pop.get_nth_chromosome(0));

        net.printNetwork();

        //Testing the final network and creating the file containing the data to be plotted
        testMLP(net, training_type, pop.get_nth_chromosome(0), params);

        u.writeMatrixToFile(fitness_evolution, "fitness_evolution.output");
        System.out.println("LAST EPOCH = "+i);
    }





    public static void testMLP(Network n, int training_type, Chromosome best_individual, Params params) throws FileNotFoundException {
        UsefulMethods u = new UsefulMethods();
        double test_temp = 0.0;
        Double[] output_data;
        Double[][] dataset = new Double[params.getNb_rows_dataset()][params.getNb_input_neurons()+1];
        ArrayList<Double[]> input_data = new ArrayList<>();
        dataset = u.file_to_matrix(params.getFilename(), params.getNb_rows_dataset(), params.getNb_input_neurons() + 1);
        Double[] input1, input2 = new Double[0];
        ArrayList<Double> res = new ArrayList<>();

        u.DoubleMatrixPrinting(dataset, "coucou", dataset.length, dataset[0].length);
        if ((params.getFilename() == "2in_xor_comas.txt") || (params.getFilename() == "2in_complex_comas.txt")) { // 2 inputs
            input1 = u.getColumn(0, dataset);
            input2 = u.getColumn(1, dataset);
            output_data = u.getColumn(2, dataset);
            input_data.add(input1);
            input_data.add(input2);
        } else { //one input
            input1 = u.getColumn(0, dataset);
            output_data = u.getColumn(1, dataset);
            input_data.add(input1);
        }
        ArrayList<Double> fitness_evolution = new ArrayList<>();

        for (int i=0; i< input_data.size();i++) {
            for (int j=0; j < input_data.get(0).length; j++) {
                System.out.println(input_data.get(i)[j]);
            }
        }

        double current_error = 0.0;
        double best_fitness = Double.POSITIVE_INFINITY;
        fitness_evolution.add(best_fitness);
        int i = 0, j, k;

        //while ((best_fitness > Params.ACCEPTABLE_ERROR) && (i < Params.nb_epochs)) {
            //For each chromosome
            //for(j=0; j< pop.getPop_size(); j++) {
                ArrayList<Double[]> input = new ArrayList<>();
                current_error = 0.0;
                for (k=0; k< input_data.size(); k++) {
                    input.add(input_data.get(k));
                }

                ArrayList<Double> ultimate_test = new ArrayList<>();
                ultimate_test.add(0.0);
                ultimate_test.add(0.0);


                for (k=0; k < input_data.get(0).length; k++) {
                    if (input_data.size() == 2) {
                        ultimate_test.set(0, input1[k]);
                        ultimate_test.set(1, input2[k]);
                    } else {
                        ultimate_test.set(0, input1[k]);
                    }
                    n.feedForwardPropagation(ultimate_test, best_individual, training_type);
                    res.add(n.getOutput_Signal());
                }

                test_temp = (1 / (1 + current_error));

                //pop.get_nth_chromosome(j).setFitness(test_temp);
            //}
        //}



/*      int k;
        ArrayList<Double> res = new ArrayList<>();
        UsefulMethods u = new UsefulMethods();
        ArrayList<Double[]> input = new ArrayList<>();

        for (k=0; k< input_data.size(); k++) {
            System.out.println(input_data.get(k)[0]+"coucoucocuouc");
            input.add(input_data.get(k));
        }

        for (k=0; k < input_data.get(0).length; k++) {
            System.out.println("input 0 = "+input.get(0)[0]);
            n.feedForwardPropagation(input, best_individual, training_type);
            res.add(n.getOutput_Signal());
            System.out.println("res = "+res.get(k));
        }  */

        u.writeMatrixToFileSpaces(res, input_data, "to_be_ploted.txt");
    }



    public static void main(String arg[]) throws IOException, IntegerOutOfRange {
        int choice_user, choice_function;
        double crossover_rate = 0, mutation_rate = 0; //For the weights evolution
        double crossover_rate2 = 0.0, mutation_rate2=0.0; //For the activation functions evolution (in case the user wish to evolve both the weights and the activation functions)
        int pop_size, nb_epochs;
        String filename_input;

        UsefulMethods u = new UsefulMethods();

        //Choose the MLP parameters to be evolved
        choice_user = u.menuProgram();

        if ((choice_user == 3) || (choice_user == 4)) {
            //Choose the evolutionary parameters for the weights
            crossover_rate = u.chooseDouble(0, 2);
            mutation_rate = u.chooseDouble(1, 2);
            //Choose the evolutionary parameters for the activation functions
            crossover_rate2 = u.chooseDouble(0, 3);
            mutation_rate2 = u.chooseDouble(1, 3);

        } else if (choice_user == 1) {
            //Choose the evolutionary parameters
            crossover_rate = u.chooseDouble(0, 1);
            mutation_rate = u.chooseDouble(1, 1);
        } else if (choice_user == 2) {
            //Choose the evolutionary parameters for the activation functions
            crossover_rate2 = u.chooseDouble(0, 3);
            mutation_rate2 = u.chooseDouble(1, 3);
        }

        pop_size = u.chooseInt(0);
        nb_epochs = u.chooseInt(1);

        //Choose which function the MLP should train
        choice_function = u.menuFunctionToTrain();

        filename_input = u.getFilename(choice_function);

        Params p = new Params(filename_input, choice_function);

        p.setCrossover_rate_weights(crossover_rate);
        p.setMutation_rate_weights(mutation_rate);
        p.setCrossover_rate_activation_functions(crossover_rate2);
        p.setMutation_rate_activation_functions(mutation_rate2);

        p.setPop_size(pop_size);
        p.setNb_epochs(nb_epochs);

        trainMLP(choice_user, p);

    }
}
