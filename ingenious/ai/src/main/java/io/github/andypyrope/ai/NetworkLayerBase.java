package io.github.andypyrope.ai;

import io.github.andypyrope.ai.util.Vector;

public abstract class NetworkLayerBase implements NetworkLayer {

   protected static final double AVERAGE_RANDOM_DOUBLE = 0.5;
   protected static final double RPROP_LOWER_VOLATILITY_MULTIPLIER = 0.5;
   protected static final double RPROP_HIGHER_VOLATILITY_MULTIPLIER = 1.1;
   protected static final double RPROP_INITIAL_VOLATILITY = 0.1;

   protected final int _inputCount;
   protected final Vector _inputSize;
   protected final int _outputCount;
   protected final Vector _outputSize;
   protected NetworkLayer _nextLayer;
   protected NetworkLayer _previousLayer;
   protected boolean _hasNoCalculation = true;
   protected boolean _hasNoAdjustment = true;

   public NetworkLayerBase(final int inputCount, final Vector inputSize,
         final int outputCount, final Vector outputSize) {

      inputSize.validateAsSize();
      outputSize.validateAsSize();

      _inputCount = inputCount;
      _inputSize = inputSize;
      _outputCount = outputCount;
      _outputSize = outputSize;
   }

   @Override
   public int getInputCount() {
      return _inputCount;
   }

   @Override
   public Vector getInputSize() {
      return _inputSize;
   }

   @Override
   public int getOutputCount() {
      return _outputCount;
   }

   @Override
   public Vector getOutputSize() {
      return _outputSize;
   }

   @Override
   public void setSurroundingLayers(final NetworkLayer previousLayer,
         final NetworkLayer nextLayer) throws InvalidSizeException {

      if (previousLayer != null) {
         previousLayer.validateSize(this);
      }

      if (nextLayer != null) {
         validateSize(nextLayer);
      }

      _previousLayer = previousLayer;
      _nextLayer = nextLayer;
   }

   @Override
   public void validateSize(final NetworkLayer nextLayer) throws InvalidSizeException {
      if (getOutputCount() != nextLayer.getInputCount()) {
         throw new InvalidSizeException(String.format(
               "The next layer should have an input count of %d but it has one of %d",
               getInputCount(),
               nextLayer.getInputCount()));
      }

      if (getOutputSize().differsFrom(nextLayer.getInputSize())) {
         throw new InvalidSizeException(getOutputSize(), nextLayer.getInputSize());
      }
   }
}
