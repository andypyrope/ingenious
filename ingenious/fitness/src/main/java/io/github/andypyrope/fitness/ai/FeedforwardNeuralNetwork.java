package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

public class FeedforwardNeuralNetwork {

   private final double _volatility;
   private final ActivationFunction _function;
   private final FeedforwardNeuron[][] _neurons;

   public FeedforwardNeuralNetwork(int inputNodeCount, int[] hidden,
      int outputNodeCount, ActivationFunction function, double volatility) {

      _volatility = volatility;
      _function = function;
      _neurons = new FeedforwardNeuron[hidden.length + 2][];

      // Output layer
      _neurons[_neurons.length - 1] = createNeuronArray(outputNodeCount, null);

      // Hidden layer(s)
      FeedforwardNeuron[] nextLayer = _neurons[_neurons.length - 1];
      for (int i = hidden.length - 1; i >= 0; i--) {
         _neurons[i + 1] = createNeuronArray(hidden[i], nextLayer);
         nextLayer = _neurons[i + 1];
      }

      // Input layer
      _neurons[0] = createNeuronArray(inputNodeCount, nextLayer);
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
      for (int i = 0; i < _neurons[0].length; i++) {
         _neurons[0][i].setOutput(inputValues[i]);
         _neurons[0][i].feedIntoNextLayer();
      }

      // Hidden / Output
      for (int i = 1; i < _neurons.length; i++) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].updateOutput();
            _neurons[i][j].feedIntoNextLayer();
         }
      }
   }

   public void backpropagate(double[] targetOutput) {
      // Assuming it's already been calculated

      // Output
      for (int i = 0; i < _neurons[_neurons.length - 1].length; i++) {
         _neurons[_neurons.length - 1][i]
                  .calculateDifferentials(targetOutput[i]);
      }

      // Hidden / Input
      for (int i = _neurons.length - 2; i >= 0; i--) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].calculateDifferentials();
            _neurons[i][j].adjustWeights(_volatility);
         }
      }
   }

   public double getError(double[] targetOutput) {
      double totalError = 0.0;
      for (int i = 0; i < _neurons[_neurons.length - 1].length; i++) {
         totalError += _neurons[_neurons.length - 1][i]
                  .getError(targetOutput[i]);
      }
      return totalError / 2.0;
   }
}
