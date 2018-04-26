package io.github.andypyrope.fitness.ai.feedforward;

import io.github.andypyrope.fitness.ai.feedforward.neurons.FeedforwardNeuron;
import io.github.andypyrope.fitness.ai.feedforward.neurons.FeedforwardNeuronFactory;

public class StandardFeedforwardNeuralNetwork implements FeedforwardNeuralNetwork {

   private final FeedforwardNeuron[][] _layers;
   private final FeedforwardNeuron[] _outputLayer;
   private final int _edgeCount;

   public StandardFeedforwardNeuralNetwork(int inputSize, int[] hidden,
         int outputSize, FeedforwardNeuronFactory neuronFactory) {

      _layers = neuronFactory.makeAllNeurons(inputSize, hidden, outputSize);
      _outputLayer = _layers[_layers.length - 1];

      int edgeCount = 0;
      for (int i = 1; i < _layers.length; i++) {
         edgeCount += _layers[i - 1].length * _layers[i].length;
      }
      _edgeCount = edgeCount;
   }

   public int getEdgeCount() {
      return _edgeCount;
   }

   public void calculate(double[] inputValues) {
      for (int i = 0; i < _layers.length; i++) {
         for (int j = 0; j < _layers[i].length; j++) {
            _layers[i][j].setNetInput(i == 0 ? inputValues[j] : 0.0);
         }
      }

      for (final FeedforwardNeuron[] layer : _layers) {
         for (final FeedforwardNeuron neuron : layer) {
            neuron.propagate();
         }
      }
   }

   public void adjust(double[] targetOutput) {
      for (int i = 0; i < _outputLayer.length; i++) {
         _outputLayer[i].adjust(targetOutput[i]);
      }

      for (int i = _layers.length - 2; i >= 0; i--) {
         for (final FeedforwardNeuron neuron : _layers[i]) {
            neuron.adjust();
         }
      }
   }

   public double[] getOutput() {
      final double[] output = new double[_outputLayer.length];
      for (int i = 0; i < _outputLayer.length; i++) {
         output[i] = _outputLayer[i].getOutput();
      }
      return output;
   }

   public double getEuclideanDistance(double[] targetOutput) {
      double totalError = 0.0;
      for (int i = 0; i < _outputLayer.length; i++) {
         final double delta = _outputLayer[i].getOutput() - targetOutput[i];
         totalError += delta * delta;
      }
      return Math.sqrt(totalError);
   }
}
