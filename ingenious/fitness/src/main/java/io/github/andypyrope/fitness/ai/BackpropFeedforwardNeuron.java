package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

/**
 * The neuron that operates in accordance with the standard back propagation
 * algorithm in its simplest form.
 */
class BackpropFeedforwardNeuron extends FeedforwardNeuronBase {

   private double _inputGradient;
   private double _outputGradient;
   private final BackpropFeedforwardNeuron[] _nextLayer;

   private final double _volatility;

   BackpropFeedforwardNeuron(BackpropFeedforwardNeuron[] nextLayer,
      ActivationFunction function, double volatility) {

      super(function, nextLayer);

      _nextLayer = nextLayer;
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
      if (_edges != null) {
         for (int i = 0; i < _edges.length; i++) {
            final double edgeGradient = _output * _nextLayer[i]._inputGradient;
            _edges[i] -= edgeGradient * _volatility;
         }
      }

      final double biasGradient = _inputGradient * _bias;
      _bias -= biasGradient * _volatility;
   }
}
