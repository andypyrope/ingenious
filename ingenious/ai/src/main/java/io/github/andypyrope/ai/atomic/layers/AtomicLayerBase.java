package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.*;
import io.github.andypyrope.ai.atomic.AtomicLayer;
import io.github.andypyrope.ai.data.AtomicRasterData;
import io.github.andypyrope.ai.data.RasterData;

abstract class AtomicLayerBase implements AtomicLayer {

   static final double AVERAGE_RANDOM_DOUBLE = 0.5;
   static final double RPROP_LOWER_VOLATILITY_MULTIPLIER = 0.5;
   static final double RPROP_HIGHER_VOLATILITY_MULTIPLIER = 1.1;
   static final double RPROP_INITIAL_VOLATILITY = 0.1;
   final int _inputSize;
   final int _outputSize;
   final double[] _output;
   final double[] _inputGradient;
   double[] _lastInput;
   private boolean _hasNoCalculation = true;
   private boolean _hasNoAdjustment = true;
   private NetworkLayer _nextLayer;
   private NetworkLayer _previousLayer;

   AtomicLayerBase(final int inputSize, final int outputSize) {
      _inputSize = inputSize;
      _outputSize = outputSize;

      _output = new double[_outputSize];
      _inputGradient = new double[_inputSize];
   }

   @Override
   public int getInputSize() {
      return _inputSize;
   }

   @Override
   public int getOutputSize() {
      return _outputSize;
   }

   @Override
   public void calculate() throws InvalidOperationException {
      if (_previousLayer == null) {
         throw new InvalidOperationException(
               "There is no previous layer to get the output of");
      }
      _lastInput = _previousLayer.getOutputAsAtomic();
      calculateWithInput(_lastInput);
      _hasNoCalculation = false;
   }

   @Override
   public void adjust() throws NoCalculationException, InvalidOperationException {
      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }

      if (_nextLayer == null) {
         throw new InvalidOperationException("There is no next layer to adjust with");
      }

      adjustWithGradient(_nextLayer.getInputGradientAsAtomic());
      _hasNoAdjustment = false;
   }

   @Override
   public void setSurroundingLayers(final NetworkLayer previousLayer,
         final NetworkLayer nextLayer) {

      if (previousLayer != null && previousLayer.getOutputSize() != _inputSize) {
         throw new MismatchException(String.format(
               "The previous layer should have an output size of %d, not %d", _inputSize,
               previousLayer.getOutputSize()));
      }
      _previousLayer = previousLayer;

      if (nextLayer != null && nextLayer.getInputSize() != _outputSize) {
         throw new MismatchException(String.format(
               "The next layer should have an input size of %d, not %d", _outputSize,
               nextLayer.getInputSize()));
      }
      _nextLayer = nextLayer;
   }

   @Override
   public double[] getOutputAsAtomic() throws NoCalculationException {
      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }
      return _output;
   }

   @Override
   public double[] getInputGradientAsAtomic() throws NoAdjustmentException {
      if (_hasNoAdjustment) {
         throw new NoAdjustmentException();
      }
      return _inputGradient;
   }

   @Override
   public RasterData[] getOutputAsRaster() throws NoCalculationException {
      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }
      return AtomicRasterData.castToRaster(_output);
   }

   @Override
   public RasterData[] getInputGradientAsRaster() throws NoAdjustmentException {
      if (_hasNoAdjustment) {
         throw new NoAdjustmentException();
      }
      return AtomicRasterData.castToRaster(_inputGradient);
   }

   @Override
   public void calculate(final double[] inputArray) throws MismatchException {
      if (inputArray.length != _inputSize) {
         throw new MismatchException(String.format("Expected an input " +
                     "array of size %d but got an array of size %d instead",
               _inputSize, inputArray.length));
      }

      _lastInput = inputArray;
      calculateWithInput(_lastInput);
      _hasNoCalculation = false;
   }

   @Override
   public double getEuclideanDistance(double[] targetOutput)
         throws NoCalculationException, MismatchException {

      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }

      if (targetOutput.length != _outputSize) {
         throw new MismatchException(String.format("Expected a target " +
                     "output array of size %d but got target output array of " +
                     "size %d instead",
               _outputSize, targetOutput.length));
      }

      double totalError = 0.0;
      for (int i = 0; i < _output.length; i++) {
         final double delta = _output[i] - targetOutput[i];
         totalError += delta * delta;
      }
      return Math.sqrt(totalError);
   }

   @Override
   public void adjust(final double[] targetOutput)
         throws NoCalculationException, MismatchException {

      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }

      if (targetOutput.length != _outputSize) {
         throw new MismatchException(String.format("Expected a target " +
                     "output array of size %d but got target output array of " +
                     "size %d instead",
               _outputSize, targetOutput.length));
      }

      // Assuming the error function is:
      // E = 1/2 * ((y[0] - t[0])^2 + .. + (y[n-1] - t[n-1])^2)
      // where y[i] is the i-th output and t[i] is the i-th target output. Then:
      // dE/dy[i] = 1/2 * ((y[i]^2)' - (2*y[i]*t[i])' + (t[i]^2)')
      // dE/dy[i] = 1/2 * (2*y[i] - 2*1*t[i] + 0)
      // dE/dy[i] = y[i] - t[i]

      final double[] outputGradient = new double[_outputSize];
      for (int i = 0; i < _outputSize; i++) {
         outputGradient[i] = _output[i] - targetOutput[i];
      }
      adjustWithGradient(outputGradient);
      _hasNoAdjustment = false;
   }

   protected abstract void calculateWithInput(final double[] input);

   protected abstract void adjustWithGradient(final double[] outputGradient);
}
