package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.data.RasterData;

public interface RasterLayer extends NetworkLayer {

   /**
    * @param input The input to calculate the output with.
    * @throws InvalidSizeException If the given input array is not with the expected size.
    */
   void calculate(RasterData[] input) throws InvalidSizeException;

   /**
    * @param targetOutput The array that contains the target raster data. This array is
    *                     used for learning and is not modified.
    * @throws InvalidSizeException If the target output is not with the expected size.
    */
   void adjust(RasterData[] targetOutput) throws InvalidSizeException;
}
