package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

class FeedforwardNeuron {

   private static final double DEFAULT_BIAS = 0.0;
   private static final double DEFAULT_EDGE_WEIGHT = 1.0;

   private double _bias;
   private final double[] _edges;
   private final FeedforwardNeuron[] _nextLayer;

   private double _netInput;
   private double _output;

   private double _outputDifferential;
   private double _inputDifferential;
   private final double[] _edgeDifferentials;

   private final ActivationFunction _function;

   FeedforwardNeuron(FeedforwardNeuron[] nextLayer,
      ActivationFunction function) {

      _bias = DEFAULT_BIAS;
      _function = function;

      _nextLayer = nextLayer;
      if (nextLayer == null) {
         _edges = null;
         _edgeDifferentials = null;
      } else {
         _edges = new double[nextLayer.length];
         _edgeDifferentials = new double[nextLayer.length];

         for (int i = 0; i < _edges.length; i++) {
            _edges[i] = DEFAULT_EDGE_WEIGHT;
         }
      }
   }

   void resetNetInput() {
      _netInput = 0.0;
   }

   void setOutput(double output) {
      _output = output;
   }

   void updateOutput() {
      _output = _function.getOutput(_netInput + _bias);
   }

   public void feedIntoNextLayer() {
      if (_nextLayer == null) {
         return;
      }

      for (int i = 0; i < _nextLayer.length; i++) {
         _nextLayer[i]._netInput += _output * _edges[i];
      }
   }

   public void calculateDifferentials() {
      _outputDifferential = 0.0;
      for (int i = 0; i < _edges.length; i++) {
         _edgeDifferentials[i] = _output * _nextLayer[i]._inputDifferential;
         _outputDifferential += _edges[i] * _nextLayer[i]._inputDifferential;
      }
      _inputDifferential = _function.getSlope(_netInput, _output) *
               _outputDifferential;
   }

   /**
    * This should be called only for output nodes
    * 
    * @param targetOutput
    */
   public void calculateDifferentials(double targetOutput) {
      _outputDifferential = _output - targetOutput;
      _inputDifferential = _function.getSlope(_netInput, _output) *
               _outputDifferential;
   }

   public void adjustWeights(double volatility) {
      for (int i = 0; i < _edges.length; i++) {
         _edges[i] -= _edgeDifferentials[i] * volatility;
      }

      final double biasDifferential = _inputDifferential * _bias;
      _bias -= biasDifferential * volatility;
   }

   public double getOutput() {
      return _output;
   }
}
