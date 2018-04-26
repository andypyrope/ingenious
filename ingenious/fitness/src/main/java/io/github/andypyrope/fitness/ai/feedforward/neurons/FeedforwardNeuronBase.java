package io.github.andypyrope.fitness.ai.feedforward.neurons;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

public abstract class FeedforwardNeuronBase implements FeedforwardNeuron {

   private static final double INITIAL_BIAS = 0.0;
   private static final double INITIAL_EDGE_WEIGHT = 1.0;

   final int _edgeCount;

   final FeedforwardNeuron[] _nextLayer;
   double _inputGradient;
   final double[] _edges;
   final ActivationFunction _function;
   double _outputGradient;
   double _bias;
   double _netInput;
   double _output;

   FeedforwardNeuronBase(FeedforwardNeuron[] nextLayer, ActivationFunction function) {
      _bias = INITIAL_BIAS;
      _function = function;
      _nextLayer = nextLayer == null ? new FeedforwardNeuron[0] : nextLayer;

      _edgeCount = _nextLayer.length;

      _edges = new double[_edgeCount];
      for (int i = 0; i < _edgeCount; i++) {
         _edges[i] = INITIAL_EDGE_WEIGHT;
      }
   }

   /**
    * (non-Javadoc)
    *
    * @see FeedforwardNeuron#setNetInput(double)
    */
   @Override
   public void setNetInput(final double netInput) {
      _netInput = netInput;
   }

   /**
    * (non-Javadoc)
    *
    * @see FeedforwardNeuron#getOutput()
    */
   @Override
   public double getOutput() {
      return _output;
   }

   /**
    * (non-Javadoc)
    *
    * @see FeedforwardNeuron#propagate()
    */
   @Override
   public void propagate() {
      _output = _function.getOutput(_netInput + _bias);

      for (int i = 0; i < _edgeCount; i++) {
         _nextLayer[i].addInto(_output * _edges[i]);
      }
   }

   @Override
   public void addInto(double amount) {
      _netInput += amount;
   }

   @Override
   public double getInputGradient() {
      return _inputGradient;
   }
}