package io.github.andypyrope.ai;

import io.github.andypyrope.ai.data.MismatchException;

public abstract class NetworkLayerBase implements NetworkLayer {

   protected static final double RPROP_LOWER_VOLATILITY_MULTIPLIER = 0.5;
   protected static final double RPROP_HIGHER_VOLATILITY_MULTIPLIER = 1.1;
   protected static final double RPROP_INITIAL_VOLATILITY = 0.1;

   protected final int _inputCount;
   protected final int _outputCount;
   protected NetworkLayer _nextLayer;
   protected NetworkLayer _previousLayer;
   protected boolean _hasNoCalculation = true;
   protected boolean _hasNoAdjustment = true;

   public NetworkLayerBase(final int inputCount, final int outputCount) {
      _inputCount = inputCount;
      _outputCount = outputCount;
   }

   @Override
   public int getInputCount() {
      return _inputCount;
   }

   @Override
   public int getOutputCount() {
      return _outputCount;
   }


   @Override
   public void setSurroundingLayers(final NetworkLayer previousLayer,
         final NetworkLayer nextLayer) throws MismatchException {

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
   public void validateSize(final NetworkLayer nextLayer) throws MismatchException {
      if (getOutputCount() != nextLayer.getInputCount()) {
         throw new MismatchException(String.format(
               "The next layer should have an input count of %d but it has one of %d",
               getInputCount(),
               nextLayer.getInputCount()));
      }

      if (getOutputWidth() != nextLayer.getInputWidth()
            || getOutputHeight() != nextLayer.getInputHeight()
            || getOutputDepth() != nextLayer.getInputDepth()) {

         throw new MismatchException(nextLayer.getInputWidth(),
               nextLayer.getInputHeight(), nextLayer.getInputDepth(),
               getOutputWidth(), getOutputHeight(), getOutputDepth());
      }
   }
}
