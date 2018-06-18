package io.github.andypyrope.ai;

import io.github.andypyrope.ai.util.RasterSize;

public abstract class NetworkLayerBase implements NetworkLayer {

   protected static final double AVERAGE_RANDOM_DOUBLE = 0.5;
   protected static final double RPROP_LOWER_VOLATILITY_MULTIPLIER = 0.5;
   protected static final double RPROP_HIGHER_VOLATILITY_MULTIPLIER = 1.1;
   protected static final double RPROP_INITIAL_VOLATILITY = 0.1;

   protected final int _inputCount;
   protected final RasterSize _inputSize;
   protected final int _outputCount;
   protected final RasterSize _outputSize;
   protected NetworkLayer _nextLayer;
   protected NetworkLayer _previousLayer;
   protected boolean _hasNoCalculation = true;
   protected boolean _hasNoAdjustment = true;

   public NetworkLayerBase(final int inputCount, final RasterSize inputSize,
         final int outputCount, final RasterSize outputSize) {

      if (inputSize.isInvalid()) {
         throw new InvalidSizeException(inputSize);
      }

      if (outputSize.isInvalid()) {
         throw new InvalidSizeException(outputSize);
      }

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
   public RasterSize getInputSize() {
      return _inputSize;
   }

   @Override
   public int getOutputCount() {
      return _outputCount;
   }

   @Override
   public RasterSize getOutputSize() {
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
