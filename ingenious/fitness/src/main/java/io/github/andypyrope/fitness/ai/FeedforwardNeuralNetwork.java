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
      _neurons = new BackpropFeedforwardNeuron[hidden.length + 2][];
      BackpropFeedforwardNeuron[] nextLayer = createNeuronArray(outputNodeCount,
         null,
         outputLayerFunction);

      // Output layer
      _neurons[_neurons.length - 1] = nextLayer;
      _outputNeurons = _neurons[_neurons.length - 1];
      
      int edgeCount = 0;

      // Hidden layer(s)
      for (int i = hidden.length - 1; i >= 0; i--) {
         edgeCount += hidden[i] * nextLayer.length;
         nextLayer = createNeuronArray(hidden[i],
            nextLayer,
            hiddenLayerFunction);
         _neurons[i + 1] = nextLayer;
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

   private BackpropFeedforwardNeuron[] createNeuronArray(int size,
      BackpropFeedforwardNeuron[] nextLayer, ActivationFunction function) {

      final BackpropFeedforwardNeuron[] result = new BackpropFeedforwardNeuron[size];
      for (int i = 0; i < size; i++) {
         result[i] = new BackpropFeedforwardNeuron(nextLayer, function, _volatility);
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
         _inputNeurons[i].propagate();
      }

      // Hidden / Output
      for (int i = 1; i < _neurons.length; i++) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].updateOutput();
            _neurons[i][j].propagate();
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
         _outputNeurons[i].calculateGradient(targetOutput[i]);
      }

      // Hidden
      for (int i = _neurons.length - 2; i >= 1; i--) {
         for (int j = 0; j < _neurons[i].length; j++) {
            _neurons[i][j].calculateGradient();
            _neurons[i][j].adjust();
         }
      }

      // Input
      for (int i = 0; i < _inputNeurons.length; i++) {
         _inputNeurons[i].adjust();
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
