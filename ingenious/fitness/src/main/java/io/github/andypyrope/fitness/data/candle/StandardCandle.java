package io.github.andypyrope.fitness.data.candle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StandardCandle implements Candle {
   private final LocalDateTime _time;

   private final double _openingPrice;
   private final double _highestPrice;
   private final double _lowestPrice;
   private final double _closingPrice;

   StandardCandle(String time, String date, String openingPrice, String highestPrice,
         String lowestPrice, String closingPrice) {

      nullCheck(closingPrice, "closing price");
      nullCheck(date, "date");

      _closingPrice = Double.parseDouble(closingPrice);
      _openingPrice = openingPrice == null || openingPrice.isEmpty()
            ? _closingPrice
            : Double.parseDouble(openingPrice);
      _highestPrice = highestPrice == null || highestPrice.isEmpty()
            ? _closingPrice
            : Double.parseDouble(highestPrice);
      _lowestPrice = lowestPrice == null || lowestPrice.isEmpty()
            ? _closingPrice
            : Double.parseDouble(lowestPrice);

      final String effectiveTime = time == null ? "00:00:00" : time;
      _time = LocalDateTime.parse(date + "T" + effectiveTime,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME);
   }

   /**
    * @param time     The time of this candle
    * @param previous The candle temporally before this one
    * @param next     The candle temporally after this one
    */
   StandardCandle(LocalDateTime time, Candle previous, Candle next) {
      final long previousToNow = Duration.between(previous.getTime(), time)
            .toNanos();
      final long nowToNext = Duration.between(time, next.getTime()).toNanos();
      final long sum = previousToNow + nowToNext;

      final double prevRatio = sum == 0
            ? 0.5
            : 1.0 - (double) (previousToNow) / sum;
      final double nextRatio = sum == 0
            ? 0.5
            : 1.0 - (double) (nowToNext) / sum;

      _time = time;
      _openingPrice = previous.getOpeningPrice() * prevRatio +
            next.getOpeningPrice() * nextRatio;
      _closingPrice = previous.getClosingPrice() * prevRatio +
            next.getClosingPrice() * nextRatio;
      _lowestPrice =
            previous.getLowestPrice() * prevRatio + next.getLowestPrice() * nextRatio;
      _highestPrice =
            previous.getHighestPrice() * prevRatio + next.getHighestPrice() * nextRatio;
   }

   private void nullCheck(String value, String valueType) {
      if (value == null) {
         throw new RuntimeException(String.format("The %s is null", valueType));
      }
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getTime()
    */
   @Override
   public LocalDateTime getTime() {
      return _time;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getHighestPrice()
    */
   @Override
   public double getHighestPrice() {
      return _highestPrice;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getOpeningPrice()
    */
   @Override
   public double getOpeningPrice() {
      return _openingPrice;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getLowestPrice()
    */
   @Override
   public double getLowestPrice() {
      return _lowestPrice;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getClosingPrice()
    */
   @Override
   public double getClosingPrice() {
      return _closingPrice;
   }
}
