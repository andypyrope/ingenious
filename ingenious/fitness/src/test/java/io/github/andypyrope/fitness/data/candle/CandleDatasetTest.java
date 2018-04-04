package io.github.andypyrope.fitness.data.candle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.data.DatasetCreationException;

public class CandleDatasetTest {

   private String _exceptionMessage;

   @Test
   public void testCreation() {
      final Candle[] data = tryToCreate("datasets/candle/test-creation.csv")
            .getData();
      assertNotNull(data);

      assertEquals(2626.07, data[0].getClosingPrice());
      assertEquals(2634.89, data[0].getHigh());
      assertEquals(2620.32, data[0].getLow());
      assertEquals(2627.82, data[0].getOpeningPrice());

      // Test that the sorting works properly
      final Double[] expected = new Double[] { 2626.07, 2651.50, 2728.12,
         2659.99 };
      final Double[] actual = new Double[] { data[0].getClosingPrice(),
         data[1].getClosingPrice(), data[2].getClosingPrice(),
         data[3].getClosingPrice() };
      compareDoubleArrays(expected, actual);
   }

   @Test
   public void testWithEqualDates() {
      final Candle[] data = tryToCreate("datasets/candle/test-equal-dates.csv")
            .getData();
      assertNotNull(data);
   }

   @Test
   public void testNonCsvCreation() {
      assertNull(tryToCreate("datasets/candle/test.txt"));
      assertEquals(
         "The data file 'datasets/candle/test.txt' should have a CSV extension",
         _exceptionMessage);
   }

   @Test
   public void testNonExistentCreation() {
      assertNull(tryToCreate("datasets/candle/test-nonexistent.csv"));
      assertEquals(
         "Data file 'datasets/candle/test-nonexistent.csv' does not exist",
         _exceptionMessage);
   }

   @Test
   public void testDuplicateColumn() {
      assertNull(tryToCreate("datasets/candle/test-duplicate-column.csv"));
      assertEquals(
         "The column 'CLOSE' in CSV file 'datasets/candle/test-duplicate-column.csv' has been encountered twice",
         _exceptionMessage);
   }

   @Test
   public void testIncorrectCellCount() {
      assertNull(tryToCreate("datasets/candle/test-incorrect-cell-count.csv"));
      assertEquals(
         "Line '3' in CSV file 'datasets/candle/test-incorrect-cell-count.csv' " +
               "has an incorrect number of cells (4 instead of 5)",
         _exceptionMessage);
   }

   @Test
   public void testMissingColumn() {
      assertNull(tryToCreate("datasets/candle/test-missing-column.csv"));
      assertEquals("Column 'close' does not exist", _exceptionMessage);
   }

   private void compareDoubleArrays(Double[] a, Double[] b) {
      for (int i = 0; i < a.length; i++) {
         if (a[i] == null && b[i] == null) {
            continue;
         }
         if ((a[i] == null) != (b[i] == null) ||
               Math.abs(a[i] - b[i]) > 0.00001) {
            throw new RuntimeException(
               String.format(
                  "Expected the 'Double' arrays [%s] and [%s] to be equal",
                  Arrays.stream(a).map(element -> String.valueOf(element))
                        .collect(Collectors.joining(", ")),
                  Arrays.stream(b).map(element -> String.valueOf(element))
                        .collect(Collectors.joining(","))));
         }
      }
   }

   private CandleDataset tryToCreate(final String filename) {
      try {
         return new CandleDataset(filename);
      } catch (DatasetCreationException e) {
         _exceptionMessage = e.getMessage();
         return null;
      }
   }
}
