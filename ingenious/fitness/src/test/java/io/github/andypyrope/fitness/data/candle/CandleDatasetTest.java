package io.github.andypyrope.fitness.data.candle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.data.DatasetCreationException;

public class CandleDatasetTest {

   private String _exceptionMessage;

   @Test
   public void testCreation() {
      List<Candle> data = tryToCreate("datasets/candle/test-creation.csv")
               .getData();
      assertNotNull(data);

      final int firstClosingPrice = 272812;
      assertEquals(firstClosingPrice, data.get(0).getClosingPrice());
      assertEquals(firstClosingPrice, data.get(0).getHigh());
      assertEquals(firstClosingPrice, data.get(0).getLow());
      assertEquals(264621, data.get(2).getOpeningPrice());
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
         "Line '4' in CSV file 'datasets/candle/test-incorrect-cell-count.csv' " +
                  "has an incorrect number of cells (4 instead of 5)",
         _exceptionMessage);
   }

   @Test
   public void testMissingColumn() {
      assertNull(tryToCreate("datasets/candle/test-missing-column.csv"));
      assertEquals("Column 'close' does not exist", _exceptionMessage);
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
