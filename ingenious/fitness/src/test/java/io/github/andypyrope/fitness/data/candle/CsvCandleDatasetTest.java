package io.github.andypyrope.fitness.data.candle;

import io.github.andypyrope.fitness.data.DatasetCreationException;
import io.github.andypyrope.fitness.testutil.TestUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CsvCandleDatasetTest {

   private String _exceptionMessage;

   @Test
   void testCreation() {
      final Candle[] data = new CsvCandleDataset(
            Paths.get("datasets", "candle", "test-creation.csv").toString()).getData();
      assertNotNull(data);

      assertEquals(2626.07, data[0].getClosingPrice());
      assertEquals(2634.89, data[0].getHighestPrice());
      assertEquals(2620.32, data[0].getLowestPrice());
      assertEquals(2627.82, data[0].getOpeningPrice());

      // Test that the sorting works properly
      final Double[] expected = new Double[]{2626.07, 2651.50, 2728.12,
            2659.99};
      final Double[] actual = new Double[]{data[0].getClosingPrice(),
            data[1].getClosingPrice(), data[2].getClosingPrice(),
            data[3].getClosingPrice()};
      TestUtil.compareDoubleArrays(expected, actual);
   }

   @Test
   void testWithEqualDates() {
      final Candle[] data = new CsvCandleDataset(
            getPath("test-equal-dates.csv")).getData();
      assertNotNull(data);
   }

   @Test
   void testNonCsvCreation() {
      final String path = getPath("test.txt");

      assertNull(tryToCreate(path));
      assertEquals(String.format("The data file '%s' should have a CSV extension", path),
            _exceptionMessage);
   }

   @Test
   void testNonExistentCreation() {
      final String path = getPath("test-nonexistent.csv");

      assertNull(tryToCreate(path));
      assertEquals(String.format("Data file '%s' does not exist", path),
            _exceptionMessage);
   }

   @Test
   void testDuplicateColumn() {
      final String path = getPath("test-duplicate-column.csv");
      assertNull(tryToCreate(path));
      assertEquals(String.format(
            "The column 'CLOSE' in CSV file '%s' has been encountered twice", path),
            _exceptionMessage);
   }

   @Test
   void testIncorrectCellCount() {
      final String path = getPath("test-incorrect-cell-count.csv");

      assertNull(tryToCreate(path));
      assertEquals(String.format("Line '3' in CSV file '%s' " +
                  "has an incorrect number of cells (4 instead of 5)", path),
            _exceptionMessage);
   }

   @Test
   void testMissingColumn() {
      assertNull(tryToCreate(getPath("test-missing-column.csv")));
      assertEquals("Column 'close' does not exist", _exceptionMessage);
   }

   private String getPath(final String filename) {
      return Paths.get("datasets", "candle", filename).toString();
   }

   private CandleDataset tryToCreate(final String filename) {
      try {
         return new CsvCandleDataset(filename);
      } catch (DatasetCreationException e) {
         _exceptionMessage = e.getMessage();
         return null;
      }
   }
}
