package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;

import java.util.Arrays;
import java.util.Random;

// Pure OOP does not seem to make sense for layers, which are 90% algorithm.
@SuppressWarnings("FeatureEnvy")
public class ConvolutionalLayer extends RasterLayerBase {

   private final int _filterCount;
   private final RasterData[] _filters;
   private final RasterData[] _filterGradients;
   private final RasterData[] _filterVolatility;
   private final RasterData[] _lastFilterGradients;

   private final int _calculationComplexity;
   private final int _adjustmentComplexity;

   /**
    * Creates a convolutional layer with a set input and filter size. Its output size is
    * deduced from the target filter size. If the filter size exceeds its maximum, it is
    * shrunk down to a size that fits the layer.
    *
    * @param inputCount         The number of inputs.
    * @param inputWidth         The width of each input.
    * @param inputHeight        The height of each input.
    * @param inputDepth         The depth of each input.
    * @param filterCount        The number of filters.
    * @param targetFilterWidth  The width of each filter.
    * @param targetFilterHeight The height of each filter.
    * @param targetFilterDepth  The depth of each filter.
    * @param random             The random number generator to use.
    */
   ConvolutionalLayer(final int inputCount, final int inputWidth,
         final int inputHeight, final int inputDepth, final int filterCount,
         final int targetFilterWidth, final int targetFilterHeight,
         final int targetFilterDepth, final Random random) {

      super(inputCount, inputWidth, inputHeight, inputDepth,
            inputCount * filterCount,
            Math.max(inputWidth - targetFilterWidth + 1, 1),
            Math.max(inputHeight - targetFilterHeight + 1, 1),
            Math.max(inputDepth - targetFilterDepth + 1, 1));
      initializeInputGradientData();

      _filterCount = filterCount;
      final int filterWidth = _inputWidth - _outputWidth + 1;
      final int filterHeight = _inputHeight - _outputHeight + 1;
      final int filterDepth = _inputDepth - _outputDepth + 1;

      _filters = new RasterData[_filterCount];
      _filterGradients = new RasterData[_filterCount];
      _lastFilterGradients = new RasterData[_filterCount];
      _filterVolatility = new RasterData[_filterCount];
      for (int i = 0; i < _filterCount; i++) {
         _filters[i] = new CustomRasterData(filterWidth, filterHeight, filterDepth);
         _filters[i].randomize(random);
         _filterGradients[i] = new CustomRasterData(filterWidth, filterHeight,
               filterDepth);
         _lastFilterGradients[i] = new CustomRasterData(filterWidth, filterHeight,
               filterDepth);
         _filterVolatility[i] = new CustomRasterData(filterWidth, filterHeight,
               filterDepth);
         _filterVolatility[i].setAll(RPROP_INITIAL_VOLATILITY);
      }

      _calculationComplexity = _outputCount *
            _outputWidth * filterWidth *
            _outputHeight * filterHeight *
            _outputDepth * filterDepth;
      _adjustmentComplexity = _calculationComplexity * 2;
   }

   @Override
   public void calculateWithInput(final RasterData[] inputs) {
      int index = 0;
      for (final RasterData input : inputs) {
         for (final RasterData filter : _filters) {
            calculateSingle(input, filter, _output[index++]);
         }
      }
   }

   private void calculateSingle(final RasterData input, final RasterData filter,
         final RasterData output) {

      output.forEach((yX, yY, yZ) -> filter.forEach((fX, fY, fZ) ->
            output.setCell(yX, yY, yZ,
                  input.getCell(yX + fX, yY + fY, yZ + fZ) * filter.getCell(fX, fY, fZ))
      ));
   }

   @Override
   public void adjustWithGradient(final RasterData[] outputGradient) {
      Arrays.stream(_inputGradients).forEach(RasterData::clear);
      Arrays.stream(_filterGradients).forEach(RasterData::clear);

      int index = 0;
      for (int i = 0; i < _inputCount; i++) {
         for (int j = 0; j < _filterCount; j++) {
            accumulateGradient(_lastInput[i], _inputGradients[i],
                  _filters[j], _filterGradients[j],
                  outputGradient[index]);
            index++;
         }
      }

      for (int i = 0; i < _filterCount; i++) {
         adjustSingle(_filters[i], _filterGradients[i], _lastFilterGradients[i],
               _filterVolatility[i]);
      }
   }

   private void accumulateGradient(final RasterData input, final RasterData inputGradient,
         final RasterData filter, final RasterData filterGradient,
         final RasterData outputGradient) {

      outputGradient.forEach((yX, yY, yZ) -> {
         final double outputGradientAtCell = outputGradient.getCell(yX, yY, yZ);

         filter.forEach((fX, fY, fZ) -> {
            inputGradient.addTo(yX + fX, yY + fY, yZ + fZ,
                  outputGradientAtCell * filter.getCell(fX, fY, fZ));
            filterGradient.addTo(fX, fY, fZ,
                  outputGradientAtCell * input.getCell(yX + fX, yY + fY, yZ + fZ));
         });
      });
   }

   private void adjustSingle(final RasterData filter, final RasterData gradient,
         final RasterData lastGradient, final RasterData volatility) {

      gradient.forEach((x, y, z) -> {
         final double multipliedGradient = gradient.getCell(x, y, z) *
               lastGradient.getCell(x, y, z);
         final double delta = Math.copySign(volatility.getCell(x, y, z),
               gradient.getCell(x, y, z));

         if (multipliedGradient < 0) {
            filter.addTo(x, y, z, -1 * delta * RPROP_LOWER_VOLATILITY_MULTIPLIER);
            volatility.multiply(x, y, z, RPROP_LOWER_VOLATILITY_MULTIPLIER);
         } else if (multipliedGradient > 0) {
            filter.addTo(x, y, z, -1 * delta);
            volatility.multiply(x, y, z, RPROP_HIGHER_VOLATILITY_MULTIPLIER);
         }

         lastGradient.setCell(x, y, z,
               multipliedGradient < 0 ? 0 : gradient.getCell(x, y, z));
      });
   }

   @Override
   public int getCalculationComplexity() {
      return _calculationComplexity;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _adjustmentComplexity;
   }
}
