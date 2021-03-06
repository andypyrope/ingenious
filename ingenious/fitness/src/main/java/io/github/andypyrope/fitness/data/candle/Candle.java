package io.github.andypyrope.fitness.data.candle;

import java.time.LocalDateTime;

public interface Candle {

   /**
    * @return The time at which the lifespan of the candle has begun
    */
   LocalDateTime getTime();

   /**
    * @return The highest price during the lifespan of the candle
    */
   double getHighestPrice();

   /**
    * @return The price at which the lifespan of the candle has begun
    */
   double getOpeningPrice();

   /**
    * @return The lowest price during the lifespan of the candle
    */
   double getLowestPrice();

   /**
    * @return The price at which the lifespan of the candle has ended
    */
   double getClosingPrice();

}