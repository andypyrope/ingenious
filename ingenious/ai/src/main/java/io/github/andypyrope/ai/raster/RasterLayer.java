package io.github.andypyrope.ai.raster;

import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.data.MismatchException;
import io.github.andypyrope.ai.data.RasterData;

public interface RasterLayer extends NetworkLayer {

   /**
    * @param input The input to calculate the output with.
    * @throws MismatchException If the given input array is not with the expected size.
    */
   void calculate(RasterData[] input) throws MismatchException;

   /**
    * @param targetOutput The array that contains the target raster data. This array is
    *                     used for learning and is not modified.
    * @throws MismatchException If the target output is not with the expected size.
    */
   void adjust(RasterData[] targetOutput) throws MismatchException;
}
