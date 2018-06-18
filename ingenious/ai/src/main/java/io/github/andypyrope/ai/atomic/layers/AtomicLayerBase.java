package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.*;
import io.github.andypyrope.ai.atomic.AtomicLayer;
import io.github.andypyrope.ai.data.AtomicRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.util.RasterSize;
import io.github.andypyrope.ai.util.TriRasterSize;

abstract class AtomicLayerBase extends NetworkLayerBase implements AtomicLayer {

   private static final RasterSize ATOMIC_SIZE = new TriRasterSize(1, 1, 1);

   final double[] _output;
   final double[] _inputGradients;
   double[] _lastInput;

   AtomicLayerBase(final int inputCount, final int outputCount) {
      super(inputCount, ATOMIC_SIZE, outputCount, ATOMIC_SIZE);
      _output = new double[_outputCount];
      _inputGradients = new double[_inputCount];
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
   public void calculate(final double[] input) throws InvalidSizeException {
      if (input.length != _inputCount) {
         throw new InvalidSizeException(String.format("Expected an input " +
                     "array of size %d but got an array of size %d instead",
               _inputCount, input.length));
      }

      _lastInput = input;
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
   public double getEuclideanDistance(double[] targetOutput)
         throws NoCalculationException, InvalidSizeException {

      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }

      if (targetOutput.length != _outputCount) {
         throw new InvalidSizeException(String.format("Expected a target " +
                     "output array of size %d but got target output array of " +
                     "size %d instead",
               _outputCount, targetOutput.length));
      }

      double totalError = 0.0;
      for (int i = 0; i < _output.length; i++) {
         final double delta = _output[i] - targetOutput[i];
         totalError += delta * delta;
      }
      return Math.sqrt(totalError);
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
      return _inputGradients;
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
      return AtomicRasterData.castToRaster(_inputGradients);
   }

   @Override
   public void adjust(final double[] targetOutput)
         throws NoCalculationException, InvalidSizeException {

      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }

      if (targetOutput.length != _outputCount) {
         throw new InvalidSizeException(String.format("Expected a target " +
                     "output array of size %d but got target output array of " +
                     "size %d instead",
               _outputCount, targetOutput.length));
      }

      // Assuming the error function is:
      // E = 1/2 * ((y[0] - t[0])^2 + .. + (y[n-1] - t[n-1])^2)
      // where y[i] is the i-th output and t[i] is the i-th target output. Then:
      // dE/dy[i] = 1/2 * ((y[i]^2)' - (2*y[i]*t[i])' + (t[i]^2)')
      // dE/dy[i] = 1/2 * (2*y[i] - 2*1*t[i] + 0)
      // dE/dy[i] = y[i] - t[i]

      final double[] outputGradient = new double[_outputCount];
      for (int i = 0; i < _outputCount; i++) {
         outputGradient[i] = _output[i] - targetOutput[i];
      }
      adjustWithGradient(outputGradient);
      _hasNoAdjustment = false;
   }

   protected abstract void calculateWithInput(final double[] input);

   protected abstract void adjustWithGradient(final double[] outputGradient);
}
