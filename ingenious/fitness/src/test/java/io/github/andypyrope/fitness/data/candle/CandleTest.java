package io.github.andypyrope.fitness.data.candle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CandleTest {

   private static final String DEFAULT_DATE = "2018-03-12";
   private static final String DEFAULT_CLOSING_PRICE = "2.01";

   @Test
   public void testMinimal() {
      Candle candle = new Candle(
         null,
         DEFAULT_DATE,
         null,
         null,
         null,
         DEFAULT_CLOSING_PRICE);
      assertEquals(candle.getClosingPrice(), candle.getOpeningPrice());
      assertEquals(candle.getClosingPrice(), candle.getLow());
      assertEquals(candle.getClosingPrice(), candle.getHigh());
      assertEquals(201, candle.getClosingPrice());

      candle = new Candle(
         null,
         DEFAULT_DATE,
         "",
         "",
         "",
         DEFAULT_CLOSING_PRICE);
      assertEquals(candle.getClosingPrice(), candle.getOpeningPrice());
      assertEquals(candle.getClosingPrice(), candle.getLow());
      assertEquals(candle.getClosingPrice(), candle.getHigh());
      assertEquals(201, candle.getClosingPrice());
   }

   @Test
   public void testTime() {
      final String closing = "2";
      final Candle firstCandle = new Candle(
         null,
         "2018-03-12",
         null,
         null,
         null,
         closing);
      final Candle otherCandle = new Candle(
         "03:04:12",
         "2018-03-13",
         null,
         null,
         null,
         closing);

      final int expectedTime = (24 + 3) * 60 + 4;

      assertEquals(expectedTime, firstCandle.minutesTo(otherCandle));
      assertEquals(-1 * expectedTime, otherCandle.minutesTo(firstCandle));
   }

   @Test
   public void testFull() {
      final Candle candle = new Candle(
         "20:12:12",
         DEFAULT_DATE,
         "1.2",
         null,
         "24.00",
         "123.01");
      assertEquals(120, candle.getOpeningPrice());
      assertEquals(candle.getClosingPrice(), candle.getHigh());
      assertEquals(2400, candle.getLow());
      assertEquals(12301, candle.getClosingPrice());
   }

   @Test
   public void testNoClosingPrice() {
      String exceptionMessage = null;
      try {
         new Candle(null, DEFAULT_DATE, null, null, null, null);
      } catch (Exception e) {
         exceptionMessage = e.getMessage();
      }
      assertEquals("The closing price is null", exceptionMessage);
   }

   @Test
   public void testNoDate() {
      String exceptionMessage = null;
      try {
         new Candle(null, null, null, null, null, DEFAULT_CLOSING_PRICE);
      } catch (Exception e) {
         exceptionMessage = e.getMessage();
      }
      assertEquals("The date is null", exceptionMessage);
   }
}
