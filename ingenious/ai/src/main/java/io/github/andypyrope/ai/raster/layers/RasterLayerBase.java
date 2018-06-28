package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.*;
import io.github.andypyrope.ai.data.AtomicRasterData;
import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;

abstract class RasterLayerBase extends NetworkLayerBase implements RasterLayer {

   private static final Vector ATOMIC_SIZE = StandardVector.UNIT;

   RasterData[] _inputGradients;
   final RasterData[] _output;
   RasterData[] _lastInput;

   RasterLayerBase(final int inputCount, final Vector inputSize,
         final int outputCount, final Vector outputSize) {

      super(inputCount, inputSize, outputCount, outputSize);

      _inputGradients = new RasterData[_inputCount];
      _output = new RasterData[_outputCount];
      for (int i = 0; i < _outputCount; i++) {
         _output[i] = new CustomRasterData(_outputSize);
      }
   }

   void initializeInputGradientData() {
      for (int i = 0; i < _inputCount; i++) {
         _inputGradients[i] = new CustomRasterData(_inputSize);
      }
   }

   @Override
   public void calculate() throws InvalidOperationException {
      if (_previousLayer == null) {
         throw new InvalidOperationException(
               "There is no previous layer to get the output of");
      }
      _lastInput = _previousLayer.getOutputAsRaster();
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

      adjustWithGradient(_nextLayer.getInputGradientAsRaster());
      _hasNoAdjustment = false;
   }

   @Override
   public double[] getOutputAsAtomic() throws NoCalculationException,
         InvalidSizeException {
      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }
      if (_outputSize.differsFrom(ATOMIC_SIZE)) {
         throw new InvalidSizeException(ATOMIC_SIZE, _outputSize);
      }
      return AtomicRasterData.castToAtomic(_output);
   }

   @Override
   public double[] getInputGradientAsAtomic()
         throws NoAdjustmentException, InvalidSizeException {

      if (_hasNoAdjustment) {
         throw new NoAdjustmentException();
      }
      if (_inputSize.differsFrom(ATOMIC_SIZE)) {
         throw new InvalidSizeException(ATOMIC_SIZE, _inputSize);
      }
      return AtomicRasterData.castToAtomic(_inputGradients);
   }

   @Override
   public RasterData[] getOutputAsRaster() throws NoCalculationException {
      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }
      return _output;
   }

   @Override
   public RasterData[] getInputGradientAsRaster() throws NoAdjustmentException {
      if (_hasNoAdjustment) {
         throw new NoAdjustmentException();
      }
      return _inputGradients;
   }

   @Override
   public void calculate(RasterData[] input) throws InvalidSizeException {
      if (input.length != _inputCount) {
         throw new InvalidSizeException(String.format("Expected an input " +
                     "array of size %d but got an array of size %d instead",
               _inputCount, input.length));
      }

      for (final RasterData currentInput : input) {
         currentInput.verifyDimensions(_inputSize);
      }

      _lastInput = input;
      calculateWithInput(input);
      _hasNoCalculation = false;
   }

   @Override
   public void adjust(RasterData[] targetOutput)
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

      final RasterData[] outputGradient = new RasterData[_outputCount];
      for (int i = 0; i < _outputCount; i++) {
         outputGradient[i] = getDifference(_output[i], targetOutput[i]);
      }
      adjustWithGradient(outputGradient);
      _hasNoAdjustment = false;
   }

   private RasterData getDifference(final RasterData first, final RasterData second) {
      final RasterData result = new CustomRasterData(first.getSize());

      result.forEach((x, y, z) -> result.setCell(x, y, z,
            first.getCell(x, y, z) - second.getCell(x, y, z)));
      return result;
   }

   abstract void adjustWithGradient(RasterData[] gradient);

   abstract void calculateWithInput(RasterData[] input);
}
