package io.github.andypyrope.fitness.data.candle;

import java.util.Calendar;

public class Candle {
   private final Calendar _time;
   private final int _open;
   private final int _high;
   private final int _low;
   private final int _close;

   Candle(String time, String date, String open, String high, String low,
      String close) {

      nullCheck(close, "closing price");
      nullCheck(date, "date");
      
      // NOTE alalev: Assuming the values have only two decimal places
      _close = getPrice(close);
      _open = (open == null || open.equals("")) ? _close : getPrice(open);
      _high = (high == null || high.equals("")) ? _close : getPrice(high);
      _low = (low == null || low.equals("")) ? _close : getPrice(low);

      int hour = 0;
      int minute = 0;
      if (time != null) {
         final String[] timeParts = time.split(":");
         hour = Integer.parseInt(timeParts[0]);
         minute = Integer.parseInt(timeParts[1]);
      }

      final String[] dateParts = date.split("-");
      final int year = Integer.parseInt(dateParts[0]);
      final int month = Integer.parseInt(dateParts[1]);
      final int day = Integer.parseInt(dateParts[2]);
      _time = Calendar.getInstance();
      _time.set(year, month, day, hour, minute);
   }

   private void nullCheck(String value, String valueType) {
      if (value == null) {
         throw new RuntimeException(String.format("The %s is null", valueType));
      }
   }

   private int getPrice(String rawPrice) {
      final String[] splitPrice = rawPrice.split("\\.");
      final int mainPart = Integer.parseInt(splitPrice[0]) * 100;
      if (splitPrice.length == 1) {
         return mainPart;
      }
      final int decimalPart = Integer.parseInt(splitPrice[1]);
      final int multiplier = splitPrice[1].length() == 1 ? 10 : 1;

      return mainPart + decimalPart * multiplier;
   }

   public int getHigh() {
      return _high;
   }

   public int getOpeningPrice() {
      return _open;
   }

   public int getLow() {
      return _low;
   }

   public int getClosingPrice() {
      return _close;
   }

   public int minutesTo(Candle other) {
      final long millisecondsTo = other._time.getTimeInMillis() -
               _time.getTimeInMillis();
      return Math.toIntExact(millisecondsTo / (1000 * 60));
   }
}
