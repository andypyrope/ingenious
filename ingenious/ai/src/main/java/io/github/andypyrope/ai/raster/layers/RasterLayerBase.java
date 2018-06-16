package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.InvalidOperationException;
import io.github.andypyrope.ai.NetworkLayerBase;
import io.github.andypyrope.ai.NoAdjustmentException;
import io.github.andypyrope.ai.NoCalculationException;
import io.github.andypyrope.ai.data.AtomicRasterData;
import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.MismatchException;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;

@SuppressWarnings("FeatureEnvy")
abstract class RasterLayerBase extends NetworkLayerBase implements RasterLayer {

   final int _inputWidth;
   final int _inputHeight;
   final int _inputDepth;
   final RasterData[] _inputGradient;
   final int _outputWidth;
   final int _outputHeight;
   final int _outputDepth;
   final RasterData[] _output;
   RasterData[] _lastInput;


   RasterLayerBase(final int inputCount, final int inputWidth, final int inputHeight,
         final int inputDepth, final int outputCount, final int outputWidth,
         final int outputHeight, final int outputDepth) {

      super(inputCount, outputCount);

      _inputWidth = inputWidth;
      _inputHeight = inputHeight;
      _inputDepth = inputDepth;
      _inputGradient = new RasterData[_inputCount];
      for (int i = 0; i < _inputCount; i++) {
         _inputGradient[i] = new CustomRasterData(_inputWidth, _inputHeight, _inputDepth);
      }

      _outputWidth = outputWidth;
      _outputHeight = outputHeight;
      _outputDepth = outputDepth;
      _output = new RasterData[_outputCount];
      for (int i = 0; i < _outputCount; i++) {
         _output[i] = new CustomRasterData(_outputWidth, _outputHeight, _outputDepth);
      }
   }

   @Override
   public int getInputWidth() {
      return _inputWidth;
   }

   @Override
   public int getInputHeight() {
      return _inputHeight;
   }

   @Override
   public int getInputDepth() {
      return _inputDepth;
   }

   @Override
   public int getOutputWidth() {
      return _outputWidth;
   }

   @Override
   public int getOutputHeight() {
      return _outputHeight;
   }

   @Override
   public int getOutputDepth() {
      return _outputDepth;
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
   public double[] getOutputAsAtomic() throws NoCalculationException, MismatchException {
      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }
      if (_outputWidth > 1 || _outputHeight > 1 || _outputDepth > 1) {
         throw new MismatchException(1, 1, 1, _outputWidth, _outputHeight, _outputDepth);
      }
      return AtomicRasterData.castToAtomic(_output);
   }

   @Override
   public double[] getInputGradientAsAtomic()
         throws NoAdjustmentException, MismatchException {

      if (_hasNoAdjustment) {
         throw new NoAdjustmentException();
      }
      if (_inputWidth > 1 || _inputHeight > 1 || _inputDepth > 1) {
         throw new MismatchException(1, 1, 1, _inputWidth, _inputHeight, _inputDepth);
      }
      return AtomicRasterData.castToAtomic(_inputGradient);
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
      return _inputGradient;
   }

   @Override
   public void calculate(RasterData[] input) throws MismatchException {
      if (input.length != _inputCount) {
         throw new MismatchException(String.format("Expected an input " +
                     "array of size %d but got an array of size %d instead",
               _inputCount, input.length));
      }

      for (final RasterData currentInput : input) {
         currentInput.verifyDimensions(_inputWidth, _inputHeight, _inputDepth);
      }

      _lastInput = input;
      calculateWithInput(input);
      _hasNoCalculation = false;
   }

   @Override
   public void adjust(RasterData[] targetOutput)
         throws NoCalculationException, MismatchException {

      if (_hasNoCalculation) {
         throw new NoCalculationException();
      }

      if (targetOutput.length != _outputCount) {
         throw new MismatchException(String.format("Expected a target " +
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
      final RasterData result = new CustomRasterData(first.getWidth(), first.getHeight(),
            first.getDepth());

      result.forEach((x, y, z) -> result.setCell(x, y, z,
            first.getCell(x, y, z) - second.getCell(x, y, z)));
      return result;
   }

   abstract void adjustWithGradient(RasterData[] gradient);

   abstract void calculateWithInput(RasterData[] input);
}
