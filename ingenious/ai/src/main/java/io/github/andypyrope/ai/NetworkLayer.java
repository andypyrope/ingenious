package io.github.andypyrope.ai;

import io.github.andypyrope.ai.data.MismatchException;
import io.github.andypyrope.ai.data.RasterData;

/**
 * A layer in a neural network.
 */
public interface NetworkLayer {

   /**
    * @return An approximation of the number of computations needed for a pass through
    *       this layer.
    */
   int getCalculationComplexity();

   /**
    * @return An approximation of the number of computations needed for a reverse pass
    *       (for learning) through this layer.
    */
   int getAdjustmentComplexity();

   /**
    * @return The number of inputs of the layer
    */
   int getInputCount();

   /**
    * @return The width of each raster instance this layer accepts.
    */
   int getInputWidth();

   /**
    * @return The height of each raster instance this layer accepts.
    */
   int getInputHeight();

   /**
    * @return The depth of each raster instance this layer accepts.
    */
   int getInputDepth();

   /**
    * @return The number of outputs of the layer
    */
   int getOutputCount();

   /**
    * @return The width of each raster instance this layer produces.
    */
   int getOutputWidth();

   /**
    * @return The height of each raster instance this layer produces.
    */
   int getOutputHeight();

   /**
    * @return The depth of each raster instance this layer produces.
    */
   int getOutputDepth();

   /**
    * Based on the net input, update the net output of this layer.
    */
   void calculate();

   /**
    * Based on the input slope of the next layer, adjust the edges, biases and/or whatever
    * other learnable parameters this layer has.
    *
    * @throws NoCalculationException    If no calculation has been made yet.
    * @throws InvalidOperationException If there is no next layer.
    */
   void adjust() throws NoCalculationException, InvalidOperationException;

   /**
    * @param previousLayer The layer before this one, from which the output is received.
    * @param nextLayer     The layer after this one, from which the input slope is
    *                      received.
    * @throws MismatchException If the surrounding layers have input/output sizes that do
    *                           not match of this layer.
    */
   void setSurroundingLayers(NetworkLayer previousLayer, NetworkLayer nextLayer)
         throws MismatchException;

   /**
    * @return The output of this layer as atomic data.
    * @throws NoCalculationException    If no calculation has been made yet.
    * @throws InvalidOperationException If the output of this layer is not atomic.
    */
   double[] getOutputAsAtomic() throws NoCalculationException, InvalidOperationException;

   /**
    * @return The slope of the input of this layer as atomic data.
    * @throws NoAdjustmentException     If no adjustment has been made yet.
    * @throws InvalidOperationException If the input of this layer is not atomic.
    */
   double[] getInputGradientAsAtomic()
         throws NoAdjustmentException, InvalidOperationException;

   /**
    * @return The output of this layer as raster data.
    * @throws NoCalculationException If no calculation has been made yet.
    */
   RasterData[] getOutputAsRaster() throws NoCalculationException;

   /**
    * @return The output of this layer as raster data.
    * @throws NoAdjustmentException If no adjustment has been made yet.
    */
   RasterData[] getInputGradientAsRaster() throws NoAdjustmentException;

   /**
    * Throws a {@link MismatchException} if the output count/dimensions of this layer do
    * not match the input count and dimensions of the next layer.
    *
    * @param nextLayer The next layer.
    * @throws MismatchException If any of the dimensions do not match.
    */
   void validateSize(NetworkLayer nextLayer) throws MismatchException;
}