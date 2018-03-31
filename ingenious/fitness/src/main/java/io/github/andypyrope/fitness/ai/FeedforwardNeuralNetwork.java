package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

public class FeedforwardNeuralNetwork {

   private final double _volatility;
   private final ActivationFunction _function;
   private final FeedforwardNeuron[][] _neurons;
   private final FeedforwardNeuron[] _inputNeurons;
   private final FeedforwardNeuron[] _outputNeurons;

   public FeedforwardNeuralNetwork(int inputNodeCount, int[] hidden,
      int outputNodeCount, ActivationFunction function, double volatility) {

      _volatility = volatility;
      _function = function;
      _neurons = new FeedforwardNeuron[hidden.length + 2][];

      // Output layer
      _neurons[_neurons.length - 1] = createNeuronArray(outputNodeCount, null);
      _outputNeurons = _neurons[_neurons.length - 1];

      // Hidden layer(s)
      FeedforwardNeuron[] nextLayer = _neurons[_neurons.length - 1];
      for (int i = hidden.length - 1; i >= 0; i--) {
         _neurons[i + 1] = createNeuronArray(hidden[i], nextLayer);
         nextLayer = _neurons[i + 1];
      }

      // Input layer
      _neurons[0] = createNeuronArray(inputNodeCount, nextLayer);
      _inputNeurons = _neurons[0];
   }

   private FeedforwardNeuron[] createNeuronArray(int size,
      FeedforwardNeuron[] nextLayer) {

      final FeedforwardNeuron[] result = new FeedforwardNeuron[size];
      for (int i = 0; i < size; i++) {
         result[i] = new FeedforwardNeuron(nextLayer, _function);
      }
      return result;
   }

   public void calculate(double[] inputValues) {
      for (int i = 0; i < _neurons.length; i++) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].resetNetInput();
         }
      }

      // Input
      for (int i = 0; i < _inputNeurons.length; i++) {
         _inputNeurons[i].setOutput(inputValues[i]);
         _inputNeurons[i].feedIntoNextLayer();
      }

      // Hidden / Output
      for (int i = 1; i < _neurons.length; i++) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].updateOutput();
            _neurons[i][j].feedIntoNextLayer();
         }
      }
   }

   /**
    * Runs the backpropagation algorithm to improve the weights/biases.
    * 
    * @param targetOutput The expected output
    */
   public void adjust(double[] targetOutput) {
      // Assuming it's already been calculated

      // Output
      for (int i = 0; i < _outputNeurons.length; i++) {
         _outputNeurons[i].calculateDifferentials(targetOutput[i]);
      }

      // Hidden / Input
      for (int i = _neurons.length - 2; i >= 0; i--) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].calculateDifferentials();
            _neurons[i][j].adjustWeights(_volatility);
         }
      }
   }

   public double[] getOutput() {
      final int outputCount = _neurons[_neurons.length - 1].length;
      final double[] result = new double[outputCount];
      for (int i = 0; i < outputCount; i++) {
         result[i] = _neurons[_neurons.length - 1][i].getOutput();
      }
      return result;
   }

   /**
    * @param targetOutput The expected output
    * @return The euclidean distance between the actual output and an expected
    *         one
    */
   public double getEucliedanDistance(double[] targetOutput) {
      double totalError = 0.0;
      for (int i = 0; i < _neurons[_neurons.length - 1].length; i++) {
         final double output = _neurons[_neurons.length - 1][i].getOutput();
         totalError += (output - targetOutput[i]) * (output - targetOutput[i]);
      }
      return Math.sqrt(totalError);
   }
}
