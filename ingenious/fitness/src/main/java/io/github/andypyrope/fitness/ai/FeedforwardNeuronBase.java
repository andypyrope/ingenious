package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

public abstract class FeedforwardNeuronBase implements FeedforwardNeuron {

   private static final double INITIAL_BIAS = 0.0;
   private static final double INITIAL_EDGE_WEIGHT = 1.0;

   final double[] _edges;
   final ActivationFunction _function;
   private final FeedforwardNeuronBase[] _nextLayer;
   double _bias;
   double _netInput;
   double _output;

   FeedforwardNeuronBase(ActivationFunction function,
         FeedforwardNeuronBase[] nextLayer) {

      _bias = INITIAL_BIAS;
      _function = function;
      _nextLayer = nextLayer;

      _edges = nextLayer == null ? null : new double[nextLayer.length];
      if (_edges != null) {
         for (int i = 0; i < _edges.length; i++) {
            _edges[i] = INITIAL_EDGE_WEIGHT;
         }
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
    * Update the output of the node based on its net input, bias and activation
    * function.
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
      if (_nextLayer == null) {
         return;
      }

      for (int i = 0; i < _nextLayer.length; i++) {
         _nextLayer[i]._netInput += _output * _edges[i];
      }
   }

   /**
    * (non-Javadoc)
    * 
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#calculateGradient()
    */
   @Override
   abstract public void calculateGradient();

   /**
    * (non-Javadoc)
    * 
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#calculateGradient(
    *      double)
    */
   @Override
   abstract public void calculateGradient(double targetOutput);

   /**
    * (non-Javadoc)
    * 
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#adjust()
    */
   @Override
   abstract public void adjust();
}