package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

public class FeedforwardNeuralNetwork {

   private final double _volatility;
   private final FeedforwardNeuron[][] _neurons;
   private final FeedforwardNeuron[] _inputNeurons;
   private final FeedforwardNeuron[] _outputNeurons;
   private final int _edgeCount;

   public FeedforwardNeuralNetwork(int inputNodeCount, int[] hidden,
      int outputNodeCount, ActivationFunction activationFunction,
      double volatility) {

      this(
         inputNodeCount,
         hidden,
         outputNodeCount,
         activationFunction,
         activationFunction,
         volatility);
   }

   public FeedforwardNeuralNetwork(int inputNodeCount, int[] hidden,
      int outputNodeCount, ActivationFunction hiddenLayerFunction,
      ActivationFunction outputLayerFunction, double volatility) {

      _volatility = volatility;
      _neurons = new FeedforwardNeuron[hidden.length + 2][];

      // Output layer
      _neurons[_neurons.length - 1] = createNeuronArray(outputNodeCount,
         null,
         outputLayerFunction);
      _outputNeurons = _neurons[_neurons.length - 1];
      
      int edgeCount = 0;

      // Hidden layer(s)
      FeedforwardNeuron[] nextLayer = _neurons[_neurons.length - 1];
      for (int i = hidden.length - 1; i >= 0; i--) {
         _neurons[i + 1] = createNeuronArray(hidden[i],
            nextLayer,
            hiddenLayerFunction);
         edgeCount += hidden[i] * nextLayer.length;
         nextLayer = _neurons[i + 1];
      }

      // Input layer
      _neurons[0] = createNeuronArray(inputNodeCount, nextLayer, null);
      _inputNeurons = _neurons[0];
      edgeCount += inputNodeCount * nextLayer.length;
      _edgeCount = edgeCount;
   }
   
   public int getEdgeCount() {
      return _edgeCount;
   }

   private FeedforwardNeuron[] createNeuronArray(int size,
      FeedforwardNeuron[] nextLayer, ActivationFunction function) {

      final FeedforwardNeuron[] result = new FeedforwardNeuron[size];
      for (int i = 0; i < size; i++) {
         result[i] = new FeedforwardNeuron(nextLayer, function);
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

      // Hidden
      for (int i = _neurons.length - 2; i >= 1; i--) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].calculateDifferentials();
            _neurons[i][j].adjustWeights(_volatility);
         }
      }

      // Input
      for (int i = 0; i < _inputNeurons.length; i++) {
         _inputNeurons[i].adjustWeights(_volatility);
      }
   }

   public double[] getOutput() {
      final double[] result = new double[_outputNeurons.length];
      for (int i = 0; i < _outputNeurons.length; i++) {
         result[i] = _outputNeurons[i].getOutput();
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
      for (int i = 0; i < _outputNeurons.length; i++) {
         final double output = _outputNeurons[i].getOutput();
         totalError += (output - targetOutput[i]) * (output - targetOutput[i]);
      }
      return Math.sqrt(totalError);
   }
}
