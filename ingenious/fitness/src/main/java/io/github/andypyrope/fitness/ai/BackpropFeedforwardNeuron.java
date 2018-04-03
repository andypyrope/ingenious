package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

/**
 * The neuron that operates in accordance with the standard back propagation
 * algorithm in its simplest form.
 */
class BackpropFeedforwardNeuron extends FeedforwardNeuronBase {

   private double _inputGradient;
   private double _outputGradient;
   private final double[] _edgeGradients;
   private final BackpropFeedforwardNeuron[] _nextLayer;

   private final double _volatility;

   BackpropFeedforwardNeuron(BackpropFeedforwardNeuron[] nextLayer,
      ActivationFunction function, double volatility) {

      super(function, nextLayer);

      _nextLayer = nextLayer;
      _edgeGradients = nextLayer == null ? null : new double[nextLayer.length];

      _volatility = volatility;
   }

   /**
    * (non-Javadoc)
    * 
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#calculateGradient()
    */
   @Override
   public void calculateGradient() {
      _outputGradient = 0.0;
      for (int i = 0; i < _edges.length; i++) {
         _edgeGradients[i] = _output * _nextLayer[i]._inputGradient;
         _outputGradient += _edges[i] * _nextLayer[i]._inputGradient;
      }
      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;
   }

   /**
    * (non-Javadoc)
    * 
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#calculateGradient(double)
    */
   @Override
   public void calculateGradient(double targetOutput) {
      _outputGradient = _output - targetOutput;
      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;
   }

   /**
    * (non-Javadoc)
    * 
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#adjust()
    */
   @Override
   public void adjust() {
      for (int i = 0; i < _edges.length; i++) {
         _edges[i] -= _edgeGradients[i] * _volatility;
      }

      final double biasDifferential = _inputGradient * _bias;
      _bias -= biasDifferential * _volatility;
   }
}
