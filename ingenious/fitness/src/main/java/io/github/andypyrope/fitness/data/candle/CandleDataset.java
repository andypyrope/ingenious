package io.github.andypyrope.fitness.data.candle;

import io.github.andypyrope.fitness.data.common.CsvDataset;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandleDataset extends CsvDataset {

   private final Candle[] _data;

   private static final String TIME_COLUMN = "time";
   private static final String DATE_COLUMN = "date";
   private static final String OPEN_COLUMN = "open";
   private static final String HIGH_COLUMN = "high";
   private static final String LOW_COLUMN = "low";
   private static final String CLOSE_COLUMN = "close";

   /**
    * @param filename The file name to the dataset file
    */
   public CandleDataset(final String filename) {
      super(filename);

      _data = new Candle[_rowCount];
      for (int i = 0; i < _rowCount; i++) {
         _data[i] = new StandardCandle(
            getCell(i, TIME_COLUMN, true),
            getCell(i, DATE_COLUMN),
            getCell(i, OPEN_COLUMN),
            getCell(i, HIGH_COLUMN),
            getCell(i, LOW_COLUMN),
            getCell(i, CLOSE_COLUMN));
      }

      Arrays.sort(_data, (candle1, candle2) -> {
         if (candle1.getTime().isEqual(candle2.getTime())) {
            return 0;
         }
         return candle1.getTime().isAfter(candle2.getTime()) ? 1 : -1;
      });
   }

   public Candle[] getData() {
      return _data;
   }

   public Candle[] getSmoothData(long distance, TemporalUnit distanceUnit) {
      final List<Candle> result = new ArrayList<>();
      LocalDateTime currentTime = _data[0].getTime();
      int index = 0;
      int lastIndex = 0;

      while (index < _data.length) {
         result.add(
            new StandardCandle(currentTime, _data[lastIndex], _data[index]));
         currentTime = currentTime.plus(distance, distanceUnit);

         while (index < _data.length &&
               currentTime.isAfter(_data[index].getTime())) {
            lastIndex = index;
            index++;
         }
      }

      return result.toArray(new Candle[0]);
   }

}
