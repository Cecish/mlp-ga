# mlp-ga
A multi-layer perceptron (MLP) artificial neural network evolved with a genetic algorithm for a task of function approximation.

The implemented MLP is trained with genetic algorithm, which is used to evolve:
- The MLP connection weights,
- The activation functions,
- Part of the MLP topology (the number of hidden neurons in each hidden layer).

## Launching the program
The main class of the projet is *MultiLayerPerceptron.java*

## Customize execution settings
Once the program is running, follow the instructions displayed to choose some parameters for the MLP.

When running the program, you are given the choice to:
1. Only evolve the weights of the artificial neural network,
2. Only evolve the activation function (among a selection of activation function candidates: null, sigmoid, cosine, Gaussian and hyperbolic tangeant functions),
3. Evolve the MLP weights, activation function and the number of neurons in each hidden layer.

Regarding the genetic algorithm parameters, you will be prompted to provide:
- Crossover and mutation rates for each part of the MLP you have decided to evolve (i.e. its weights, activation function, topology),
- The size of the initial population
- The number of epochs

The program comes with a set of functions to approximate:
1. Linear function
2. Cubic function
3. Sine function
4. Tanh function
5. XOR function
6. A complex function

You will be prompted to select which one you would like to approximate at runtime.

These functions' input and output datasets, used for training the MLP, are provided at the root of the project.

*Nota bene* : Some parameters are not asked and are defined (and may be changed by you) in *Params.java*

## Plotting the approximated and activation functions
The graphs of the approximated functions and the activation function used by the MLP are not displayed at runtime. Instead, the programm generates output files: *MSE_used_in_error_evolution.output* and *fitness_evolution.output*. You can plot them using gnuplot for instance :
	
	gnuplot
	plot "fitness_evolution.output"

## Important notes
This MLP evolved with a genetic algorithm has been submitted as part of a university assessment and is licensed here under the Apache License, version 2.0.

Plagiarism is :thumbsdown: and you are not allowed to re-use any part of this project without giving due credit.
