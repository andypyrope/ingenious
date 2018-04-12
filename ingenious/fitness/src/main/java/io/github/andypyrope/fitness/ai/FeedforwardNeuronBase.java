package io.github.andypyrope.fitness.ai;

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

   FeedforwardNeuronBase(ActivationFunction function, FeedforwardNeuron[] nextLayer) {
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
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#resetNetInput()
    */
   @Override
   public void resetNetInput() {
      _netInput = 0.0;
   }

   /**
    * (non-Javadoc)
    *
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#setOutput(double)
    */
   @Override
   public void setOutput(double output) {
      _output = output;
   }

   /**
    * (non-Javadoc)
    *
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#getOutput()
    */
   @Override
   public double getOutput() {
      return _output;
   }

   /**
    * Update the output of the node based on its net input, bias and activation function.
    */
   @Override
   public void updateOutput() {
      _output = _function.getOutput(_netInput + _bias);
   }

   /**
    * (non-Javadoc)
    *
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#propagate()
    */
   @Override
   public void propagate() {
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