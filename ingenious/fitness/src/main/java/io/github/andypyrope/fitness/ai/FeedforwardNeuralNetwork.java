package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

public class FeedforwardNeuralNetwork {

   private final FeedforwardNeuron[][] _layers;
   private final FeedforwardNeuron[] _inputNeurons;
   private final FeedforwardNeuron[] _outputNeurons;
   private final int _edgeCount;

   public FeedforwardNeuralNetwork(int inputNodeCount, int[] hidden,
      int outputNodeCount, ActivationFunction activationFunction) {

      this(
         inputNodeCount,
         hidden,
         outputNodeCount,
         activationFunction,
         activationFunction);
   }

   private FeedforwardNeuralNetwork(int inputNodeCount, int[] hidden,
         int outputNodeCount, ActivationFunction hiddenLayerFunction,
         ActivationFunction outputLayerFunction) {

      _layers = new RpropFeedforwardNeuron[hidden.length + 2][];
      RpropFeedforwardNeuron[] nextLayer = createLayer(outputNodeCount, null,
         outputLayerFunction);

      // Output layer
      _layers[_layers.length - 1] = nextLayer;
      _outputNeurons = _layers[_layers.length - 1];

      int edgeCount = 0;

      // Hidden layer(s)
      for (int i = hidden.length - 1; i >= 0; i--) {
         edgeCount += hidden[i] * nextLayer.length;
         nextLayer = createLayer(hidden[i],
            nextLayer,
            hiddenLayerFunction);
         _layers[i + 1] = nextLayer;
      }

      // Input layer
      _layers[0] = createLayer(inputNodeCount, nextLayer, null);
      _inputNeurons = _layers[0];
      edgeCount += inputNodeCount * nextLayer.length;
      _edgeCount = edgeCount;
   }

   public int getEdgeCount() {
      return _edgeCount;
   }

   private RpropFeedforwardNeuron[] createLayer(int size,
      RpropFeedforwardNeuron[] nextLayer, ActivationFunction function) {

      final RpropFeedforwardNeuron[] result = new RpropFeedforwardNeuron[size];
      for (int i = 0; i < size; i++) {
         result[i] = new RpropFeedforwardNeuron(function, nextLayer);
      }
      return result;
   }

   public void calculate(double[] inputValues) {
      // All
      for (final FeedforwardNeuron[] layer : _layers) {
         for (FeedforwardNeuron neuron : layer) {
            neuron.resetNetInput();
         }
      }

      // Input
      for (int i = 0; i < _inputNeurons.length; i++) {
         _inputNeurons[i].setOutput(inputValues[i]);
         _inputNeurons[i].propagate();
      }

      // Hidden / Output
      for (int i = 1; i < _layers.length; i++) {
         for (FeedforwardNeuron neuron : _layers[i]) {
            neuron.updateOutput();
            neuron.propagate();
         }
      }
   }

   /**
    * Runs the backpropagation algorithm to improve the weights/biases.
    * 
    * @param targetOutput The expected output
    */
   public void adjust(double[] targetOutput) {
      // Output
      for (int i = 0; i < _outputNeurons.length; i++) {
         _outputNeurons[i].calculateGradient(targetOutput[i]);
         _outputNeurons[i].adjust();
      }

      // Hidden
      for (int i = _layers.length - 2; i >= 1; i--) {
         for (FeedforwardNeuron neuron : _layers[i]) {
            neuron.calculateGradient();
            neuron.adjust();
         }
      }

      // Input
      for (FeedforwardNeuron neuron : _inputNeurons) {
         neuron.adjust();
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
   public double getEuclideanDistance(double[] targetOutput) {
      double totalError = 0.0;
      for (int i = 0; i < _outputNeurons.length; i++) {
         final double output = _outputNeurons[i].getOutput();
         totalError += (output - targetOutput[i]) * (output - targetOutput[i]);
      }
      return Math.sqrt(totalError);
   }
}
