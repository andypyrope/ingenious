package io.github.andypyrope.fitness.data.candle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StandardCandle implements Candle {
   private final LocalDateTime _time;

   private final double _open;
   private final double _high;
   private final double _low;
   private final double _close;

   StandardCandle(String time, String date, String open, String high,
      String low, String close) {

      nullCheck(close, "closing price");
      nullCheck(date, "date");

      _close = Double.parseDouble(close);
      _open = open == null || open.equals("")
         ? _close
         : Double.parseDouble(open);
      _high = high == null || high.equals("")
         ? _close
         : Double.parseDouble(high);
      _low = low == null || low.equals("") ? _close : Double.parseDouble(low);

      final String effectiveTime = time == null ? "00:00:00" : time;
      _time = LocalDateTime.parse(date + "T" + effectiveTime,
         DateTimeFormatter.ISO_LOCAL_DATE_TIME);
   }

   /**
    * @param time The time of this candle
    * @param previous The candle temporally before this one
    * @param next The candle temporally after this one
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
      _open = previous.getOpeningPrice() * prevRatio +
            next.getOpeningPrice() * nextRatio;
      _close = previous.getClosingPrice() * prevRatio +
            next.getClosingPrice() * nextRatio;
      _low = previous.getLow() * prevRatio + next.getLow() * nextRatio;
      _high = previous.getHigh() * prevRatio + next.getHigh() * nextRatio;
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
    * @see io.github.andypyrope.fitness.data.candle.Candle#getHigh()
    */
   @Override
   public double getHigh() {
      return _high;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getOpeningPrice()
    */
   @Override
   public double getOpeningPrice() {
      return _open;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getLow()
    */
   @Override
   public double getLow() {
      return _low;
   }

   /*
    * (non-Javadoc)
    * @see io.github.andypyrope.fitness.data.candle.Candle#getClosingPrice()
    */
   @Override
   public double getClosingPrice() {
      return _close;
   }
}
