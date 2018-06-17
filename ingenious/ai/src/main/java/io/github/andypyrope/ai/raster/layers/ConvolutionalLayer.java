package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.util.RasterSize;

import java.util.Arrays;
import java.util.Random;

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
    * @param inputCount       The number of inputs.
    * @param inputSize        The size (width, height, depth) of the input.
    * @param filterCount      The number of filters.
    * @param targetFilterSize The size of each filter. May be cut down if the resulting
    *                         output size is below 1.
    * @param random           The random number generator to use.
    */
   ConvolutionalLayer(final int inputCount, final RasterSize inputSize,
         final int filterCount, final RasterSize targetFilterSize, final Random random) {

      super(inputCount, inputSize,
            inputCount * filterCount,
            inputSize.minus(targetFilterSize).plus(1).atLeast(1));
      initializeInputGradientData();

      _filterCount = filterCount;
      final RasterSize filterSize = inputSize.minus(getOutputSize()).plus(1);

      _filters = new RasterData[_filterCount];
      _filterGradients = new RasterData[_filterCount];
      _lastFilterGradients = new RasterData[_filterCount];
      _filterVolatility = new RasterData[_filterCount];
      for (int i = 0; i < _filterCount; i++) {
         _filters[i] = new CustomRasterData(filterSize);
         _filters[i].randomize(random);
         _filterGradients[i] = new CustomRasterData(filterSize);
         _lastFilterGradients[i] = new CustomRasterData(filterSize);
         _filterVolatility[i] = new CustomRasterData(filterSize);
         _filterVolatility[i].setAll(RPROP_INITIAL_VOLATILITY);
      }

      _calculationComplexity = _outputCount * getOutputSize().getPixelCount() *
            filterSize.getPixelCount();
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
