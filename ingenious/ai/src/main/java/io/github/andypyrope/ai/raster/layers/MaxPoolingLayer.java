package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.util.CoordinateConsumer;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;

import java.util.Arrays;

public class MaxPoolingLayer extends RasterLayerBase {

   private static final Vector PADDING = StandardVector.ZERO;
   private static final Vector STRIDE = StandardVector.UNIT;

   private final Vector _windowSize;
   private final Vector[][][][] _bestPositions;

   private final int _calculationComplexity;
   private final int _adjustmentComplexity;

   /**
    * Creates a convolutional layer with a set input and filter size. Its output size is
    * deduced from the target filter size. If the filter size exceeds its maximum, it is
    * shrunk down to a size that fits the layer.
    *
    * @param inputCount The number of inputs.
    * @param inputSize  The size (width, height, depth) of the input.
    * @param windowSize The size of each filter. May be cut down if the resulting output
    *                   size is below 1.
    */
   MaxPoolingLayer(final int inputCount, final Vector inputSize,
         final Vector windowSize) {

      super(inputCount, inputSize,
            inputCount, inputSize.getScanSize(windowSize, PADDING, STRIDE));
      initializeInputGradientData();

      _windowSize = windowSize;
      _windowSize.validateAsSize();

      final int outputWidth = (int) _outputSize.getX();
      final int outputHeight = (int) _outputSize.getY();
      final int outputDepth = (int) _outputSize.getZ();
      _bestPositions = new Vector[_outputCount][outputDepth][outputHeight][outputWidth];

      _calculationComplexity = _outputCount * _outputSize.getPixelCount() *
            _windowSize.getPixelCount();
      _adjustmentComplexity = _inputSize.getPixelCount() +
            _outputCount * _outputSize.getPixelCount();
   }

   @Override
   public void adjustWithGradient(final RasterData[] outputGradients) {
      Arrays.stream(_inputGradients).forEach(RasterData::clear);

      for (int i = 0; i < _inputCount; i++) {
         final Vector[][][] bestPositions = _bestPositions[i];
         final RasterData inputGradient = _inputGradients[i];
         final RasterData outputGradient = outputGradients[i];
         _inputSize.slideWindow(_windowSize, PADDING, STRIDE, (inPos, yX, yY, yZ) ->
               inputGradient.addTo(bestPositions[yZ][yY][yX],
                     outputGradient.getCell(bestPositions[yZ][yY][yX])));
      }
   }

   @Override
   public void calculateWithInput(final RasterData[] inputs) {
      for (int i = 0; i < _inputCount; i++) {
         final Vector[][][] bestPositions = _bestPositions[i];
         final RasterData input = inputs[i];
         final RasterData output = _output[i];
         _inputSize.slideWindow(_windowSize, PADDING, STRIDE, (inPos, yX, yY, yZ) -> {
            final CoordinateConsumer maxGetter = new MaxValueConsumer(input, inPos);
            _windowSize.forEach(maxGetter);
            output.setCell(yX, yY, yZ, ((MaxValueConsumer) maxGetter)._maxValue);
            bestPositions[yZ][yY][yX] = ((MaxValueConsumer) maxGetter)._maxPosition;
         });
      }
   }

   @Override
   public int getCalculationComplexity() {
      return _calculationComplexity;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _adjustmentComplexity;
   }

   /**
    * Finds the max value and position of that max value after a traversal with a window.
    */
   private static class MaxValueConsumer implements CoordinateConsumer {
      private final Vector _pivot;
      private final RasterData _input;
      private double _maxValue;
      private Vector _maxPosition;

      /**
       * @param input The input data.
       * @param pivot The position in the input data where the (0,0,0) cell of the window
       *              is.
       */
      MaxValueConsumer(final RasterData input, final Vector pivot) {
         _input = input;
         _pivot = pivot;
         _maxValue = 0.0;
         _maxPosition = _pivot;
      }

      @Override
      public void accept(final int x, final int y, final int z) {
         final Vector currentPosition = _pivot.plus(x, y, z);
         final double currentValue = _input.getCell(currentPosition);
         if (currentValue > _maxValue) {
            _maxValue = currentValue;
            _maxPosition = currentPosition;
         }
      }
   }
}
