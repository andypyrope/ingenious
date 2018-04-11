package io.github.andypyrope.fitness.data.candle;

import io.github.andypyrope.fitness.testutil.TestUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardCandleTest {

   private static final String DEFAULT_DATE = "2018-03-12";
   private static final String DEFAULT_CLOSING_PRICE = "2.01";

   @Test
   void testMinimal() {
      Candle candle = new StandardCandle(
            null,
            DEFAULT_DATE,
            null,
            null,
            null,
            DEFAULT_CLOSING_PRICE);
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getOpeningPrice());
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getLow());
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getHigh());
      TestUtil.compareDoubles(2.01, candle.getClosingPrice());

      candle = new StandardCandle(
            null,
            DEFAULT_DATE,
            "",
            "",
            "",
            DEFAULT_CLOSING_PRICE);
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getOpeningPrice());
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getLow());
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getHigh());
      TestUtil.compareDoubles(2.01, candle.getClosingPrice());
   }

   @Test
   void testCandleBetweenTwoOthers() {
      final double firstOpen = 21.21;
      final double firstHigh = 120.64;
      final double firstLow = 28;
      final double firstClose = 104.2;
      final Candle firstCandle = new StandardCandle(
            null,
            "2018-03-12",
            String.valueOf(firstOpen),
            String.valueOf(firstHigh),
            String.valueOf(firstLow),
            String.valueOf(firstClose));
      final double secondOpen = 85.00;
      final double secondHigh = 91.01;
      final double secondLow = 28.05;
      final double secondClose = 73.23232323;
      final Candle secondCandle = new StandardCandle(
            "03:04",
            "2018-03-13",
            String.valueOf(secondOpen),
            String.valueOf(secondHigh),
            String.valueOf(secondLow),
            String.valueOf(secondClose));

      final Candle middleCandle = new StandardCandle(
            LocalDateTime.parse("2018-03-12T04:21:24"),
            firstCandle,
            secondCandle);
      TestUtil.compareDoubles(31.477676108374382, middleCandle.getOpeningPrice());
      TestUtil.compareDoubles(115.87073768472906, middleCandle.getHigh());
      TestUtil.compareDoubles(28.008048029556647, middleCandle.getLow());
      TestUtil.compareDoubles(99.21542444108496, middleCandle.getClosingPrice());
   }

   @Test
   void testCandleBetweenTwoWithTheSameTime() {
      final Candle firstCandle = new StandardCandle(
            "23:01:03",
            "2018-03-12",
            String.valueOf(1.3),
            String.valueOf(10.7),
            String.valueOf(0.4),
            String.valueOf(0.4));
      final Candle secondCandle = new StandardCandle(
            "23:01:03",
            "2018-03-12",
            String.valueOf(1.23),
            String.valueOf(5.35),
            String.valueOf(1.24),
            String.valueOf(4.2));

      final Candle middleCandle = new StandardCandle(
            LocalDateTime.parse("2018-03-12T23:01:03"),
            firstCandle,
            secondCandle);

      TestUtil.compareDoubles(
            (firstCandle.getOpeningPrice() + secondCandle.getOpeningPrice()) / 2.0,
            middleCandle.getOpeningPrice());
      TestUtil.compareDoubles(
            (firstCandle.getHigh() + secondCandle.getHigh()) / 2.0,
            middleCandle.getHigh());
      TestUtil.compareDoubles((firstCandle.getLow() + secondCandle.getLow()) / 2.0,
            middleCandle.getLow());
      TestUtil.compareDoubles(
            (firstCandle.getClosingPrice() + secondCandle.getClosingPrice()) / 2.0,
            middleCandle.getClosingPrice());
   }

   @Test
   void testFull() {
      final Candle candle = new StandardCandle(
            "20:12",
            DEFAULT_DATE,
            "1.2",
            null,
            "24.00",
            "123.01");
      TestUtil.compareDoubles(1.20, candle.getOpeningPrice());
      TestUtil.compareDoubles(candle.getClosingPrice(), candle.getHigh());
      TestUtil.compareDoubles(24.00, candle.getLow());
      TestUtil.compareDoubles(123.01, candle.getClosingPrice());
   }

   @Test
   void testNoClosingPrice() {
      String exceptionMessage = null;
      try {
         new StandardCandle(null, DEFAULT_DATE, null, null, null, null);
      } catch (Exception e) {
         exceptionMessage = e.getMessage();
      }
      assertEquals("The closing price is null", exceptionMessage);
   }

   @Test
   void testNoDate() {
      String exceptionMessage = null;
      try {
         new StandardCandle(null, null, null, null, null, DEFAULT_CLOSING_PRICE);
      } catch (Exception e) {
         exceptionMessage = e.getMessage();
      }
      assertEquals("The date is null", exceptionMessage);
   }
}
