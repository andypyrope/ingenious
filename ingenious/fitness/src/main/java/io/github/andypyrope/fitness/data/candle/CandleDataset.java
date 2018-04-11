package io.github.andypyrope.fitness.data.candle;

import java.time.temporal.TemporalUnit;

public interface CandleDataset {

   /**
    * @return The candles in this dataset.
    */
   Candle[] getData();

   /**
    * @return The candles in this dataset with equal distance between each other. New
    *       candles may be created and some may be omitted.
    */
   Candle[] getSmoothData(long distance, TemporalUnit distanceUnit);
}
