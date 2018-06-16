package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.data.RasterData;

// Pure OOP does not seem to make sense for layers, which are 90% algorithm.
@SuppressWarnings("FeatureEnvy")
public class RasterActivationLayer extends RasterLayerBase {

   private final int _count;
   private final ActivationFunction _function;

   private final int _calculationComplexity;
   private final int _adjustmentComplexity;

   /**
    * Creates a layer which applies an activation function to each pixel in its input.
    *
    * @param count    The number of inputs (and outputs).
    * @param width    The width of the input/output.
    * @param height   The height of the input/output.
    * @param depth    The depth of the input/output.
    * @param function The activation function of the layer.
    */
   RasterActivationLayer(final int count, final int width,
         final int height, final int depth, final ActivationFunction function) {

      super(count, width, height, depth,
            count, width, height, depth);
      initializeInputGradientData();

      _count = count;
      _function = function;

      _calculationComplexity = count * width * height * depth;
      _adjustmentComplexity = _calculationComplexity;
   }

   @Override
   public void adjustWithGradient(final RasterData[] outputGradients) {
      for (int i = 0; i < _inputCount; i++) {
         final RasterData inputGradient = _inputGradients[i];
         final RasterData outputGradient = outputGradients[i];
         final RasterData lastInput = _lastInput[i];
         final RasterData currentOutput = _output[i];

         inputGradient.forEach((x, y, z) -> {
            final double slope = _function.getSlope(lastInput.getCell(x, y, z),
                  currentOutput.getCell(x, y, z));

            inputGradient.setCell(x, y, z, outputGradient.getCell(x, y, z) * slope);
         });
      }
   }

   @Override
   public void calculateWithInput(final RasterData[] inputs) {
      for (int i = 0; i < _count; i++) {
         final RasterData input = inputs[i];
         final RasterData output = _output[i];

         output.forEach((x, y, z) ->
               output.setCell(x, y, z, _function.getOutput(input.getCell(x, y, z))));
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
}
